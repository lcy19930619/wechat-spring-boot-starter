package net.jlxxw.component.weixin.enums;

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
    SUBSCRIBE("subscribe","用户关注"),
    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    UNSUBSCRIBE("unsubscribe","用户取消关注"),

    /**
     * 用户未关注时，进行关注后的事件推送
     */
    SUBSCRIBE_QRSCENE("subscribe","用户未关注时，进行关注后的事件推送"),


    /**
     * 事件类型：scan(用户已关注时的扫描带参数二维码)
     */
    SCAN("SCAN","用户扫描"),
    /**
     * 事件类型：LOCATION(上报地理位置)
     */
    LOCATION("LOCATION","上报地理位置"),
    /**
     * 事件类型：CLICK(自定义菜单)
     */
    CLICK("CLICK","自定义菜单"),
    /**
     * 事件类型 TEMPLATESENDJOBFINISH(模板送达结果)
     */
    TEMPLATESENDJOBFINISH("TEMPLATESENDJOBFINISH","模板送达结果"),

    /**
     * 点击菜单跳转链接时的事件推送
     */
    VIEW("VIEW","点击菜单跳转链接时的事件推送"),

    ;
    private final String eventCode;

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
