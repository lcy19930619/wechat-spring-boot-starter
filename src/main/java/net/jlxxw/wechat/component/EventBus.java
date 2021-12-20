package net.jlxxw.wechat.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.jlxxw.wechat.component.listener.AbstractWeChatEventListener;
import net.jlxxw.wechat.component.listener.AbstractWeChatMessageListener;
import net.jlxxw.wechat.component.listener.UnKnowWeChatEventListener;
import net.jlxxw.wechat.component.listener.UnKnowWeChatMessageListener;
import net.jlxxw.wechat.dto.message.*;
import net.jlxxw.wechat.dto.message.event.*;
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


    @PostConstruct
    public void postConstruct() {
        // 初始化xmlMapper相关配置
        XML_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        XML_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        XML_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        // 初始化objectMapper相关配置
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        if (!CollectionUtils.isEmpty(abstractWeChatMessageListeners)) {
            // 如果已经配置了微信监听器，则按照支持到类型枚举，进行分类
            Map<WeChatMessageTypeEnum, List<AbstractWeChatMessageListener>> map = abstractWeChatMessageListeners.stream().collect(Collectors.groupingBy(AbstractWeChatMessageListener::supportMessageType));

            map.forEach((k, v) -> {
                if (v.size() > 1) {
                    // 因为每个消息都需要由一个返回值，如果配置多个监听器，则无法知道哪个返回值可用，故，限制只能有一个监听器
                    throw new BeanCreationException("微信messageListener不能注册多次");
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
                    throw new BeanCreationException("微信eventListener不能注册多次");
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
        switch (msgType) {
            case "text":
                // 文本信息, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E6%96%87%E6%9C%AC%E6%B6%88%E6%81%AF
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), TextMessage.class);
                return handlerMessage(abstractWeChatMessage, WeChatMessageTypeEnum.TEXT);

            case "image":
                // 图片信息, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), ImageMessage.class);
                return handlerMessage(abstractWeChatMessage, WeChatMessageTypeEnum.IMAGE);

            case "voice":
                // 音频信息, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E8%AF%AD%E9%9F%B3%E6%B6%88%E6%81%AF
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), VoiceMessage.class);
                return handlerMessage(abstractWeChatMessage, WeChatMessageTypeEnum.VOICE);

            case "video":
                // 视频信息, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), VideoMessage.class);
                return handlerMessage(abstractWeChatMessage, WeChatMessageTypeEnum.VIDEO);

            case "shortvideo":
                // 短视频信息, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%B0%8F%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), ShortVideoMessage.class);
                return handlerMessage(abstractWeChatMessage, WeChatMessageTypeEnum.SHORT_VIDEO);

            case "location":
                // 地理位置信息, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE%E6%B6%88%E6%81%AF
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), LocationMessage.class);
                return handlerMessage(abstractWeChatMessage, WeChatMessageTypeEnum.LOCATION);

            case "link":
                // 链接信息, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E9%93%BE%E6%8E%A5%E6%B6%88%E6%81%AF
                abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), LinkMessage.class);
                return handlerMessage(abstractWeChatMessage, WeChatMessageTypeEnum.LINK);

            case "event":
                // 事件类型的请求内容
                String event = objectNode.get("Event").textValue();

                switch (event) {
                    // 订阅事件, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E5%85%B3%E6%B3%A8-%E5%8F%96%E6%B6%88%E5%85%B3%E6%B3%A8%E4%BA%8B%E4%BB%B6
                    case "subscribe":
                        if (Objects.isNull(objectNode.get("EventKey"))) {
                            abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeEventMessage.class);
                            return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.SUBSCRIBE);
                        }
                        String eventKey = objectNode.get("EventKey").textValue();
                        if (eventKey != null && eventKey.contains("qrscene_")) {
                            // 用户未关注时，进行关注后的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E6%89%AB%E6%8F%8F%E5%B8%A6%E5%8F%82%E6%95%B0%E4%BA%8C%E7%BB%B4%E7%A0%81%E4%BA%8B%E4%BB%B6
                            abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeQrsceneEventMessage.class);
                            return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.SUBSCRIBE_QRSCENE);
                        }
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.SUBSCRIBE);

                    // 取消订阅, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E5%85%B3%E6%B3%A8-%E5%8F%96%E6%B6%88%E5%85%B3%E6%B3%A8%E4%BA%8B%E4%BB%B6
                    case "unsubscribe":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), UnSubscribeEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.UNSUBSCRIBE);

                    // 扫描二维码事件, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E6%89%AB%E6%8F%8F%E5%B8%A6%E5%8F%82%E6%95%B0%E4%BA%8C%E7%BB%B4%E7%A0%81%E4%BA%8B%E4%BB%B6
                    case "SCAN":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), SubscribeScanEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.SCAN);

                    // 地理位置信息上报事件, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E4%B8%8A%E6%8A%A5%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE%E4%BA%8B%E4%BB%B6
                    case "LOCATION":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), LocationEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.LOCATION);

                    // 模版推送回调事件, https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html#%E4%BA%8B%E4%BB%B6%E6%8E%A8%E9%80%81
                    case "TEMPLATESENDJOBFINISH":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), TemplateEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.TEMPLATESENDJOBFINISH);

                    // ---------------------菜单类事件 开始---------------------

                    // 点击菜单拉取消息时的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#0
                    case "CLICK":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), ClickMenuGetInfoEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.CLICK);

                    // 点击菜单跳转链接时的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#1
                    case "VIEW":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), ClickMenuGotoLinkEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);


                    // 扫码推事件的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#2
                    case "scancode_push":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), ScancodePushEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);

                    // 扫码推事件且弹出“消息接收中”提示框的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#3
                    case "scancode_waitmsg":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), ScancodeWaitmsgEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);

                    // 弹出系统拍照发图的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#4
                    case "pic_sysphoto":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), PicSysphotoEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);

                    // 弹出拍照或者相册发图的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#5
                    case "pic_photo_or_album":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), PicPhotoOrAlbumEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);

                    // 弹出微信相册发图器的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#6
                    case "pic_weixin":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), PicWeChatEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);

                    // 弹出地理位置选择器的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#7
                    case "location_select":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), LocationSelectEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);

                    // 点击菜单跳转小程序的事件推送, https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#8
                    case "view_miniprogram":
                        abstractWeChatMessage = OBJECT_MAPPER.readValue(objectNode.toString(), ViewMiniProgramEventMessage.class);
                        return handlerEvent(abstractWeChatMessage, WeChatEventTypeEnum.VIEW);
                    // ---------------------菜单类事件 结束---------------------


                    // 未知的事件，用户可自行扩展
                    default:
                        if (Objects.isNull(unKnowWeChatEventListener)) {
                            throw new IllegalArgumentException("未知的事件请求信息类型，event:" + event + ",请求数据信息:" + objectNode.toString());
                        }
                        return unKnowWeChatEventListener.handlerOtherType(objectNode);
                }

                // 未知的消息，用户可自行扩展
            default:
                if (Objects.isNull(unKnowWeChatMessageListener)) {
                    throw new IllegalArgumentException("未知的消息请求信息类型,messageType:" + msgType + ",请求数据信息:" + objectNode.toString());
                }
                return unKnowWeChatMessageListener.handlerOtherType(objectNode);
        }
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
            throw new IllegalArgumentException("未注册任何相关消息监听器，或监听器未加入到ioc容器中");
        }
        final AbstractWeChatMessageListener abstractWeChatMessageListener = messageListenerMap.get(weChatMessageTypeEnum);

        if (Objects.isNull(abstractWeChatMessageListener)) {
            throw new IllegalArgumentException(weChatMessageTypeEnum.name() + "消息监听器未注册");
        }

        LoggerUtils.debug(logger, "接收到微信请求，请求类型:{},请求参数:{}", weChatMessageTypeEnum.getDescription(), JSON.toJSONString(abstractWeChatMessage));
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
            throw new IllegalArgumentException("未注册相关事件监听器，或监听器未加入到ioc容器中");
        }
        final AbstractWeChatEventListener abstractWeChatEventListener = eventListenerMap.get(weChatEventTypeEnum);

        if (Objects.isNull(abstractWeChatEventListener)) {
            throw new IllegalArgumentException(weChatEventTypeEnum.name() + "事件监听器未注册");
        }
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
