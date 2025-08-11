package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chunyang.leng
 * @date 2022-08-16 14:00
 */
public class DraftPicItemDTO {
    /**
     * 图片的md5值
     */
    @JSONField(name = "image_media_id")
    @JsonProperty(value = "image_media_id")
    private String picMd5;

    /**
     * 图片的url
     */
    @JSONField(name = "image_url")
    @JsonProperty(value = "image_url")
    private String picUrl;

    public String getPicMd5() {
        return picMd5;
    }

    public void setPicMd5(String picMd5) {
        this.picMd5 = picMd5;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}