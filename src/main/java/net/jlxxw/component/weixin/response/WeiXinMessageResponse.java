package net.jlxxw.component.weixin.response;

import java.util.Arrays;
import java.util.List;

/**
 * 微信消息应答对象
 * @author chunyang.leng
 * @date 2021/1/20 11:51 上午
 */
public class WeiXinMessageResponse {
    /**
     * 接收方帐号（收到的OpenID）
     */
    private String ToUserName;

    /**
     * 开发者微信号
     */
    private String FromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private Long CreateTime;

    /**
     * 消息类型，
     * 文本为text
     */
    private String MsgType;

    /**
     * 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
     */
    private String Content;

    /**
     * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    private Integer ArticleCount;

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
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Integer getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(Integer articleCount) {
        ArticleCount = articleCount;
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
     * @param content
     * @return
     */
    public static WeiXinMessageResponse buildText(String content){
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("text");
        weiXinMessageResponse.setContent(content);
        return weiXinMessageResponse;
    }

    /**
     * 回复图片消息
     * @param mediaId 通过素材管理中的接口上传多媒体文件，得到的id。
     * @return
     */
    public static WeiXinMessageResponse buildImage(String mediaId){
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("image");
        Image image = new Image();
        image.setMediaId(mediaId);
        weiXinMessageResponse.setImage(image);
        return weiXinMessageResponse;
    }

    /**
     * 回复语音消息
     * @param mediaId 通过素材管理中的接口上传多媒体文件，得到的id。
     * @return
     */
    public static WeiXinMessageResponse buildVoice(String mediaId){
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("voice");
        Voice voice = new Voice();
        voice.setMediaId(mediaId);
        weiXinMessageResponse.setVoice(voice);
        return weiXinMessageResponse;
    }

    /**
     * 回复视频消息
     * @param mediaId 通过素材管理中的接口上传多媒体文件，得到的id
     * @param title 视频消息的标题
     * @param description 视频消息的描述
     * @return
     */
    public static WeiXinMessageResponse buildVideo(String mediaId,String title,String description){
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
     * @param title 音乐标题
     * @param description 音乐描述
     * @param musicUrl 音乐链接
     * @param hqMusicUrl 高质量音乐链接，WIFI环境优先使用该链接播放音乐
     * @param thumbMediaId 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
     * @return
     */
    public static WeiXinMessageResponse buildMusic(String title,String description,String musicUrl,String hqMusicUrl,String thumbMediaId){
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("music");
        Music music = new Music();
        music.setTitle(title);
        music.setDescription(description);
        music.setMusicURL(musicUrl);
        music.setHQMusicUrl(hqMusicUrl);
        music.setThumbMediaId(thumbMediaId);
        weiXinMessageResponse.setMusic(music);
        return weiXinMessageResponse;
    }

    /**
     * 回复图文消息
     * @param articles
     * @return
     */
    public static WeiXinMessageResponse buildArticle(Article... articles){
        WeiXinMessageResponse weiXinMessageResponse = new WeiXinMessageResponse();
        weiXinMessageResponse.setMsgType("news");
        weiXinMessageResponse.setArticleCount(articles.length);
        weiXinMessageResponse.setArticles(Arrays.asList(articles));
        return weiXinMessageResponse;
    }
}

class Image{
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}

class Voice{
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}

class Video{
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    private String MediaId;
    /**
     * 视频消息的标题
     */
    private String title;
    /**
     * 视频消息的描述
     */
    private String Description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}

class Music{
    /**
     * 音乐标题
     */
    private String title;
    /**
     * 音乐描述
     */
    private String Description;

    /**
     * 音乐链接
     */
    private String MusicURL;

    /**
     * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
     */
    private String HQMusicUrl;

    /**
     * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String ThumbMediaId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMusicURL() {
        return MusicURL;
    }

    public void setMusicURL(String musicURL) {
        MusicURL = musicURL;
    }

    public String getHQMusicUrl() {
        return HQMusicUrl;
    }

    public void setHQMusicUrl(String HQMusicUrl) {
        this.HQMusicUrl = HQMusicUrl;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }
}

class Article{
    /**
     * 图文消息标题
     */
    private String Title;

    /**
     * 图文消息描述
     */
    private String Description;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     *
     */
    private String PicUrl;

    /**
     * 点击图文消息跳转链接
     */
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}

