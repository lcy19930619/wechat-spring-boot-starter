package net.jlxxw.wechat.dto.customer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 小程序页面
 * @author chunyang.leng
 * @date 2022-06-28 3:30 PM
 */
public class MiniProgramPageDTO {

    /**
     * 非必填，图文消息/视频消息/音乐消息/小程序卡片的标题
     */
    private String title;
    /**
     * 必填，小程序的appid，要求小程序的 appid 需要与公众号有关联关系
     */
    private String appid;
    /**
     * 必填，小程序的页面路径，跟 app.json 对齐，支持参数，比如pages/index/index?foo=bar
     */
    @JSONField(name = "pagepath")
    @JsonProperty(value = "pagepath")
    private String pagePath;

    /**
     * 必填，缩略图/小程序卡片图片的媒体ID，小程序卡片图片建议大小为520*416
     */
    @JSONField(name = "thumb_media_id")
    @JsonProperty(value = "thumb_media_id")
    private String thumbMediaId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
