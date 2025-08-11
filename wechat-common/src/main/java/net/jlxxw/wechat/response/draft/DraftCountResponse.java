package net.jlxxw.wechat.response.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;


/**
 * 草稿总数响应
 */
public class DraftCountResponse extends WeChatResponse {
    /**
     * 图文消息草稿总数
     */
    @JSONField(name = "total_count")
    @JsonProperty(value = "total_count")
    private Integer totalCount;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}