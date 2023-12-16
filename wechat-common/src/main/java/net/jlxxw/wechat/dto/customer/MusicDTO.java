package net.jlxxw.wechat.dto.customer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 音乐消息
 * @author chunyang.leng
 * @date 2022-06-28 3:17 PM
 */
public class MusicDTO {

    /**
     * 非必填，图文消息/视频消息/音乐消息/小程序卡片的标题
     */
    private String title;
    /**
     * 非必填，图文消息/视频消息/音乐消息的描述
     */
    private String description;

    /**
     * 必填，音乐链接
     */
    @JSONField(name = "musicurl")
    @JsonProperty(value = "musicurl")
    private String musicUrl;

    /**
     * 必填，高品质音乐链接，wifi环境优先使用该链接播放音乐
     */
    @JSONField(name = "hqmusicurl")
    @JsonProperty(value = "hqmusicurl")
    private String hqMusicUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getHqMusicUrl() {
        return hqMusicUrl;
    }

    public void setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
