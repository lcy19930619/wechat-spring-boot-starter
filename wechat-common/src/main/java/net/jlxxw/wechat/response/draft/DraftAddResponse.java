package net.jlxxw.wechat.response.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 新增草稿响应
 * 
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_add.html">文档地址</a>
 */
public class DraftAddResponse extends WeChatResponse {
    /**
     * 新增的图文消息素材id
     */
    @JSONField(name = "media_id")
    @JsonProperty(value = "media_id")
    private String mediaId;
    

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

}