package net.jlxxw.wechat.dto.customer.news;

/**
 * 图文信息具体内容
 * @author chunyang.leng
 * @date 2020/11/11 12:55
 */
public class ArticlesDTO {
    private String title;

    private String description;

    private String url;

    private String picurl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
