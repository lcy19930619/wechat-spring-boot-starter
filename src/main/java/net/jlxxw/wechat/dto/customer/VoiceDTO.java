package net.jlxxw.wechat.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 语音消息
 * @author chunyang.leng
 * @date 2022-06-17 10:53 PM
 */
public class VoiceDTO {
    /**
     * 必填，发送的图片/语音/视频/图文消息（点击跳转到图文消息页）的媒体ID
     */
    @JsonProperty(value = "media_id")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
