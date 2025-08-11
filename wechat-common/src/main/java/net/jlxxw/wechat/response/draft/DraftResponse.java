package net.jlxxw.wechat.response.draft;

import net.jlxxw.wechat.dto.draft.DraftContentDTO;
import net.jlxxw.wechat.response.WeChatResponse;
import java.util.Date;

/**
 * 草稿详情响应
 */
public class DraftResponse extends WeChatResponse {
    /**
     * 草稿内容
     */
    private DraftContentDTO content;
    
    /**
     * 该草稿最近一次更新的时间
     */
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
}