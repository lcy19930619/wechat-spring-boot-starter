package net.jlxxw.wechat.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.jlxxw.wechat.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.dto.message.event.SubscribeEventMessage;
import net.jlxxw.wechat.dto.message.event.SubscribeQrsceneEventMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.exception.AesException;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 事件总线
 *
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:35 上午
 */
@Component
public class EventBus {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);
    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired(required = false)
    private List<AbstractWeChatMessageListener> abstractWeChatMessageListeners;
    @Autowired(required = false)
    private List<AbstractWeChatEventListener> abstractWeChatEventListeners;
    @Autowired(required = false)
    private WeChatMsgCodec weChatMsgCodec;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired(required = false)
    private UnKnowWeChatEventListener unKnowWeChatEventListener;
    @Autowired(required = false)
    private UnKnowWeChatMessageListener unKnowWeChatMessageListener;
    @Autowired
    private ThreadPoolTaskExecutor eventBusThreadPool;

    /**
     * 消息处理监听器
     * key 支持的消息类型
     * value 消息监听器
     */
    private final Map<WeChatMessageTypeEnum, AbstractWeChatMessageListener> messageListenerMap = new HashMap<>();
    /**
     * 消息处理监听器
     * key 支持的消息类型
     * value 消息监听器
     */
    private final Map<WeChatEventTypeEnum, AbstractWeChatEventListener> eventListenerMap = new HashMap<>();

    /**
     * 消息枚举和type映射集合
     * key WeChatMessageTypeEnum.key
     * value WeChatMessageTypeEnum
     */
    private static final Map<String,WeChatMessageTypeEnum> MESSAGE_TYPE_ENUM_MAP = new HashMap<>(16);
    /**
     * 事件枚举和type映射集合
     * key WeChatEventTypeEnum.eventCode
     * value WeChatEventTypeEnum
     */
    private static final Map<String,WeChatEventTypeEnum> EVENT_TYPE_ENUM_MAP = new HashMap<>(16);

    static {
        WeChatMessageTypeEnum[] messageTypeEnums = WeChatMessageTypeEnum.values();
        for (WeChatMessageTypeEnum messageTypeEnum : messageTypeEnums) {
            MESSAGE_TYPE_ENUM_MAP.put(messageTypeEnum.getKey(),messageTypeEnum);
        }
        WeChatEventTypeEnum[] eventTypeEnums = WeChatEventTypeEnum.values();
        for (WeChatEventTypeEnum eventTypeEnum : eventTypeEnums) {
            EVENT_TYPE_ENUM_MAP.put(eventTypeEnum.getEventCode(),eventTypeEnum);
        }

    }
    @PostConstruct
    public void postConstruct() {
        // 初始化xmlMapper相关配置
        XML_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        XML_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        XML_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

        // 初始化objectMapper相关配置
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

        if (!CollectionUtils.isEmpty(abstractWeChatMessageListeners)) {
            // 如果已经配置了微信监听器，则按照支持到类型枚举，进行分类
            Map<WeChatMessageTypeEnum, List<AbstractWeChatMessageListener>> map = abstractWeChatMessageListeners.stream().collect(Collectors.groupingBy(AbstractWeChatMessageListener::supportMessageType));

            map.forEach((k, v) -> {
                if (v.size() > 1) {
                    // 因为每个消息都需要由一个返回值，如果配置多个监听器，则无法知道哪个返回值可用，故，限制只能有一个监听器
                    throw new BeanCreationException("微信 "+ k.getDescription() +" messageListener不能注册多次");
                }
                messageListenerMap.put(k, v.get(0));
            });
        }

        // 警告信息打印，提示未注册的监听器
        final WeChatMessageTypeEnum[] values = WeChatMessageTypeEnum.values();
        for (WeChatMessageTypeEnum value : values) {
            if (!messageListenerMap.containsKey(value)) {
                LoggerUtils.warn(logger, value.getDescription() + "消息处理器未注册!!!");
            }
        }

        if (!CollectionUtils.isEmpty(abstractWeChatEventListeners)) {
            // 如果已经配置了微信监听器，则按照支持到类型枚举，进行分类
            Map<WeChatEventTypeEnum, List<AbstractWeChatEventListener>> eventMap = abstractWeChatEventListeners.stream().collect(Collectors.groupingBy(AbstractWeChatEventListener::supportEventType));
            eventMap.forEach((k, v) -> {
                if (v.size() > 1) {
                    // 因为每个事件都需要由一个返回值，如果配置多个监听器，则无法知道哪个返回值可用，故，限制只能有一个监听器
                    throw new BeanCreationException("微信 "+ k.getDescription() +" eventListener不能注册多次");
                }
                eventListenerMap.put(k, v.get(0));
            });
        }

        // 警告信息打印，提示未注册的监听器
        final WeChatEventTypeEnum[] eventValues = WeChatEventTypeEnum.values();
        for (WeChatEventTypeEnum value : eventValues) {
            if (!eventListenerMap.containsKey(value)) {
                LoggerUtils.warn(logger, value.getDescription() + "事件处理器未注册!!!");
            }
        }
    }

    /**
     * 微信请求事件分发处理，用于controller接口处理
     *
     * @param request request全部信息
     * @return 返回给微信的数据
     */
    public String dispatcher(HttpServletRequest request) {
        final Future<String> future = eventBusThreadPool.submit(() -> {
            // 从request中取得输入流
            InputStream inputStream = request.getInputStream();
            Reader reader = new InputStreamReader(inputStream);
            return handlerWeiXinMessage(reader);
        });
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            final Enumeration<String> parameterNames = request.getParameterNames();
            JSONObject jsonObject = new JSONObject();
            while (parameterNames.hasMoreElements()) {
                final String name = parameterNames.nextElement();
                jsonObject.put(name, request.getParameter(name));
            }
            logger.error("事件分发处理出现异常,微信参数:{},异常信息:{}", jsonObject.toJSONString(), e);
            return "";
        }
    }

    /**
     * 微信消息加解密处理，netty使用
     *
     * @param bytes 微信请求的全部数据
     * @param uri   微信携带的uri，用于获取加解密参数内容
     * @throws AesException 微信信息加解密异常
     */
    public String dispatcher(byte[] bytes, String uri) throws AesException {
        if (weChatProperties.isEnableMessageEnc()) {
            // 微信发送进来的xml
            String inputXML = new String(bytes, StandardCharsets.UTF_8);

            /*
                处理微信uri参数，并封装到map中
             */
            Map<String, String> map = new HashMap<>(16);
            int index = uri.indexOf("?");
            String str = uri.substring(index + 1);
            String[] split = str.split("&");
            for (String s : split) {
                String[] arr = s.split("=");
                map.put(arr[0], arr[1]);
            }
            // 获取签名
            String msgSignature = map.get("msg_signature");
            // 获取时间戳
            String timestamp = map.get("timestamp");
            // 获取随机串
            String nonce = map.get("nonce");
            // 消息解密
            String decryptMsg = weChatMsgCodec.decryptMsg(msgSignature, timestamp, nonce, inputXML);
            LoggerUtils.debug(logger, "微信消息解密成功，信息为:{}", decryptMsg);
            // 将解密后的数据，转换为byte数组，用于协议的具体处理
            bytes = decryptMsg.getBytes(StandardCharsets.UTF_8);
        }
        // 调用具体的分发器，实现数据的处理
        String result = dispatcher(bytes);
        if (weChatProperties.isEnableMessageEnc()) {
            // 如果启用了信息加解密功能，则对返回值进行加密处理
            result = weChatMsgCodec.encrypt(result);
            LoggerUtils.debug(logger, "微信消息加密成功，信息为:{}", result);
        }
        return result;
    }

    /**
     * 处理微信请求信息，无加密，netty使用
     */
    public String dispatcher(byte[] bytes) {
        try {
            // jackson会自动关闭流，不需要手动关闭
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            Reader reader = new InputStreamReader(inputStream);
            return handlerWeiXinMessage(reader);
        } catch (Exception e) {
            logger.error("事件分发处理出现异常,微信参数:{},异常信息:", new String(bytes, StandardCharsets.UTF_8), e);
            return "";
        }
    }

    /**
     * 具体对微信信息处理
     *
     * @param reader 微信输入的数据
     * @return 微信处理的结果
     * @throws IOException io异常
     */
    private String handlerWeiXinMessage(Reader reader) throws IOException {
        // 将输入的内容转换为ObjectNode统一处理,objectNode.toString() 为json格式
        ObjectNode objectNode = XML_MAPPER.readValue(reader, ObjectNode.class);
        // 获取消息类型，根据类型寻找对应监听器进行处理
        final String msgType = objectNode.get("MsgType").textValue();

        AbstractWeChatMessage abstractWeChatMessage;
        if("event".equals(msgType)){
            // 如果是事件类型，则使用事件进行处理
            String event = objectNode.get("Event").textValue();
            WeChatEventTypeEnum weChatEventTypeEnum = EVENT_TYPE_ENUM_MAP.get(event);
            if(Objects.isNull(weChatEventTypeEnum)){
                // 未注册的事件枚举，兜底处理
                if (Objects.isNull(unKnowWeChatEventListener)) {
                    // 未能使用兜底事件处理器，则直接丢出异常
                    throw new IllegalArgumentException("未知的事件请求信息类型，event:" + event + ",请求数据信息:" + objectNode);
                }
                // 未注册的枚举类型，则使用兜底的事件处理器
                return unKnowWeChatEventListener.handlerOtherType(objectNode);
            }else{
                if(event.equals(WeChatEventTypeEnum.SUBSCRIBE.getEventCode())){
                    // 关注事件类型
                    // 将数据转换为事件支持的传输对象
                    if(Objects.isNull(objectNode.get("EventKey"))){
                        // 普通的关注事件
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.SUBSCRIBE);
                    }

                    String eventKey = objectNode.get("EventKey").textValue();
                    if (eventKey != null && eventKey.contains("qrscene_") && WeChatEventTypeEnum.SUBSCRIBE.equals(weChatEventTypeEnum) ) {
                        // 以qrscene_开头的关注事件，为扫码关注事件，扫码关注事件单独处理
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeQrsceneEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.QRSCENE_SUBSCRIBE);
                    }else{
                        // 普通关注事件，自动转换处理
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), weChatEventTypeEnum.getCoverEventClass());
                        return handlerEvent(abstractWeChatMessage, weChatEventTypeEnum);
                    }
                }

                // 普通事件，自动转换处理
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), weChatEventTypeEnum.getCoverEventClass());
                return handlerEvent(abstractWeChatMessage, weChatEventTypeEnum);
            }
        }

        // 消息处理兜底
        WeChatMessageTypeEnum weChatMessageTypeEnum = MESSAGE_TYPE_ENUM_MAP.get(msgType);
        if(Objects.isNull(weChatMessageTypeEnum)){
            // 未注册的枚举，要进行兜底处理
            if (Objects.isNull(unKnowWeChatMessageListener)) {
                // 未能使用兜底事件处理器，则抛出异常
                throw new IllegalArgumentException("未知的消息请求信息类型,messageType:" + msgType + ",请求数据信息:" + objectNode);
            }
            // 进行兜底处理
            return unKnowWeChatMessageListener.handlerOtherType(objectNode);
        }
        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), weChatMessageTypeEnum.getConverClass());
        return handlerMessage(abstractWeChatMessage, weChatMessageTypeEnum);
    }


    /**
     * 处理微信信息，若出现异常，则向返回空白字符串
     *
     * @param abstractWeChatMessage 微信消息
     * @param weChatMessageTypeEnum 消息类型枚举
     * @return 处理完毕的xml字符串
     */
    private String handlerMessage(AbstractWeChatMessage abstractWeChatMessage, WeChatMessageTypeEnum weChatMessageTypeEnum) {
        if (CollectionUtils.isEmpty(abstractWeChatMessageListeners)) {
            throw new IllegalArgumentException("未注册任何相关消息监听器，或监听器未加入到ioc容器中,当前微信请求参数:" + JSON.toJSONString(abstractWeChatMessage));
        }
        final AbstractWeChatMessageListener abstractWeChatMessageListener = messageListenerMap.get(weChatMessageTypeEnum);

        if (Objects.isNull(abstractWeChatMessageListener)) {
            throw new IllegalArgumentException(weChatMessageTypeEnum.name() + "消息监听器未注册,当前微信请求参数:" + JSON.toJSONString(abstractWeChatMessage));
        }

        LoggerUtils.debug(logger, "接收到微信请求，请求类型:{},请求参数:{}", weChatMessageTypeEnum.getDescription(), JSON.toJSONString(abstractWeChatMessage));
        // 执行消息处理
        WeChatMessageResponse response = abstractWeChatMessageListener.handler(abstractWeChatMessage);
        if (Objects.isNull(response)) {
            return "";
        }
        String toUserName = abstractWeChatMessage.getToUserName();
        String fromUserName = abstractWeChatMessage.getFromUserName();
        response.setFromUserName(toUserName);
        response.setCreateTime(System.currentTimeMillis() / 1000);
        response.setToUserName(fromUserName);
        try {
            String res = XML_MAPPER.writeValueAsString(response);
            LoggerUtils.debug(logger, "返回微信应答信息，参数:{}", res);
            return res;
        } catch (JsonProcessingException e) {
            LoggerUtils.error(logger, "jackson 转xml失败，输入参数:" + JSON.toJSONString(response), e);
            return "";
        }
    }

    /**
     * 处理微信事件，若出现异常，则向返回空白字符串
     *
     * @param abstractWeChatMessage 微信事件信息
     * @param weChatEventTypeEnum   微信事件枚举
     * @return 处理结果
     */
    private String handlerEvent(AbstractWeChatMessage abstractWeChatMessage, WeChatEventTypeEnum weChatEventTypeEnum) {
        if (CollectionUtils.isEmpty(abstractWeChatEventListeners)) {
            throw new IllegalArgumentException("未注册相关事件监听器，或监听器未加入到ioc容器中,当前微信请求参数:" + JSON.toJSONString(abstractWeChatMessage));
        }
        final AbstractWeChatEventListener abstractWeChatEventListener = eventListenerMap.get(weChatEventTypeEnum);

        if (Objects.isNull(abstractWeChatEventListener)) {
            throw new IllegalArgumentException(weChatEventTypeEnum.name() + "事件监听器未注册,当前微信请求参数:"+ JSON.toJSONString(abstractWeChatMessage));
        }
        // 执行事件处理
        WeChatMessageResponse response = abstractWeChatEventListener.handler(abstractWeChatMessage);
        if (Objects.isNull(response)) {
            return "";
        }
        String toUserName = abstractWeChatMessage.getToUserName();
        String fromUserName = abstractWeChatMessage.getFromUserName();
        response.setFromUserName(toUserName);
        response.setCreateTime(System.currentTimeMillis() / 1000);
        response.setToUserName(fromUserName);
        try {
            return XML_MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            LoggerUtils.error(logger, "jackson 转xml失败，输入参数:" + JSON.toJSONString(response), e);
            return "";
        }
    }
}
