package net.jlxxw.wechat.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;
import java.util.List;

/**
 * 微信消息应答对象
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:51 上午
 */
@JacksonXmlRootElement(localName = "xml")
public class WeiXinMessageResponse {
    /**
     * 接收方帐号（收到的OpenID）
     */
    @JacksonXmlCData
    private String toUserName;

    /**
     * 开发者微信号
     */
    @JacksonXmlCData
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private Long createTime;

    /**
     * 消息类型，
     * 文本为text
     */
    @JacksonXmlCData
    private String msgType;

    /**
     * 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
     */
    @JacksonXmlCData
    private String content;

    /**
     * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    private Integer articleCount;

    /**
     * 图文
     */
    private List<Article> Articles;
    /**
     * 图片
     */
    private Image image;
    /**
     * 音乐
     */
    private Music music;
    /**
     * 视频
     */
    private Video video;
    /**
     * 音频
     */
    private Voice voice;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        Articles = articles;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Voice getVoice() {
        return voice;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    /**
     * 回复文本消息
     *
     * @param content
     * @return
     */
    public static WeiXinMessageResponse buildText(String content) {
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("text");
        weiXinMessageResponse.setContent(content);
        return weiXinMessageResponse;
    }

    /**
     * 回复图片消息
     *
     * @param mediaId 通过素材管理中的接口上传多媒体文件，得到的id。
     * @return
     */
    public static WeiXinMessageResponse buildImage(String mediaId) {
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("image");
        Image image = new Image();
        image.setMediaId(mediaId);
        weiXinMessageResponse.setImage(image);
        return weiXinMessageResponse;
    }

    /**
     * 回复语音消息
     *
     * @param mediaId 通过素材管理中的接口上传多媒体文件，得到的id。
     * @return
     */
    public static WeiXinMessageResponse buildVoice(String mediaId) {
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("voice");
        Voice voice = new Voice();
        voice.setMediaId(mediaId);
        weiXinMessageResponse.setVoice(voice);
        return weiXinMessageResponse;
    }

    /**
     * 回复视频消息
     *
     * @param mediaId     通过素材管理中的接口上传多媒体文件，得到的id
     * @param title       视频消息的标题
     * @param description 视频消息的描述
     * @return
     */
    public static WeiXinMessageResponse buildVideo(String mediaId, String title, String description) {
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("video");
        Video video = new Video();
        video.setMediaId(mediaId);
        video.setTitle(title);
        video.setDescription(description);
        weiXinMessageResponse.setVideo(video);
        return weiXinMessageResponse;
    }

    /**
     * 回复音乐消息
     *
     * @param title        音乐标题
     * @param description  音乐描述
     * @param musicUrl     音乐链接
     * @param hqMusicUrl   高质量音乐链接，WIFI环境优先使用该链接播放音乐
     * @param thumbMediaId 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
     * @return
     */
    public static WeiXinMessageResponse buildMusic(String title, String description, String musicUrl, String hqMusicUrl, String thumbMediaId) {
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("music");
        Music music = new Music();
        music.setTitle(title);
        music.setDescription(description);
        music.setMusicURL(musicUrl);
        music.sethQMusicUrl(hqMusicUrl);
        music.setThumbMediaId(thumbMediaId);
        weiXinMessageResponse.setMusic(music);
        return weiXinMessageResponse;
    }

    /**
     * 回复图文消息
     *
     * @param articles
     * @return
     */
    public static WeiXinMessageResponse buildArticle(Article... articles) {
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("news");
        weiXinMessageResponse.setArticleCount(articles.length);
        weiXinMessageResponse.setArticles(Arrays.asList(articles));
        return weiXinMessageResponse;
    }
}

class Image {
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    @JacksonXmlCData
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}

class Voice {
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    @JacksonXmlCData
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}

class Video {
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    @JacksonXmlCData
    private String mediaId;
    /**
     * 视频消息的标题
     */
    @JacksonXmlCData
    private String title;
    /**
     * 视频消息的描述
     */
    @JacksonXmlCData
    private String description;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

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
}

class Music {
    /**
     * 音乐标题
     */
    @JacksonXmlCData
    private String title;
    /**
     * 音乐描述
     */
    @JacksonXmlCData
    private String description;

    /**
     * 音乐链接
     */
    @JacksonXmlCData
    private String musicURL;

    /**
     * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
     */
    @JacksonXmlCData
    private String hQMusicUrl;

    /**
     * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
     */
    @JacksonXmlCData
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

    public String getMusicURL() {
        return musicURL;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }

    public String gethQMusicUrl() {
        return hQMusicUrl;
    }

    public void sethQMusicUrl(String hQMusicUrl) {
        this.hQMusicUrl = hQMusicUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}

class Article {
    /**
     * 图文消息标题
     */
    @JacksonXmlCData
    private String title;

    /**
     * 图文消息描述
     */
    @JacksonXmlCData
    private String description;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    @JacksonXmlCData
    private String picUrl;

    /**
     * 点击图文消息跳转链接
     */
    @JacksonXmlCData
    private String url;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

