package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

import java.util.List;
import java.util.Objects;

/**
 * 获取已发布文章详情的响应结果
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublishgetarticle.html">文档地址</a>
 */
public class FreePublishGetArticleResponse extends WeChatResponse {

    /**
     * 图文信息集合
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
        if (!(o instanceof FreePublishGetArticleResponse)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FreePublishGetArticleResponse that = (FreePublishGetArticleResponse) o;
        return Objects.equals(newsItem, that.newsItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), newsItem);
    }

    @Override
    public String toString() {
        return "FreePublishGetArticleResponse{" +
            "newsItem=" + newsItem +
            '}';
    }
}