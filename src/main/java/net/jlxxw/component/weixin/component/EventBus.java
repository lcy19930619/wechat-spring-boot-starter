package net.jlxxw.component.weixin.component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.SAXParserFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.jlxxw.component.weixin.component.listener.WeiXinEventListener;
import net.jlxxw.component.weixin.component.listener.WeiXinMessageListener;
import net.jlxxw.component.weixin.dto.message.ImageMessage;
import net.jlxxw.component.weixin.dto.message.LinkMessage;
import net.jlxxw.component.weixin.dto.message.LocationMessage;
import net.jlxxw.component.weixin.dto.message.ShortVideoMessage;
import net.jlxxw.component.weixin.dto.message.TextMessage;
import net.jlxxw.component.weixin.dto.message.VideoMessage;
import net.jlxxw.component.weixin.dto.message.VoiceMessage;
import net.jlxxw.component.weixin.dto.message.WeiXinMessage;
import net.jlxxw.component.weixin.dto.message.event.ClickMenuGetInfoEventMessage;
import net.jlxxw.component.weixin.dto.message.event.ClickMenuGotoLinkEventMessage;
import net.jlxxw.component.weixin.dto.message.event.LocationEventMessage;
import net.jlxxw.component.weixin.dto.message.event.SubscribeEventMessage;
import net.jlxxw.component.weixin.dto.message.event.SubscribeQrsceneEventMessage;
import net.jlxxw.component.weixin.dto.message.event.SubscribeScanEventMessage;
import net.jlxxw.component.weixin.dto.message.event.TemplateEventMessage;
import net.jlxxw.component.weixin.dto.message.event.UnSubscribeEventMessage;
import net.jlxxw.component.weixin.enums.WeiXinEventTypeEnum;
import net.jlxxw.component.weixin.enums.WeiXinMessageTypeEnum;
import net.jlxxw.component.weixin.response.WeiXinMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    @Autowired
    private ThreadPoolTaskExecutor eventBusThreadPool;
    private SAXParserFactory factory = new org.apache.xerces.jaxp.SAXParserFactoryImpl();
    private XmlMapper xmlMapper;
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
        xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

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
			Reader reader =new InputStreamReader(inputStream);
            inputStream.close();
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
     * 微信请求处理结果
     */
    public String dispatcher(byte[] bytes) {
        final Future<String> future = eventBusThreadPool.submit(() -> {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			Reader reader = new InputStreamReader(inputStream);
			inputStream.close();
            return handlerWeiXinMessage(reader);
        });
        try {
            return future.get(5, TimeUnit.SECONDS);
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
                weiXinMessage = xmlMapper.readValue(reader,TextMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.TEXT);
            case "image":
				weiXinMessage = xmlMapper.readValue(reader,ImageMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.IMAGE);
            case "voice":
				weiXinMessage = xmlMapper.readValue(reader,VoiceMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.VOICE);
            case "video":
				weiXinMessage = xmlMapper.readValue(reader,VideoMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.VIDEO);
            case "shortvideo":
				weiXinMessage = xmlMapper.readValue(reader,ShortVideoMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.SHORT_VIDEO);
            case "location":
				weiXinMessage = xmlMapper.readValue(reader,LocationMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.LOCATION);
            case "link":
				weiXinMessage = xmlMapper.readValue(reader,LinkMessage.class);
                return handlerMessage(weiXinMessage, WeiXinMessageTypeEnum.LINK);
            case "event":
                String event = objectNode.get("Event").textValue();

                switch (event) {
                    // todo
                    case "subscribe":
                        String eventKey = objectNode.get("EventKey").textValue();
                        if (eventKey != null && eventKey.contains("qrscene_")) {
                            // 用户未关注时，进行关注后的事件推送
							weiXinMessage = xmlMapper.readValue(reader,SubscribeQrsceneEventMessage.class);
                            return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE_QRSCENE);
                        }
						weiXinMessage = xmlMapper.readValue(reader,SubscribeEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.SUBSCRIBE);
                    case "unsubscribe":
                        // 取消订阅
						weiXinMessage = xmlMapper.readValue(reader,UnSubscribeEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.UNSUBSCRIBE);
                    case "SCAN":
						weiXinMessage = xmlMapper.readValue(reader,SubscribeScanEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.SCAN);
                    case "LOCATION":
						weiXinMessage = xmlMapper.readValue(reader,LocationEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.LOCATION);

                    case "CLICK":
                        // 点击菜单拉取消息时的事件推送
						weiXinMessage = xmlMapper.readValue(reader,ClickMenuGetInfoEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.CLICK);
                    case "TEMPLATESENDJOBFINISH":
						weiXinMessage = xmlMapper.readValue(reader,TemplateEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.TEMPLATESENDJOBFINISH);

                    case "VIEW":
                        // 点击菜单跳转链接时的事件推送
						weiXinMessage = xmlMapper.readValue(reader,ClickMenuGotoLinkEventMessage.class);
                        return handlerEvent(weiXinMessage, WeiXinEventTypeEnum.VIEW);

                    default:
                        throw new IllegalArgumentException("未知的请求信息类型");
                }
            default:
                throw new IllegalArgumentException("未知的请求信息类型");
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
            return xmlMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            logger.info("jackson bean to xml failed,input param:{}",JSON.toJSONString(response),e);
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
            logger.info("jackson bean to xml failed,input param:{}",JSON.toJSONString(response),e);
            return "";
        }
    }
}
