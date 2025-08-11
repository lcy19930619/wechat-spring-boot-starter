package net.jlxxw.wechat.dto.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * 发布草稿的请求参数
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_submit.html">文档地址</a>
 */
public class FreePublishSubmitDTO {

    /**
     * 要发布的草稿的media_id
     */
    @NotBlank(message = "草稿media_id不能为空")
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