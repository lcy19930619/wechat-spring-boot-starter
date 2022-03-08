package net.jlxxw.wechat.dto.message.event;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户未关注时，进行关注后的事件推送
 *
 * @author chunyang.leng
 * @date 2021/1/22 7:20 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E6%89%AB%E6%8F%8F%E5%B8%A6%E5%8F%82%E6%95%B0%E4%BA%8C%E7%BB%B4%E7%A0%81%E4%BA%8B%E4%BB%B6">文档地址</a>
 */
public class SubscribeQrsceneEventMessage extends SubscribeEventMessage {

    /**
     * 事件KEY值，qrscene_为前缀，后面为二维码的参数值
     */
    private String eventKey;

    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    private String ticket;

    @Override
    public String getEventKey() {
        return eventKey;
    }

    @Override
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
