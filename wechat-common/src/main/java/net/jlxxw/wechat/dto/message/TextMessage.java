package net.jlxxw.wechat.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 文本类型信息
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:18 上午
 */
public class TextMessage extends AbstractWeChatMessage {

    /**
     * 文本消息内容
     */
    private String content;

    /**
     * 客服接口发送调研菜单时，用户点击的菜单id
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html">文档地址</a>
     */
    @JsonProperty(value = "bizmsgmenuid")
    private String bizMsgMenuId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBizMsgMenuId() {
        return bizMsgMenuId;
    }

    public void setBizMsgMenuId(String bizMsgMenuId) {
        this.bizMsgMenuId = bizMsgMenuId;
    }
}
