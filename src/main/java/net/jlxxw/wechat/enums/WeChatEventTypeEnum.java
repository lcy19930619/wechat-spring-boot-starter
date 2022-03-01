package net.jlxxw.wechat.enums;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;
import net.jlxxw.wechat.dto.message.event.*;

/**
 * 微信事件枚举
 *
 * @author chunyang.leng
 * @date 2021/1/20 1:03 下午
 */
public enum WeChatEventTypeEnum {

    /**
     * 事件类型：subscribe(订阅)
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E5%85%B3%E6%B3%A8-%E5%8F%96%E6%B6%88%E5%85%B3%E6%B3%A8%E4%BA%8B%E4%BB%B6">文档地址</a>
     */
    SUBSCRIBE("subscribe", "用户关注", SubscribeEventMessage.class),

    /**
     * 用户未关注时，进行关注后的事件推送
     * 扫描二维码关注事件
     * <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E6%89%AB%E6%8F%8F%E5%B8%A6%E5%8F%82%E6%95%B0%E4%BA%8C%E7%BB%B4%E7%A0%81%E4%BA%8B%E4%BB%B6">文档地址</a>
     */
    QRSCENE_SUBSCRIBE("qrscene_subscribe", "用户未关注时，进行关注后的事件推送", SubscribeQrsceneEventMessage.class),

    /**
     * 事件类型：unsubscribe(取消订阅)
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E5%85%B3%E6%B3%A8-%E5%8F%96%E6%B6%88%E5%85%B3%E6%B3%A8%E4%BA%8B%E4%BB%B6">文档地址</a>
     */
    UNSUBSCRIBE("unsubscribe", "用户取消关注", UnSubscribeEventMessage.class),

    /**
     * 事件类型：scan(用户已关注时的扫描带参数二维码)
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E6%89%AB%E6%8F%8F%E5%B8%A6%E5%8F%82%E6%95%B0%E4%BA%8C%E7%BB%B4%E7%A0%81%E4%BA%8B%E4%BB%B6">文档地址</a>
     */
    SCAN("SCAN", "用户已关注时的扫描带参数二维码", SubscribeScanEventMessage.class),

    /**
     * 事件类型：LOCATION(上报地理位置)
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E4%B8%8A%E6%8A%A5%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE%E4%BA%8B%E4%BB%B6">文档地址</a>
     */
    LOCATION("LOCATION", "上报地理位置", LocationEventMessage.class),

    /**
     * 事件类型 TEMPLATESENDJOBFINISH(模板送达结果)
     */
    TEMPLATESENDJOBFINISH("TEMPLATESENDJOBFINISH", "模板送达结果", TemplateEventMessage.class),

    // -------------------------------------------菜单类事件开始-------------------------------------------

    /**
     * 事件类型：CLICK(自定义菜单)
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#0">文档地址</a>
     */
    CLICK("CLICK", "点击菜单拉取消息时的事件推送", ClickMenuGetInfoEventMessage.class),

    /**
     * 点击菜单跳转链接时的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#1">文档地址</a>
     */
    VIEW("VIEW", "点击菜单跳转链接时的事件推送", ClickMenuGotoLinkEventMessage.class),

    /**
     * 扫码推事件的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#2">文档地址</a>
     */
    SCANCODE_PUSH("scancode_push", "菜单扫码推事件的事件推送", ScancodePushEventMessage.class),

    /**
     * 扫码推事件且弹出“消息接收中”提示框的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#3">文档地址</a>
     */
    SCANCODE_WAITMSG("scancode_waitmsg", "扫码推事件且弹出“消息接收中”提示框的事件推送", ScancodeWaitmsgEventMessage.class),

    /**
     * 弹出系统拍照发图的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#4">文档地址</a>
     */
    PIC_SYSPHOTO("pic_sysphoto", "弹出系统拍照发图的事件推送", PicSysphotoEventMessage.class),

    /**
     * 弹出拍照或者相册发图的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#5">文档地址</a>
     */
    PIC_PHOTO_OR_ALBUM("pic_photo_or_album", "弹出拍照或者相册发图的事件推送", PicPhotoOrAlbumEventMessage.class),

    /**
     * 弹出微信相册发图器的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#6">文档地址</a>
     */
    PIC_WEIXIN("pic_weixin", "弹出微信相册发图器的事件推送", PicWeChatEventMessage.class),

    /**
     * 弹出地理位置选择器的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#7">文档地址</a>
     */
    LOCATION_SELECT("location_select", "弹出地理位置选择器的事件推送", LocationSelectEventMessage.class),

    /**
     * 点击菜单跳转小程序的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#8">文档地址</a>
     */
    VIEW_MINIPROGRAM("view_miniprogram", "点击菜单跳转小程序的事件推送", ViewMiniProgramEventMessage.class),

    // -------------------------------------------菜单类事件结束-------------------------------------------

    ;

    /**
     * 事件event
     */
    private final String eventCode;
    /**
     * 事件描述
     */
    private final String description;

    /**
     * 待转换的事件类型枚举
     */
    private final Class<? extends AbstractWeChatMessage> coverEventClass;

    WeChatEventTypeEnum(String eventCode, String description, Class<? extends AbstractWeChatMessage> coverEventClass) {
        this.eventCode = eventCode;
        this.description = description;
        this.coverEventClass = coverEventClass;
    }

    public Class<? extends AbstractWeChatMessage> getCoverEventClass() {
        return coverEventClass;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getDescription() {
        return description;
    }
}
