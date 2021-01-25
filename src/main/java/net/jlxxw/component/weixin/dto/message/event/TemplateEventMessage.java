package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.WeiXinMessage;

/**
 * @author chunyang.leng
 * @date 2021/1/25 1:35 下午
 */
public class TemplateEventMessage extends WeiXinMessage {
    /**
     * 消息id
     */
    private Integer MsgID;

    /**
     * 发送状态为成功
     */
    private String Status;

    public Integer getMsgID() {
        return MsgID;
    }

    public void setMsgID(Integer msgID) {
        MsgID = msgID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
