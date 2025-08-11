package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * 已发布文章条目
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_batchget.html">文档地址</a>
 */
public class FreePublishItem {

    /**
     * 成功发布的图文消息id
     */
    @JSONField(name = "article_id")
    @JsonProperty(value = "article_id")
    private String articleId;

    /**
     * 图文消息内容
     */
    private FreePublishContent content;

    /**
     * 图文消息更新时间
     */
    @JSONField(name = "update_time")
    @JsonProperty(value = "update_time")
    private Long updateTime;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public FreePublishContent getContent() {
        return content;
    }

    public void setContent(FreePublishContent content) {
        this.content = content;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishItem)) {
            return false;
        }
        FreePublishItem that = (FreePublishItem) o;
        return Objects.equals(articleId, that.articleId) && Objects.equals(content, that.content)
            && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, content, updateTime);
    }

    @Override
    public String toString() {
        return "FreePublishItem{" +
            "articleId='" + articleId + '\'' +
            ", content=" + content +
            ", updateTime=" + updateTime +
            '}';
    }
}