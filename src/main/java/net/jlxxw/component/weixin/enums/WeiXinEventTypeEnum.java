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
    SUBSCRIBE("subscribe"),
    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    UNSUBSCRIBE("unsubscribe"),
    /**
     * 事件类型：scan(用户已关注时的扫描带参数二维码)
     */
    SCAN("SCAN"),
    /**
     * 事件类型：LOCATION(上报地理位置)
     */
    LOCATION("LOCATION"),
    /**
     * 事件类型：CLICK(自定义菜单)
     */
    CLICK("CLICK"),
    /**
     * 事件类型 TEMPLATESENDJOBFINISH(模板送达结果)
     */
    TEMPLATESENDJOBFINISH("TEMPLATESENDJOBFINISH"),

    /**
     * 点击菜单跳转链接时的事件推送
     */
    VIEW("VIEW"),

    ;
    String eventCode;

    WeiXinEventTypeEnum(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventCode() {
        return eventCode;
    }
}
