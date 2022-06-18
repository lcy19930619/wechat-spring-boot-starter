package net.jlxxw.wechat.dto.customer.news;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 图片信息
 * @author chunyang.leng
 * @date 2022-06-17 10:53 PM
 */
public class ImageDTO {

    @JsonProperty(value = "media_id")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
