package net.jlxxw.wechat.dto.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * 删除已发布文章的请求参数
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublishdelete.html">文档地址</a>
 */
public class FreePublishDeleteDTO {

    /**
     * 成功发布时返回的 article_id
     */
    @NotBlank(message = "article_id不能为空")
    @JSONField(name = "article_id")
    @JsonProperty(value = "article_id")
    private String articleId;

    /**
     * 要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章
     */
    private Integer index;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishDeleteDTO)) {
            return false;
        }
        FreePublishDeleteDTO that = (FreePublishDeleteDTO) o;
        return Objects.equals(articleId, that.articleId) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, index);
    }

    @Override
    public String toString() {
        return "FreePublishDeleteDTO{" +
            "articleId='" + articleId + '\'' +
            ", index=" + index +
            '}';
    }
}