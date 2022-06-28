package net.jlxxw.wechat.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 图文信息具体内容
 *
 * @author chunyang.leng
 * @date 2020/11/11 12:55
 */
public class ArticlesDTO {
    /**
     * 非必填，图文消息/视频消息/音乐消息/小程序卡片的标题
     */
    private String title;

    /**
     * 非必填，图文消息/视频消息/音乐消息的描述
     */
    private String description;

    /**
     * 非必填，图文消息被点击后跳转的链接
     */
    private String url;

    /**
     * 非必填图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
     */
    @JsonProperty(value = "picurl")
    private String picUrl;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
