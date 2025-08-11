package net.jlxxw.wechat.dto.draft;

import java.util.List;

/**
 * 草稿内容DTO
 */
public class DraftContentDTO {
    /**
     * 图文内容列表
     */
    private List<DraftNewsItemDTO> newsItem;

    public List<DraftNewsItemDTO> getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(List<DraftNewsItemDTO> newsItem) {
        this.newsItem = newsItem;
    }
}