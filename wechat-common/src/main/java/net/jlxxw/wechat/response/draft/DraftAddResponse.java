package net.jlxxw.wechat.response.draft;

import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 新增草稿响应
 */
public class DraftAddResponse extends WeChatResponse {
    /**
     * 新增的图文消息素材id
     */
    private String mediaId;
    

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

}