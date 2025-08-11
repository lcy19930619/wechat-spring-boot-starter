package net.jlxxw.wechat.dto.draft;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 更新草稿请求DTO
 */
public class DraftUpdateDTO {
    /**
     * 要修改的图文消息的id
     */
    @NotBlank(message = "要修改的图文消息的id不能为空")
    private String mediaId;
    
    /**
     * 要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
     */
    @NotNull(message = "要更新的文章在图文消息中的位置不能为空")
    private Integer index;
    
    /**
     * 图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得
     */
    private String thumbMediaId;
    
    /**
     * 图文消息的作者
     */
    private String author;
    
    /**
     * 图文消息的标题
     */
    private String title;
    
    /**
     * 在图文消息页面点击"阅读原文"后的页面链接
     */
    private String contentSourceUrl;
    
    /**
     * 图文消息页面的内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
     */
    private String content;
    
    /**
     * 图文消息的描述，如本字段为空，则默认抓取正文前54个字
     */
    private String digest;
    
    /**
     * 是否打开评论，0不打开(默认)，1打开
     */
    private Integer needOpenComment;
    
    /**
     * 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    private Integer onlyFansCanComment;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
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
}