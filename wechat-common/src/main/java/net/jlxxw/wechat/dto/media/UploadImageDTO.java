package net.jlxxw.wechat.dto.media;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 上传图文消息图片DTO
 *
 * @author chunyang.leng
 * @date 2025-08-11 6:05 PM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/material/permanent/api_uploadimage.html">文档地址</a>
 */
public class UploadImageDTO {

    /**
     * 图片文件
     */
    @NotNull(message = "图片文件不能为空")
    @JSONField(name = "media")
    @JsonProperty("media")
    private File media;

    public @NotNull(message = "图片文件不能为空") File getMedia() {
        return media;
    }

    public void setMedia(@NotNull(message = "图片文件不能为空") File media) {
        this.media = media;
    }
}