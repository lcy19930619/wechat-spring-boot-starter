package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;

/**
 * 关注事件
 *
 * @author chunyang.leng
 * @date 2021/1/22 7:17 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E5%85%B3%E6%B3%A8-%E5%8F%96%E6%B6%88%E5%85%B3%E6%B3%A8%E4%BA%8B%E4%BB%B6">文档地址</a>
 */
public class SubscribeEventMessage extends AbstractWeChatMessage {

    /**
     * 事件类型
     */
    private String eventKey;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
