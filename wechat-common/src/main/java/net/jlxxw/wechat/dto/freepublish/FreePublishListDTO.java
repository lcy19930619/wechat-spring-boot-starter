package net.jlxxw.wechat.dto.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 获取已发布文章列表的请求参数
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_batchget.html">文档地址</a>
 */
public class FreePublishListDTO {

    /**
     * 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
     */
    @NotNull(message = "offset不能为空")
    @Min(value = 0, message = "offset必须大于等于0")
    private Integer offset;

    /**
     * 返回素材的数量，取值在1到20之间
     */
    @NotNull(message = "count不能为空")
    @Min(value = 1, message = "count必须大于等于1")
    @Max(value = 20, message = "count必须小于等于20")
    private Integer count;

    /**
     * 1 表示不返回content字段，0表示正常返回，默认为0
     */
    @Min(value = 0, message = "noContent必须大于等于0")
    @Max(value = 1, message = "noContent必须小于等于1")
    @JSONField(name = "no_content")
    @JsonProperty(value = "no_content")
    private Integer noContent;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getNoContent() {
        return noContent;
    }

    public void setNoContent(Integer noContent) {
        this.noContent = noContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishListDTO)) {
            return false;
        }
        FreePublishListDTO that = (FreePublishListDTO) o;
        return Objects.equals(offset, that.offset) && Objects.equals(count, that.count)
            && Objects.equals(noContent, that.noContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, count, noContent);
    }

    @Override
    public String toString() {
        return "FreePublishListDTO{" +
            "offset=" + offset +
            ", count=" + count +
            ", noContent=" + noContent +
            '}';
    }
}