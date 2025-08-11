package net.jlxxw.wechat.response.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import net.jlxxw.wechat.dto.draft.CoverInfo;
import net.jlxxw.wechat.dto.draft.ImageInfo;
import net.jlxxw.wechat.dto.draft.ProductInfo;

/**
 * 内部文章类，包含所有文章字段
 */
public class ArticleItem {
    /**
     * 文章类型，
     * 分别有图文消息（news）
     * 图片消息（newspic）
     * 不填默认为图文消息（news）
     */
    @JSONField(name = "article_type")
    @JsonProperty(value = "article_type")
    private String articleType;
    /**
     * 图文消息的标题
     */
    @NotBlank(message = "图文消息的标题不能为空")
    private String title;
    /**
     * 图文消息的作者
     */
    private String author;

    /**
     * 图文消息的描述，如本字段为空，则默认抓取正文前54个字
     */
    private String digest;
    /**
     * 图文消息页面的内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
     */
    @NotBlank(message = "图文消息页面的内容不能为空")
    private String content;
    /**
     * 图文消息的原文地址，即点击"阅读原文"后的URL
     */
    @JSONField(name = "content_source_url")
    @JsonProperty(value = "content_source_url")
    private String contentSourceUrl;

    /**
     * 图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得
     */
    @JSONField(name = "thumb_media_id")
    @JsonProperty(value = "thumb_media_id")
    private String thumbMediaId;

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
     * 图片消息里的图片相关信息，图片数量最多为20张，首张图片即为封面图
     */
    @JSONField(name = "image_info")
    @JsonProperty(value = "image_info")
    private ImageInfo imageInfo;

    /**
     * 商品信息
     */
    @JSONField(name = "product_info")
    @JsonProperty(value = "product_info")
    private ProductInfo productInfo;

    /**
     * 草稿的临时链接
     */
    private String url;

    // Getters and setters
    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

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

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
