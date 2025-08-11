package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class InnerImage {
    /**
     * 图片消息里的图片素材id（必须是永久MediaID）
     */
    @NotBlank(message = "图片素材media_id不能为空")
    @JSONField(name = "image_media_id")
    @JsonProperty(value = "image_media_id")
    private String imageMediaId;

    public String getImageMediaId() {
        return imageMediaId;
    }

    public void setImageMediaId(String imageMediaId) {
        this.imageMediaId = imageMediaId;
    }
}
