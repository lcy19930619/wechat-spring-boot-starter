package net.jlxxw.wechat.dto.draft;

/**
 * 草稿图文内容DTO
 */
public class DraftNewsItemDTO {
    /**
     * 文章类型，分别有图文消息（news）、图片消息（newspic），不填默认为图文消息（news）
     */
    private String articleType;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 作者
     */
    private String author;

    /**
     * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,
     * 涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。
     * 外部图片url将被过滤。 图片消息则仅支持纯文本和部分特殊功能标签如商品，商品个数不可超过50个
     */
    private String content;
    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。
     * 如果本字段为没有填写，则默认抓取正文前54个字。
     */
    private String digest;

    /**
     * 图文消息的原文地址，即点击"阅读原文"后的URL
     */
    private String contentSourceUrl;
    
    /**
     * 图文消息的封面图片素材id（必须是永久MediaID）
     */
    private String thumbMediaId;
    
    /**
     * 是否打开评论，0不打开(默认)，1打开
     */
    private Integer needOpenComment;
    
    /**
     * 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    private Integer onlyFansCanComment;
    
    /**
     * 图片消息里的图片相关信息，图片数量最多为20张，首张图片即为封面图
     */
    private DraftImageInfoDTO imageInfo;
    
    /**
     * 商品信息
     */
    private DraftProductInfoDTO productInfo;
    
    /**
     * 草稿的临时链接
     */
    private String url;

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

    public DraftImageInfoDTO getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(DraftImageInfoDTO imageInfo) {
        this.imageInfo = imageInfo;
    }

    public DraftProductInfoDTO getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(DraftProductInfoDTO productInfo) {
        this.productInfo = productInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}