package net.jlxxw.wechat.response.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

import java.util.List;

/**
 * 获取草稿列表返回结果
 */
public class DraftGetResponse extends WeChatResponse {

    /**
     * 图文素材列表
     */
    @JSONField(name = "news_item")
    @JsonProperty(value = "news_item")
    private List<ArticleItem> newsItem;

    public List<ArticleItem> getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(List<ArticleItem> newsItem) {
        this.newsItem = newsItem;
    }
}
