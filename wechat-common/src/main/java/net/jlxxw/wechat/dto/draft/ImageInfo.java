package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;

import java.util.List;

public class ImageInfo {
    /**
     * 图片列表
     */
    @JSONField(name = "image_list")
    @JsonProperty(value = "image_list")
    @Max.List(@Max(value = 20, message = "图片数量最多为20张"))
    private List<InnerImage> imageList;

    public @Max.List(@Max(value = 20, message = "图片数量最多为20张")) List<InnerImage> getImageList() {
        return imageList;
    }

    public void setImageList(@Max.List(@Max(value = 20, message = "图片数量最多为20张")) List<InnerImage> imageList) {
        this.imageList = imageList;
    }
}
