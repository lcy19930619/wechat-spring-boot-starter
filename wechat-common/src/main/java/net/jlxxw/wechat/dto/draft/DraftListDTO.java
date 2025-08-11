package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 获取草稿列表请求DTO
 */
public class DraftListDTO {
    /**
     * 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     */
    @NotNull(message = "偏移位置不能为空")
    @Min(value = 0, message = "偏移位置不能小于0")
    private Integer offset;
    
    /**
     * 返回素材的数量，取值在1到20之间
     */
    @NotNull(message = "返回素材的数量不能为空")
    @Min(value = 1, message = "返回素材的数量不能小于1")
    @Max(value = 20, message = "返回素材的数量不能大于20")
    private Integer count;
    
    /**
     * 1 表示不返回 content 字段，0 表示正常返回，默认为 0
     */
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
}