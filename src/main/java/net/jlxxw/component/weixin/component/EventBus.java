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
import net.jlxxw.component.weixin.component.listener.UnKnowWeiXinEventListener;
import net.jlxxw.component.weixin.component.listener.UnKnowWeiXinMessageListener;
import net.jlxxw.component.weixin.component.listener.WeiXinEventListener;
import net.jlxxw.component.weixin.component.listener.WeiXinMessageListener;
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
import javax.xml.parsers.SAXParserFactory;
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
    private List<WeiXinMessageListener> weiXinMessageListeners;
    @Autowired(required = false)
    private List<WeiXinEventListener> weiXinEventListeners;
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
    private final SAXParserFactory factory = new org.apache.xerces.jaxp.SAXParserFactoryImpl();
    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 消息处理监听器
     * key 支持的消息类型
     * value 消息监听器
     */
    private final Map<WeiXinMessageTypeEnum, WeiXinMessageListener> messageListenerMap = new HashMap<>();
    /**
     * 消息处理监听器
     * key 支持的消息类型
     * value 消息监听器
     */
    private final Map<WeiXinEventTypeEnum, WeiXinEventListener> eventListenerMap = new HashMap<>();


    @PostConstruct
    public void postConstruct() {
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        factory.setValidating(false);
        factory.setNamespaceAware(false);

        if (!CollectionUtils.isEmpty(weiXinMessageListeners)) {
            Map<WeiXinMessageTypeEnum, List<WeiXinMessageListener>> map = weiXinMessageListeners.stream().collect(Collectors.groupingBy(WeiXinMessageListener::supportMessageType));

            map.forEach((k, v) -> {
                if (v.size() > 1) {
                    throw new BeanCreationException("微信messageListener不能注册多次");
                }
                messageListenerMap.put(k, v.get(0));
            });
        }


        final WeiXinMessageTypeEnum[] values = WeiXinMessageTypeEnum.values();
        for (WeiXinMessageTypeEnum value : values) {
            if (!messageListenerMap.containsKey(value)) {
                logger.warn(value.getDescription() + "消息处理器未注册!!!");
            }
        }

        if (!CollectionUtils.isEmpty(weiXinEventListeners)) {
            Map<WeiXinEventTypeEnum, List<WeiXinEventListener>> eventMap = weiXinEventListeners.stream().collect(Collectors.groupingBy(WeiXinEventListener::supportEventType));
            eventMap.forEach((k, v) -> {
                if (v.size() > 1) {
                    throw new BeanCreationException("微信eventListener不能注册多次");
                }
                eventListenerMap.put(k, v.get(0));
            });
        }

        final WeiXinEventTypeEnum[] eventValues = WeiXinEventTypeEnum.values();
        for (WeiXinEventTypeEnum value : eventValues) {
            if (!eventListenerMap.containsKey(value)) {
                logger.warn(value.getDescription() + "事件处理器未注册!!!");
            }
        }
    }

    /**
     * 微信请求处理结果
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
     * 处理微信加解密分发
     */
    public String dispatcher(byte[] bytes, String uri) throws AesException {
        if (weiXinProperties.isEnableMessageEnc()) {
            // 微信发送进来的xml
            String inputXML = new String(bytes, StandardCharsets.UTF_8);

            int index = uri.indexOf("?");
            String str = uri.substring(index + 1);
            String[] split = str.split("&");
            Map<String, String> map = new HashMap<>(16);
            for (String s : split) {
                String[] arr = s.split("=");
                map.put(arr[0], arr[1]);
            }
            String msgSignature = map.get("msg_signature");
            String timestamp = map.get("timestamp");
            String nonce = map.get("nonce");
            String decryptMsg = weiXinMsgCodec.decryptMsg(msgSignature, timestamp, nonce, inputXML);
            logger.debug("微信消息解密成功，信息为:{}", decryptMsg);
            bytes = decryptMsg.getBytes(StandardCharsets.UTF_8);
        }
        String result = dispatcher(bytes);
        if (weiXinProperties.isEnableMessageEnc()) {
            result = weiXinMsgCodec.encrypt(result);
            logger.debug("微信消息加密成功，信息为:{}", result);
        }
        return result;
    }

    /**
     * 微信请求处理结果
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

    private String handlerWeiXinMessage(Reader reader) throws IOException {
        ObjectNode objectNode = xmlMapper.readValue(reader, ObjectNode.class);
        final String msgType = objectNode.get("MsgType").textValue();

        WeiXinMessage weiXinMessage;
        switch (msgType) {
            case "text":
                weiXinMessage = objectMapper.readValue(objectNode.toString(), TextMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.TEXT);
            case "image":
                weiXinMessage = objectMapper.readValue(objectNode.toString(), ImageMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.IMAGE);
            case "voice":
                weiXinMessage = objectMapper.readValue(objectNode.toString(), VoiceMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.VOICE);
            case "video":
                weiXinMessage = objectMapper.readValue(objectNode.toString(), VideoMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.VIDEO);
            case "shortvideo":
                weiXinMessage = objectMapper.readValue(objectNode.toString(), ShortVideoMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.SHORT_VIDEO);
            case "location":
                weiXinMessage = objectMapper.readValue(objectNode.toString(), LocationMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.LOCATION);
            case "link":
                weiXinMessage = objectMapper.readValue(objectNode.toString(), LinkMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.LINK);
            case "event":
                String event = objectNode.get("Event").textValue();

                switch (event) {
                    case "subscribe":
                        if (Objects.isNull(objectNode.get("EventKey"))) {
                            weiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeEventMessage.class);
                            return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE);
                        }
                        String eventKey = objectNode.get("EventKey").textValue();
                        if (eventKey != null && eventKey.contains("qrscene_")) {
                            // 用户未关注时，进行关注后的事件推送
                            weiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeQrsceneEventMessage.class);
                            return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE_QRSCENE);
                        }
                        weiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE);
                    case "unsubscribe":
                        // 取消订阅
                        weiXinMessage = objectMapper.readValue(objectNode.toString(), UnSubscribeEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.UNSUBSCRIBE);
                    case "SCAN":
                        weiXinMessage = objectMapper.readValue(objectNode.toString(), SubscribeScanEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.SCAN);
                    case "LOCATION":
                        weiXinMessage = objectMapper.readValue(objectNode.toString(), LocationEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.LOCATION);

                    case "CLICK":
                        // 点击菜单拉取消息时的事件推送
                        weiXinMessage = objectMapper.readValue(objectNode.toString(), ClickMenuGetInfoEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.CLICK);
                    case "TEMPLATESENDJOBFINISH":
                        weiXinMessage = objectMapper.readValue(objectNode.toString(), TemplateEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.TEMPLATESENDJOBFINISH);

                    case "VIEW":
                        // 点击菜单跳转链接时的事件推送
                        weiXinMessage = objectMapper.readValue(objectNode.toString(), ClickMenuGotoLinkEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.VIEW);

                    default:
                        if(Objects.isNull(unKnowWeiXinEventListener)){
                            throw new IllegalArgumentException("未知的事件请求信息类型，event:"+event);
                        }
                        return unKnowWeiXinEventListener.handlerOtherType(objectNode);
                }
            default:
                if(Objects.isNull(unKnowWeiXinMessageListener)){
                    throw new IllegalArgumentException("未知的消息请求信息类型,messageType:"+msgType);
                }
                return  unKnowWeiXinMessageListener.handlerOtherType(objectNode);
        }
    }


    /**
     * 处理常规消息
     *
     * @param weiXinMessage         微信消息
     * @param weiXinMessageTypeEnum 消息类型枚举
     * @return 处理完毕的xml字符串
     */
    private String handlerMessage(WeiXinMessage weiXinMessage, WeiXinMessageTypeEnum weiXinMessageTypeEnum) {
        if (CollectionUtils.isEmpty(weiXinMessageListeners)) {
            throw new IllegalArgumentException("未注册相关消息监听器");
        }
        final WeiXinMessageListener weiXinMessageListener = messageListenerMap.get(weiXinMessageTypeEnum);

        if (Objects.isNull(weiXinMessageListener)) {
            throw new IllegalArgumentException(weiXinMessageTypeEnum.name() + "消息监听器未注册");
        }

        LoggerUtils.debug(logger, "接收到微信请求，请求类型:{},请求参数:{}", weiXinMessageTypeEnum.getDescription(), JSON.toJSONString(weiXinMessage));
        WeiXinMessageResponse response = weiXinMessageListener.handler(weiXinMessage);
        if (Objects.isNull(response)) {
            return "";
        }
        String toUserName = weiXinMessage.getToUserName();
        String fromUserName = weiXinMessage.getFromUserName();
        response.setFromUserName(toUserName);
        response.setCreateTime(System.currentTimeMillis() / 1000);
        response.setToUserName(fromUserName);
        try {
            String res = xmlMapper.writeValueAsString(response);
            LoggerUtils.debug(logger, "返回微信应答信息，参数:{}", res);
            return res;
        } catch (JsonProcessingException e) {
            logger.info("jackson bean to xml failed,input param:{}", JSON.toJSONString(response), e);
            return "";
        }
    }

    /**
     * 处理微信事件
     *
     * @param weiXinMessage
     * @param weiXinEventTypeEnum
     * @return
     */
    private String handlerEvent(WeiXinMessage weiXinMessage, WeiXinEventTypeEnum weiXinEventTypeEnum) {
        if (CollectionUtils.isEmpty(weiXinEventListeners)) {
            throw new IllegalArgumentException("未注册相关事件监听器");
        }
        final WeiXinEventListener weiXinEventListener = eventListenerMap.get(weiXinEventTypeEnum);

        if (Objects.isNull(weiXinEventListener)) {
            throw new IllegalArgumentException(weiXinEventTypeEnum.name() + "事件监听器未注册");
        }
        WeiXinMessageResponse response = weiXinEventListener.handler(weiXinMessage);
        if (Objects.isNull(response)) {
            return "";
        }
        String toUserName = weiXinMessage.getToUserName();
        String fromUserName = weiXinMessage.getFromUserName();
        response.setFromUserName(toUserName);
        response.setCreateTime(System.currentTimeMillis() / 1000);
        response.setToUserName(fromUserName);
        try {
            return xmlMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            logger.info("jackson bean to xml failed,input param:{}", JSON.toJSONString(response), e);
            return "";
        }
    }
}
