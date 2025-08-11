package net.jlxxw.wechat.response.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;
import java.util.List;

/**
 * 草稿列表响应
 */
public class DraftListResponse extends WeChatResponse {
    /**
     * 素材列表
     */
    private List<DraftListItem> item;
    
    /**
     * 草稿素材的总数
     */
    @JSONField(name = "total_count")
    @JsonProperty(value = "total_count")
    private Integer totalCount;
    /**
     * 本次调用获取的素材的数量
     */
    @JSONField(name = "item_count")
    @JsonProperty(value = "item_count")
    private Integer item_count;

    public List<DraftListItem> getItem() {
        return item;
    }

    public void setItem(List<DraftListItem> item) {
        this.item = item;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}