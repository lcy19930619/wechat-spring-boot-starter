package net.jlxxw.wechat.event.component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.dto.message.event.SubscribeEventMessage;
import net.jlxxw.wechat.dto.message.event.SubscribeQrsceneEventMessage;
import net.jlxxw.wechat.enums.WeChatEventTypeEnum;
import net.jlxxw.wechat.enums.WeChatMessageTypeEnum;
import net.jlxxw.wechat.event.codec.WeChatMessageCodec;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.event.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.response.WeChatMessageResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 事件总线
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:35 上午
 */
public class EventBus {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);
    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
    private static final Map<String, WeChatMessageTypeEnum> MESSAGE_TYPE_ENUM_MAP = new HashMap<>(16);
    /**
     * 事件枚举和type映射集合
     * key WeChatEventTypeEnum.eventCode
     * value WeChatEventTypeEnum
     */
    private static final Map<String, WeChatEventTypeEnum> EVENT_TYPE_ENUM_MAP = new HashMap<>(16);

    static {
        WeChatMessageTypeEnum[] messageTypeEnums = WeChatMessageTypeEnum.values();
        for (WeChatMessageTypeEnum messageTypeEnum : messageTypeEnums) {
            MESSAGE_TYPE_ENUM_MAP.put(messageTypeEnum.getKey(), messageTypeEnum);
        }
        WeChatEventTypeEnum[] eventTypeEnums = WeChatEventTypeEnum.values();
        for (WeChatEventTypeEnum eventTypeEnum : eventTypeEnums) {
            EVENT_TYPE_ENUM_MAP.put(eventTypeEnum.getEventCode(), eventTypeEnum);
        }

    }

    private final Executor eventBusThreadPool;
    /**
     * 有可能未注册任何消息处理器
     */
    private final List<AbstractWeChatMessageListener> abstractWeChatMessageListeners;

    /**
     * 有可能未注册任何事件处理器
     */
    private final List<AbstractWeChatEventListener> abstractWeChatEventListeners;


    /**
     * 有可能未注册兜底策略
     */
    private final UnKnowWeChatEventListener unKnowWeChatEventListener;
    /**
     * 有可能未注册兜底策略
     */
    private final UnKnowWeChatMessageListener unKnowWeChatMessageListener;

    /**
     * 消息编解码器
     */
    private final WeChatMessageCodec weChatMessageCodec;

    /**
     * 构建事件总线
     *
     * @param eventBusThreadPool             事件总线线程池
     * @param abstractWeChatMessageListeners 微信消息监听器集合
     * @param abstractWeChatEventListeners   微信事件监听器集合
     * @param unKnowWeChatEventListener      未知类型事件处理器
     * @param unKnowWeChatMessageListener    未知类型消息处理器
     */
    public EventBus(Executor eventBusThreadPool,
                    List<AbstractWeChatMessageListener> abstractWeChatMessageListeners,
                    List<AbstractWeChatEventListener> abstractWeChatEventListeners,
                    UnKnowWeChatEventListener unKnowWeChatEventListener,
                    UnKnowWeChatMessageListener unKnowWeChatMessageListener,
                    WeChatMessageCodec weChatMessageCodec) {
        this.eventBusThreadPool = eventBusThreadPool;
        this.abstractWeChatMessageListeners = abstractWeChatMessageListeners;
        this.abstractWeChatEventListeners = abstractWeChatEventListeners;
        this.unKnowWeChatEventListener = unKnowWeChatEventListener;
        this.unKnowWeChatMessageListener = unKnowWeChatMessageListener;
        this.weChatMessageCodec = weChatMessageCodec;
        LoggerUtils.info(logger, "公众号组件 ---> 初始化事件总线");
        init();
        LoggerUtils.info(logger, "公众号组件 ---> 事件总线初始化完毕");
    }

    public void init() {
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
                    throw new BeanCreationException("公众号组件 ---> 微信 " + k.getDescription() + " messageListener不能注册多次");
                }
                messageListenerMap.put(k, v.get(0));
            });
        }

        // 警告信息打印，提示未注册的监听器
        final WeChatMessageTypeEnum[] values = WeChatMessageTypeEnum.values();
        for (WeChatMessageTypeEnum value : values) {
            if (!messageListenerMap.containsKey(value)) {
                LoggerUtils.warn(logger, "公众号组件 ---> " + value.getDescription() + "消息处理器未注册!!!");
            }
        }

        if (!CollectionUtils.isEmpty(abstractWeChatEventListeners)) {
            // 如果已经配置了微信监听器，则按照支持到类型枚举，进行分类
            Map<WeChatEventTypeEnum, List<AbstractWeChatEventListener>> eventMap = abstractWeChatEventListeners.stream().collect(Collectors.groupingBy(AbstractWeChatEventListener::supportEventType));
            eventMap.forEach((k, v) -> {
                if (v.size() > 1) {
                    // 因为每个事件都需要由一个返回值，如果配置多个监听器，则无法知道哪个返回值可用，故，限制只能有一个监听器
                    throw new BeanCreationException("公众号组件 ---> 微信 " + k.getDescription() + " eventListener不能注册多次");
                }
                eventListenerMap.put(k, v.get(0));
            });
        }

        // 警告信息打印，提示未注册的监听器
        final WeChatEventTypeEnum[] eventValues = WeChatEventTypeEnum.values();
        for (WeChatEventTypeEnum value : eventValues) {
            if (!eventListenerMap.containsKey(value)) {
                LoggerUtils.warn(logger, "公众号组件 ---> " + value.getDescription() + "事件处理器未注册!!!");
            }
        }
        if (unKnowWeChatEventListener == null) {
            LoggerUtils.warn(logger, "公众号组件 ---> 未发现未知事件处理器，可能存在事件回调丢失情况");
        }
        if (unKnowWeChatMessageListener == null) {
            LoggerUtils.warn(logger, "公众号组件 ---> 未发现未知消息处理器，可能存在消息回调丢失情况");
        }
    }



    /**
     * 不使用线程池，消费处理数据
     * @param bytes 数据
     * @param uri 请求 uri
     */
    public String dispatcher(byte[] bytes, String uri) {
        String result = "";
        try {
            byte[] data = bytes;
            // 微信发送进来的xml
            String inputXML = new String(data, StandardCharsets.UTF_8);
            // 消息解密
            String decryptMsg = weChatMessageCodec.decrypt(uri, inputXML);
            // 将解密后的数据，转换为byte数组，用于协议的具体处理
            data = decryptMsg.getBytes(StandardCharsets.UTF_8);
            // 调用具体的分发器，实现数据的处理
            result = dispatcher(data);
            // 如果启用了信息加解密功能，则对返回值进行加密处理
            return weChatMessageCodec.encrypt(result);
        }catch (Exception e) {
            logger.error("事件分发处理出现异常,返回兜底空白字符串,微信参数:" + new String(bytes, StandardCharsets.UTF_8) + " ,uri参数:" + uri + ",异常信息:", e);
            return "";
        }
    }


    /**
     * 消费处理数据，等待时间
     * @param bytes 数据
     * @param uri 请求 uri
     * @param responseConsumer 应答信息消费者
     */
    public void dispatcher(byte[] bytes, String uri, Consumer<String> responseConsumer) {
        eventBusThreadPool.execute(() -> {
            String result = "";
            try {
                byte[] data = bytes;
                // 微信发送进来的xml
                String inputXML = new String(data, StandardCharsets.UTF_8);
                // 消息解密
                String decryptMsg = weChatMessageCodec.decrypt(uri, inputXML);
                // 将解密后的数据，转换为byte数组，用于协议的具体处理
                data = decryptMsg.getBytes(StandardCharsets.UTF_8);
                // 调用具体的分发器，实现数据的处理
                result = dispatcher(data);
                // 如果启用了信息加解密功能，则对返回值进行加密处理
                result = weChatMessageCodec.encrypt(result);
            }catch (Exception e) {
                logger.error("事件分发处理出现异常,返回兜底空白字符串,微信参数:" + new String(bytes, StandardCharsets.UTF_8) + " ,uri参数:" + uri + ",异常信息:", e);
            }
            responseConsumer.accept(result);
        });

    }

    /**
     * 处理微信请求信息，无加密，netty使用
     */
    private String dispatcher(byte[] bytes) {
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
        if ("event".equals(msgType)) {
            // 如果是事件类型，则使用事件进行处理
            String event = objectNode.get("Event").textValue();
            WeChatEventTypeEnum weChatEventTypeEnum = EVENT_TYPE_ENUM_MAP.get(event);
            if (Objects.isNull(weChatEventTypeEnum)) {
                // 未注册的事件枚举，兜底处理
                if (Objects.isNull(unKnowWeChatEventListener)) {
                    // 未能使用兜底事件处理器，则直接丢出异常
                    throw new IllegalArgumentException("未知的事件请求信息类型，event:" + event + ",请求数据信息:" + objectNode);
                }
                // 未注册的枚举类型，则使用兜底的事件处理器
                return unKnowWeChatEventListener.handlerOtherType(objectNode);
            } else {
                if (event.equals(WeChatEventTypeEnum.SUBSCRIBE.getEventCode())) {
                    // 关注事件类型
                    // 将数据转换为事件支持的传输对象
                    if (Objects.isNull(objectNode.get("EventKey"))) {
                        // 普通的关注事件
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.SUBSCRIBE);
                    }

                    String eventKey = objectNode.get("EventKey").textValue();
                    if (eventKey != null && eventKey.contains("qrscene_") && WeChatEventTypeEnum.SUBSCRIBE.equals(weChatEventTypeEnum)) {
                        // 以qrscene_开头的关注事件，为扫码关注事件，扫码关注事件单独处理
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeQrsceneEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.QRSCENE_SUBSCRIBE);
                    } else {
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
        if (Objects.isNull(weChatMessageTypeEnum)) {
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
            throw new IllegalArgumentException(weChatEventTypeEnum.name() + "事件监听器未注册,当前微信请求参数:" + JSON.toJSONString(abstractWeChatMessage));
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
