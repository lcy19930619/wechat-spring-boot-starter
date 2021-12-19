package net.jlxxw.wechat.enums;

/**
 * 微信事件枚举
 *
 * @author chunyang.leng
 * @date 2021/1/20 1:03 下午
 */
public enum WeiXinEventTypeEnum {

    /**
     * 事件类型：subscribe(订阅)
     */
    SUBSCRIBE("subscribe", "用户关注"),

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    UNSUBSCRIBE("unsubscribe", "用户取消关注"),

    /**
     * 用户未关注时，进行关注后的事件推送
     */
    SUBSCRIBE_QRSCENE("subscribe", "用户未关注时，进行关注后的事件推送"),

    /**
     * 事件类型：scan(用户已关注时的扫描带参数二维码)
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E6%89%AB%E6%8F%8F%E5%B8%A6%E5%8F%82%E6%95%B0%E4%BA%8C%E7%BB%B4%E7%A0%81%E4%BA%8B%E4%BB%B6">文档地址</a>
     */
    SCAN("SCAN", "用户已关注时的扫描带参数二维码"),

    /**
     * 事件类型：LOCATION(上报地理位置)
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E4%B8%8A%E6%8A%A5%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE%E4%BA%8B%E4%BB%B6">文档地址</a>
     */
    LOCATION("LOCATION", "上报地理位置"),

    /**
     * 事件类型 TEMPLATESENDJOBFINISH(模板送达结果)
     */
    TEMPLATESENDJOBFINISH("TEMPLATESENDJOBFINISH", "模板送达结果"),

    // -------------------------------------------菜单类事件开始-------------------------------------------

    /**
     * 事件类型：CLICK(自定义菜单)
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#0">文档地址</a>
     */
    CLICK("CLICK", "点击菜单拉取消息时的事件推送"),

    /**
     * 点击菜单跳转链接时的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#1">文档地址</a>
     */
    VIEW("VIEW", "点击菜单跳转链接时的事件推送"),

    /**
     * 扫码推事件的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#2">文档地址</a>
     */
    SCANCODE_PUSH("scancode_push", "菜单扫码推事件的事件推送"),

    /**
     * 扫码推事件且弹出“消息接收中”提示框的事件推送
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#3">文档地址</a>
     */
    SCANCODE_WAITMSG("scancode_waitmsg", "扫码推事件且弹出“消息接收中”提示框的事件推送"),

    /**
     * 弹出系统拍照发图的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#4">文档地址</a>
     */
    PIC_SYSPHOTO("pic_sysphoto", "弹出系统拍照发图的事件推送"),

    /**
     * 弹出拍照或者相册发图的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#5">文档地址</a>
     */
    PIC_PHOTO_OR_ALBUM("pic_photo_or_album", "弹出拍照或者相册发图的事件推送"),

    /**
     * 弹出微信相册发图器的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#6">文档地址</a>
     */
    PIC_WEIXIN("pic_weixin", "弹出微信相册发图器的事件推送"),

    /**
     * 弹出地理位置选择器的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#7">文档地址</a>
     */
    LOCATION_SELECT("location_select", "弹出地理位置选择器的事件推送"),

    /**
     * 点击菜单跳转小程序的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#8">文档地址</a>
     */
    VIEW_MINIPROGRAM("view_miniprogram", "点击菜单跳转小程序的事件推送"),

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

    WeiXinEventTypeEnum(String eventCode, String description) {
        this.eventCode = eventCode;
        this.description = description;
    }

    public String getEventCode() {
        return eventCode;
    }

    public String getDescription() {
        return description;
    }
}
