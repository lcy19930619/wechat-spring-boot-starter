package net.jlxxw.wechat.response.freepublish;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * 已发布图文内容条目
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_batchget.html">文档地址</a>
 */
public class FreePublishNewsItem {

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字。
     */
    private String digest;

    /**
     * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。 图片消息则仅支持纯文本和部分特殊功能标签如商品，商品个数不可超过50个
     */
    private String content;

    /**
     * 图文消息的原文地址，即点击"阅读原文"后的URL
     */
    @JSONField(name = "content_source_url")
    @JsonProperty(value = "content_source_url")
    private String contentSourceUrl;

    /**
     * 图文消息的封面图片素材id（必须是永久MediaID）
     */
    @JSONField(name = "thumb_media_id")
    @JsonProperty(value = "thumb_media_id")
    private String thumbMediaId;

    /**
     * 图文消息的封面图片URL
     */
    @JSONField(name = "thumb_url")
    @JsonProperty(value = "thumb_url")
    private String thumbUrl;

    /**
     * 是否打开评论，0不打开(默认)，1打开
     */
    @JSONField(name = "need_open_comment")
    @JsonProperty(value = "need_open_comment")
    private Integer needOpenComment;

    /**
     * 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    @JSONField(name = "only_fans_can_comment")
    @JsonProperty(value = "only_fans_can_comment")
    private Integer onlyFansCanComment;

    /**
     * 草稿的临时链接
     */
    private String url;

    /**
     * 该图文是否被删除
     */
    @JSONField(name = "is_deleted")
    @JsonProperty(value = "is_deleted")
    private Boolean deleted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Integer getNeedOpenComment() {
        return needOpenComment;
    }

    public void setNeedOpenComment(Integer needOpenComment) {
        this.needOpenComment = needOpenComment;
    }

    public Integer getOnlyFansCanComment() {
        return onlyFansCanComment;
    }

    public void setOnlyFansCanComment(Integer onlyFansCanComment) {
        this.onlyFansCanComment = onlyFansCanComment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreePublishNewsItem)) {
            return false;
        }
        FreePublishNewsItem that = (FreePublishNewsItem) o;
        return Objects.equals(title, that.title) && Objects.equals(author, that.author)
            && Objects.equals(digest, that.digest) && Objects.equals(content, that.content)
            && Objects.equals(contentSourceUrl, that.contentSourceUrl) && Objects.equals(
            thumbMediaId, that.thumbMediaId) && Objects.equals(thumbUrl, that.thumbUrl)
            && Objects.equals(needOpenComment, that.needOpenComment) && Objects.equals(
            onlyFansCanComment, that.onlyFansCanComment) && Objects.equals(url, that.url)
            && Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, digest, content, contentSourceUrl, thumbMediaId, thumbUrl,
            needOpenComment, onlyFansCanComment, url, deleted);
    }

    @Override
    public String toString() {
        return "FreePublishNewsItem{" +
            "title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", digest='" + digest + '\'' +
            ", content='" + content + '\'' +
            ", contentSourceUrl='" + contentSourceUrl + '\'' +
            ", thumbMediaId='" + thumbMediaId + '\'' +
            ", thumbUrl='" + thumbUrl + '\'' +
            ", needOpenComment=" + needOpenComment +
            ", onlyFansCanComment=" + onlyFansCanComment +
            ", url='" + url + '\'' +
            ", deleted=" + deleted +
            '}';
    }
}