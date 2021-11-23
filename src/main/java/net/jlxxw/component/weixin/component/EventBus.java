package net.jlxxw.component.weixin.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.jlxxw.component.weixin.component.listener.AbrstractWeiXinEventListener;
import net.jlxxw.component.weixin.component.listener.AbrstractWeiXinMessageListener;
import net.jlxxw.component.weixin.component.listener.UnKnowWeiXinEventListener;
import net.jlxxw.component.weixin.component.listener.UnKnowWeiXinMessageListener;
import net.jlxxw.component.weixin.dto.message.*;
import net.jlxxw.component.weixin.dto.message.event.*;
import net.jlxxw.component.weixin.enums.WeiXinEventTypeEnum;
import net.jlxxw.component.weixin.enums.WeiXinMessageTypeEnum;
import net.jlxxw.component.weixin.exception.AesException;
import net.jlxxw.component.weixin.properties.WeiXinProperties;
import net.jlxxw.component.weixin.response.WeiXinMessageResponse;
import net.jlxxw.component.weixin.util.LoggerUtils;
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
 * @author chunyang.leng
 * @date 2021/1/20 11:35 上午
 */
@Component
public class EventBus {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);
    @Autowired(required = false)
    private List<AbrstractWeiXinMessageListener> abrstractWeiXinMessageListeners;
    @Autowired(required = false)
    private List<AbrstractWeiXinEventListener> abrstractWeiXinEventListeners;
    @Autowired(required = false)
    private WeiXinMsgCodec weiXinMsgCodec;
    @Autowired
    private WeiXinProperties weiXinProperties;
    @Autowired(required = false)
    private UnKnowWeiXinEventListener unKnowWeiXinEventListener;
    @Autowired(required = false)
    private UnKnowWeiXinMessageListener unKnowWeiXinMessageListener;
    @Autowired
    private ThreadPoolTaskExecutor eventBusThreadPool;
    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 消息处理监听器
     * key 支持的消息类型
     * value 消息监听器
     */
    private final Map<WeiXinMessageTypeEnum, AbrstractWeiXinMessageListener> messageListenerMap = new HashMap<>();
    /**
     * 消息处理监听器
     * key 支持的消息类型
     * value 消息监听器
     */
    private final Map<WeiXinEventTypeEnum, AbrstractWeiXinEventListener> eventListenerMap = new HashMap<>();


    @PostConstruct
    public void postConstruct() {
        // 初始化xmlMapper相关配置
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        // 初始化objectMapper相关配置
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        if (!CollectionUtils.isEmpty(abrstractWeiXinMessageListeners)) {
            // 如果已经配置了微信监听器，则按照支持到类型枚举，进行分类
            Map<WeiXinMessageTypeEnum, List<AbrstractWeiXinMessageListener>> map = abrstractWeiXinMessageListeners.stream().collect(Collectors.groupingBy(AbrstractWeiXinMessageListener::supportMessageType));

            map.forEach((k, v) -> {
                if (v.size() > 1) {
                    // 因为每个消息都需要由一个返回值，如果配置多个监听器，则无法知道哪个返回值可用，故，限制只能有一个监听器
                    throw new BeanCreationException("微信messageListener不能注册多次");
                }
                messageListenerMap.put(k, v.get(0));
            });
        }

        // 警告信息打印，提示未注册的监听器
        final WeiXinMessageTypeEnum[] values = WeiXinMessageTypeEnum.values();
        for (WeiXinMessageTypeEnum value : values) {
            if (!messageListenerMap.containsKey(value)) {
                LoggerUtils.warn(logger, value.getDescription() + "消息处理器未注册!!!");
            }
        }

        if (!CollectionUtils.isEmpty(abrstractWeiXinEventListeners)) {
            // 如果已经配置了微信监听器，则按照支持到类型枚举，进行分类
            Map<WeiXinEventTypeEnum, List<AbrstractWeiXinEventListener>> eventMap = abrstractWeiXinEventListeners.stream().collect(Collectors.groupingBy(AbrstractWeiXinEventListener::supportEventType));
            eventMap.forEach((k, v) -> {
                if (v.size() > 1) {
                    // 因为每个事件都需要由一个返回值，如果配置多个监听器，则无法知道哪个返回值可用，故，限制只能有一个监听器
                    throw new BeanCreationException("微信eventListener不能注册多次");
                }
                eventListenerMap.put(k, v.get(0));
            });
        }

        // 警告信息打印，提示未注册的监听器
        final WeiXinEventTypeEnum[] eventValues = WeiXinEventTypeEnum.values();
        for (WeiXinEventTypeEnum value : eventValues) {
            if (!eventListenerMap.containsKey(value)) {
                LoggerUtils.warn(logger, value.getDescription() + "事件处理器未注册!!!");
            }
        }
    }

    /**
     * 微信请求事件分发处理，用于controller接口处理
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
     * @param bytes 微信请求的全部数据
     * @param uri 微信携带的uri，用于获取加解密参数内容
     *
     */
    public String dispatcher(byte[] bytes, String uri) throws AesException {
        if (weiXinProperties.isEnableMessageEnc()) {
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
            String decryptMsg = weiXinMsgCodec.decryptMsg(msgSignature, timestamp, nonce, inputXML);
            LoggerUtils.debug(logger, "微信消息解密成功，信息为:{}", decryptMsg);
            // 将解密后的数据，转换为byte数组，用于协议的具体处理
            bytes = decryptMsg.getBytes(StandardCharsets.UTF_8);
        }
        // 调用具体的分发器，实现数据的处理
        String result = dispatcher(bytes);
        if (weiXinProperties.isEnableMessageEnc()) {
            // 如果启用了信息加解密功能，则对返回值进行加密处理
            result = weiXinMsgCodec.encrypt(result);
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
            logger.error("事件分发处理出现异常,微信参数:{},异常信息:{}", new String(bytes, StandardCharsets.UTF_8), e);
            return "";
        }
    }

    /**
     * 具体对微信信息处理
     * @param reader 微信输入的数据
     * @return 微信处理的结果
     * @throws IOException
     */
    private String handlerWeiXinMessage(Reader reader) throws IOException {
        // 将输入的内容转换为ObjectNode统一处理
        ObjectNode objectNode = xmlMapper.readValue(reader, ObjectNode.class);
        // 获取消息类型，根据类型寻找对应监听器进行处理
        final String msgType = objectNode.get("MsgType").textValue();

        AbrstractWeiXinMessage abrstractWeiXinMessage;
        switch (msgType) {
            case "text":
                // 文本信息
                abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), TextMessageAbrstract.class);
                return handlerMessage(abrstractWeiXinMessage, WeiXinMessageTypeEnum.TEXT);
            case "image":
                // 图片信息
                abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), ImageMessageAbrstract.class);
                return handlerMessage(abrstractWeiXinMessage, WeiXinMessageTypeEnum.IMAGE);
            case "voice":
                // 音频信息
                abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), VoiceMessageAbrstract.class);
                return handlerMessage(abrstractWeiXinMessage, WeiXinMessageTypeEnum.VOICE);
            case "video":
                // 视频信息
                abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), VideoMessageAbrstract.class);
                return handlerMessage(abrstractWeiXinMessage, WeiXinMessageTypeEnum.VIDEO);
            case "shortvideo":
                // 短视频信息
                abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), ShortVideoMessageAbrstract.class);
                return handlerMessage(abrstractWeiXinMessage, WeiXinMessageTypeEnum.SHORT_VIDEO);
            case "location":
                // 地理位置信息
                abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), LocationMessageAbrstract.class);
                return handlerMessage(abrstractWeiXinMessage, WeiXinMessageTypeEnum.LOCATION);
            case "link":
                // 链接信息
                abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), LinkMessageAbrstract.class);
                return handlerMessage(abrstractWeiXinMessage, WeiXinMessageTypeEnum.LINK);
            case "event":
                // 事件类型的请求内容
                String event = objectNode.get("Event").textValue();

                switch (event) {
                    // 订阅事件
                    case "subscribe":
                        if (Objects.isNull(objectNode.get("EventKey"))) {
                            abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeEventMessageAbrstract.class);
                            return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE);
                        }
                        String eventKey = objectNode.get("EventKey").textValue();
                        if (eventKey != null && eventKey.contains("qrscene_")) {
                            // 用户未关注时，进行关注后的事件推送
                            abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeQrsceneEventMessageAbrstract.class);
                            return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE_QRSCENE);
                        }
                        abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeEventMessageAbrstract.class);
                        return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE);

                    // 取消订阅
                    case "unsubscribe":
                        abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), UnSubscribeEventMessageAbrstract.class);
                        return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.UNSUBSCRIBE);

                    // 扫描二维码事件
                    case "SCAN":
                        abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeScanEventMessageAbrstract.class);
                        return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.SCAN);

                    // 地理为之信息上报事件
                    case "LOCATION":
                        abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), LocationEventMessageAbrstract.class);
                        return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.LOCATION);

                    // 点击菜单拉取消息时的事件推送
                    case "CLICK":
                        abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), ClickMenuGetInfoEventMessageAbrstract.class);
                        return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.CLICK);

                    // 模版推送回调事件
                    case "TEMPLATESENDJOBFINISH":
                        abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), TemplateEventMessageAbrstract.class);
                        return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.TEMPLATESENDJOBFINISH);

                    // 点击菜单跳转链接时的事件推送
                    case "VIEW":
                        abrstractWeiXinMessage = objectMapper.readValue(objectNode.toString(), ClickMenuGotoLinkEventMessageAbrstract.class);
                        return handlerEvent(abrstractWeiXinMessage, WeiXinEventTypeEnum.VIEW);
                    // 未知的事件，用户可自行扩展
                    default:
                        if (Objects.isNull(unKnowWeiXinEventListener)) {
                            throw new IllegalArgumentException("未知的事件请求信息类型，event:" + event + ",请求数据信息:" + objectNode.toString());
                        }
                        return unKnowWeiXinEventListener.handlerOtherType(objectNode);
                }

            // 未知的消息，用户可自行扩展
            default:
                if (Objects.isNull(unKnowWeiXinMessageListener)) {
                    throw new IllegalArgumentException("未知的消息请求信息类型,messageType:" + msgType + ",请求数据信息:" + objectNode.toString());
                }
                return unKnowWeiXinMessageListener.handlerOtherType(objectNode);
        }
    }


    /**
     * 处理微信信息
     *
     * @param abrstractWeiXinMessage         微信消息
     * @param weiXinMessageTypeEnum 消息类型枚举
     * @return 处理完毕的xml字符串
     */
    private String handlerMessage(AbrstractWeiXinMessage abrstractWeiXinMessage, WeiXinMessageTypeEnum weiXinMessageTypeEnum) {
        if (CollectionUtils.isEmpty(abrstractWeiXinMessageListeners)) {
            throw new IllegalArgumentException("未注册任何相关消息监听器，或监听器未加入到ioc容器中");
        }
        final AbrstractWeiXinMessageListener abrstractWeiXinMessageListener = messageListenerMap.get(weiXinMessageTypeEnum);

        if (Objects.isNull(abrstractWeiXinMessageListener)) {
            throw new IllegalArgumentException(weiXinMessageTypeEnum.name() + "消息监听器未注册");
        }

        LoggerUtils.debug(logger, "接收到微信请求，请求类型:{},请求参数:{}", weiXinMessageTypeEnum.getDescription(), JSON.toJSONString(abrstractWeiXinMessage));
        WeiXinMessageResponse response = abrstractWeiXinMessageListener.handler(abrstractWeiXinMessage);
        if (Objects.isNull(response)) {
            return "";
        }
        String toUserName = abrstractWeiXinMessage.getToUserName();
        String fromUserName = abrstractWeiXinMessage.getFromUserName();
        response.setFromUserName(toUserName);
        response.setCreateTime(System.currentTimeMillis() / 1000);
        response.setToUserName(fromUserName);
        try {
            String res = xmlMapper.writeValueAsString(response);
            LoggerUtils.debug(logger, "返回微信应答信息，参数:{}", res);
            return res;
        } catch (JsonProcessingException e) {
            LoggerUtils.error(logger,"jackson 转xml失败，输入参数:"+JSON.toJSONString(response),e);
            return "";
        }
    }

    /**
     * 处理微信事件
     *
     * @param abrstractWeiXinMessage 微信事件信息
     * @param weiXinEventTypeEnum 微信事件枚举
     * @return 处理结果
     */
    private String handlerEvent(AbrstractWeiXinMessage abrstractWeiXinMessage, WeiXinEventTypeEnum weiXinEventTypeEnum) {
        if (CollectionUtils.isEmpty(abrstractWeiXinEventListeners)) {
            throw new IllegalArgumentException("未注册相关事件监听器，或监听器未加入到ioc容器中");
        }
        final AbrstractWeiXinEventListener abrstractWeiXinEventListener = eventListenerMap.get(weiXinEventTypeEnum);

        if (Objects.isNull(abrstractWeiXinEventListener)) {
            throw new IllegalArgumentException(weiXinEventTypeEnum.name() + "事件监听器未注册");
        }
        WeiXinMessageResponse response = abrstractWeiXinEventListener.handler(abrstractWeiXinMessage);
        if (Objects.isNull(response)) {
            return "";
        }
        String toUserName = abrstractWeiXinMessage.getToUserName();
        String fromUserName = abrstractWeiXinMessage.getFromUserName();
        response.setFromUserName(toUserName);
        response.setCreateTime(System.currentTimeMillis() / 1000);
        response.setToUserName(fromUserName);
        try {
            return xmlMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            LoggerUtils.error(logger,"jackson 转xml失败，输入参数:"+JSON.toJSONString(response),e);
            return "";
        }
    }
}
