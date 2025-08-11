package net.jlxxw.wechat.dto.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * 获取已发布文章详情的请求参数
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublishgetarticle.html">文档地址</a>
 */
public class FreePublishGetArticleDTO {

    /**
     * 要获取的草稿的article_id
     */
    @NotBlank(message = "article_id不能为空")
    @JSONField(name = "article_id")
    @JsonProperty(value = "article_id")
    private String articleId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishGetArticleDTO)) {
            return false;
        }
        FreePublishGetArticleDTO that = (FreePublishGetArticleDTO) o;
        return Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId);
    }

    @Override
    public String toString() {
        return "FreePublishGetArticleDTO{" +
            "articleId='" + articleId + '\'' +
            '}';
    }
}