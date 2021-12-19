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
     */
    SCAN("SCAN", "用户扫描"),

    /**
     * 事件类型：LOCATION(上报地理位置)
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
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#0
     */
    CLICK("CLICK", "自定义菜单"),

    /**
     * 点击菜单跳转链接时的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#1
     */
    VIEW("VIEW", "点击菜单跳转链接时的事件推送"),

    /**
     * 扫码推事件的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#2
     */
    SCANCODE_PUSH("scancode_push", "菜单扫码推事件的事件推送"),

    /**
     * 扫码推事件且弹出“消息接收中”提示框的事件推送
     */
    SCANCODE_WAITMSG("scancode_waitmsg", "扫码推事件且弹出“消息接收中”提示框的事件推送"),

    /**
     * 弹出系统拍照发图的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#4
     */
    PIC_SYSPHOTO("pic_sysphoto", "弹出系统拍照发图的事件推送"),

    /**
     * 弹出拍照或者相册发图的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#5
     */
    PIC_PHOTO_OR_ALBUM("pic_photo_or_album", "弹出拍照或者相册发图的事件推送"),

    /**
     * 弹出微信相册发图器的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#6
     */
    PIC_WEIXIN("pic_weixin", "弹出微信相册发图器的事件推送"),

    /**
     * 弹出地理位置选择器的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#7
     */
    LOCATION_SELECT("location_select", "弹出地理位置选择器的事件推送"),

    /**
     * 点击菜单跳转小程序的事件推送
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Custom_Menu_Push_Events.html#8
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
