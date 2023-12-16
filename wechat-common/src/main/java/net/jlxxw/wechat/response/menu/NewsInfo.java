package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 菜单返回数据
 *
 * @author chunyang.leng
 * @date 2021-12-20 11:01 上午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Querying_Custom_Menus.html">文档地址</a>
 */
public class NewsInfo {

    /**
     * 图文消息的标题
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 作者
     */
    @JSONField(name = "author")
    private String author;

    /**
     * 摘要
     */
    @JSONField(name = "digest")
    private String digest;

    /**
     * 是否显示封面，0为不显示，1为显示
     */
    @JSONField(name = "show_cover")
    private Integer showCover;

    /**
     * 封面图片的URL
     */
    @JSONField(name = "cover_url")
    private String coverUrl;

    /**
     * 正文的URL
     */
    @JSONField(name = "content_url")
    private String contentUrl;

    /**
     * 原文的URL，若置空则无查看原文入口
     */
    @JSONField(name = "source_url")
    private String sourceUrl;

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

    public Integer getShowCover() {
        return showCover;
    }

    public void setShowCover(Integer showCover) {
        this.showCover = showCover;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
