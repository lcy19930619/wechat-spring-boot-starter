package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

/**
 * 已发布文章内容
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_batchget.html">文档地址</a>
 */
public class FreePublishContent {

    /**
     * 图文内容列表
     */
    @JSONField(name = "news_item")
    @JsonProperty(value = "news_item")
    private List<FreePublishNewsItem> newsItem;

    public List<FreePublishNewsItem> getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(List<FreePublishNewsItem> newsItem) {
        this.newsItem = newsItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishContent)) {
            return false;
        }
        FreePublishContent that = (FreePublishContent) o;
        return Objects.equals(newsItem, that.newsItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsItem);
    }

    @Override
    public String toString() {
        return "FreePublishContent{" +
            "newsItem=" + newsItem +
            '}';
    }
}