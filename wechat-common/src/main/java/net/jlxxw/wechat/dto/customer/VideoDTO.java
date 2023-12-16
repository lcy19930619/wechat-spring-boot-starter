package net.jlxxw.wechat.dto.customer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 视频消息
 * @author chunyang.leng
 * @date 2022-06-28 3:16 PM
 */
public class VideoDTO {
    /**
     * 必填，发送的图片/语音/视频/图文消息（点击跳转到图文消息页）的媒体ID
     */
    @JSONField(name = "media_id")
    @JsonProperty(value = "media_id")
    private String mediaId;
    /**
     * 必填，缩略图/小程序卡片图片的媒体ID，小程序卡片图片建议大小为520*416
     */
    @JSONField(name = "thumb_media_id")
    @JsonProperty(value = "thumb_media_id")
    private String thumbMediaId;

    private String title;

    private String description;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
