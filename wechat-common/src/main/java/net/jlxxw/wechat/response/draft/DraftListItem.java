package net.jlxxw.wechat.response.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.dto.draft.DraftContentDTO;
import java.util.Date;

/**
 * 草稿列表项
 */
public class DraftListItem {
    /**
     * 草稿内容
     */
    private DraftContentDTO content;
    /**
     * 该草稿的id
     */
    @JSONField(name = "media_id")
    @JsonProperty(value = "media_id")
    private String mediaId;
    /**
     * 该草稿最近一次更新的时间
     */
    @JSONField(name = "update_time")
    @JsonProperty(value = "update_time")
    private Date updateTime;
    

    public DraftContentDTO getContent() {
        return content;
    }

    public void setContent(DraftContentDTO content) {
        this.content = content;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

}