package net.jlxxw.wechat.dto.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * 获取发布状态的请求参数
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_get.html">文档地址</a>
 */
public class FreePublishGetDTO {

    /**
     * 发布任务id
     */
    @NotBlank(message = "publish_id不能为空")
    @JSONField(name = "publish_id")
    @JsonProperty(value = "publish_id")
    private String publishId;

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishGetDTO)) {
            return false;
        }
        FreePublishGetDTO that = (FreePublishGetDTO) o;
        return Objects.equals(publishId, that.publishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publishId);
    }

    @Override
    public String toString() {
        return "FreePublishGetDTO{" +
            "publishId='" + publishId + '\'' +
            '}';
    }
}