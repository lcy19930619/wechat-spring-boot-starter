package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * 文章详情中的条目信息
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_get.html">文档地址</a>
 */
public class FreePublishArticleItem {

    /**
     * 文章对应的编号
     */
    private Integer idx;

    /**
     * 图文的永久链接
     */
    @JSONField(name = "article_url")
    @JsonProperty(value = "article_url")
    private String articleUrl;

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishArticleItem)) {
            return false;
        }
        FreePublishArticleItem that = (FreePublishArticleItem) o;
        return Objects.equals(idx, that.idx) && Objects.equals(articleUrl, that.articleUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idx, articleUrl);
    }

    @Override
    public String toString() {
        return "FreePublishArticleItem{" +
            "idx=" + idx +
            ", articleUrl='" + articleUrl + '\'' +
            '}';
    }
}