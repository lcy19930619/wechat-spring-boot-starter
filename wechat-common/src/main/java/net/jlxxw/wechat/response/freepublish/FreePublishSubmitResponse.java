package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 发布草稿的响应结果
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_submit.html">文档地址</a>
 */
public class FreePublishSubmitResponse extends WeChatResponse {

    /**
     * 发布任务的id
     */
    @JSONField(name = "publish_id")
    @JsonProperty(value = "publish_id")
    private String publishId;

    /**
     * 消息的数据ID
     */
    @JSONField(name = "msg_data_id")
    @JsonProperty(value = "msg_data_id")
    private String msgDataId;

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getMsgDataId() {
        return msgDataId;
    }

    public void setMsgDataId(String msgDataId) {
        this.msgDataId = msgDataId;
    }
}