package net.jlxxw.wechat.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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
public class WeChatMessageResponse {
    /**
     * 接收方帐号（收到的OpenID）
     */
    @JacksonXmlProperty
    private String toUserName;

    /**
     * 开发者微信号
     */
    @JacksonXmlProperty
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private Long createTime;

    /**
     * 消息类型，
     * 文本为text
     */
    @JacksonXmlProperty
    private String msgType;

    /**
     * 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
     */
    @JacksonXmlProperty
    private String content;

    /**
     * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    private Integer articleCount;

    /**
     * 图文
     */
    @JacksonXmlProperty(localName = "Articles")
    private List<Article> articles;
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

    /**
     * 客服接口数据
     */
    private TransInfo transInfo;

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
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
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

    public TransInfo getTransInfo() {
        return transInfo;
    }

    public void setTransInfo(TransInfo transInfo) {
        this.transInfo = transInfo;
    }

    /**
     * 回复文本消息
     *
     * @param content 回复的文件内容
     * @return
     */
    public static WeChatMessageResponse buildText(String content) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("text");
        weChatMessageResponse.setContent(content);
        return weChatMessageResponse;
    }

    /**
     * 回复图片消息
     *
     * @param mediaId 通过素材管理中的接口上传多媒体文件，得到的id。
     * @return
     */
    public static WeChatMessageResponse buildImage(String mediaId) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("image");
        Image image = new Image();
        image.setMediaId(mediaId);
        weChatMessageResponse.setImage(image);
        return weChatMessageResponse;
    }

    /**
     * 回复语音消息
     *
     * @param mediaId 通过素材管理中的接口上传多媒体文件，得到的id。
     * @return
     */
    public static WeChatMessageResponse buildVoice(String mediaId) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("voice");
        Voice voice = new Voice();
        voice.setMediaId(mediaId);
        weChatMessageResponse.setVoice(voice);
        return weChatMessageResponse;
    }

    /**
     * 回复视频消息
     *
     * @param mediaId     通过素材管理中的接口上传多媒体文件，得到的id
     * @param title       视频消息的标题
     * @param description 视频消息的描述
     * @return
     */
    public static WeChatMessageResponse buildVideo(String mediaId, String title, String description) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("video");
        Video video = new Video();
        video.setMediaId(mediaId);
        video.setTitle(title);
        video.setDescription(description);
        weChatMessageResponse.setVideo(video);
        return weChatMessageResponse;
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
    public static WeChatMessageResponse buildMusic(String title, String description, String musicUrl, String hqMusicUrl, String thumbMediaId) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("music");
        Music music = new Music();
        music.setTitle(title);
        music.setDescription(description);
        music.setMusicUrl(musicUrl);
        music.setHqMusicUrl(hqMusicUrl);
        music.setThumbMediaId(thumbMediaId);
        weChatMessageResponse.setMusic(music);
        return weChatMessageResponse;
    }

    /**
     * 回复图文消息
     *
     * @param articles 回复的图文消息
     * @return
     */
    public static WeChatMessageResponse buildArticle(Article... articles) {
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("news");
        weChatMessageResponse.setArticleCount(articles.length);
        weChatMessageResponse.setArticles(Arrays.asList(articles));
        return weChatMessageResponse;
    }

    /**
     * 转发到客服系统
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Customer_Service/Forwarding_of_messages_to_service_center.html">文档地址</a>
     * @return
     */
    public static WeChatMessageResponse buildTransferCustomerService(){
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("transfer_customer_service");
        return weChatMessageResponse;
    }

    /**
     * 转发到客服系统，并指定客服提供服务
     * @param kfAccount 客服账号,完整客服帐号，格式为：帐号前缀@公众号微信号
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Customer_Service/Forwarding_of_messages_to_service_center.html">文档地址</a>
     * @return
     */
    public static WeChatMessageResponse buildTransferCustomerService(String kfAccount){
        WeChatMessageResponse weChatMessageResponse = new WeChatMessageResponse();
        weChatMessageResponse.setMsgType("transfer_customer_service");
        TransInfo transInfo = new TransInfo();
        transInfo.setKfAccount(kfAccount);
        weChatMessageResponse.setTransInfo(transInfo);
        return weChatMessageResponse;
    }


    public static class Image {
        /**
         * 通过素材管理中的接口上传多媒体文件，得到的id。
         */
        @JacksonXmlProperty
        private String mediaId;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }
    }

    public static class Voice {
        /**
         * 通过素材管理中的接口上传多媒体文件，得到的id。
         */
        @JacksonXmlProperty
        private String mediaId;

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }
    }

    public static class Video {
        /**
         * 通过素材管理中的接口上传多媒体文件，得到的id。
         */
        @JacksonXmlProperty
        private String mediaId;
        /**
         * 视频消息的标题
         */
        @JacksonXmlProperty
        private String title;
        /**
         * 视频消息的描述
         */
        @JacksonXmlProperty
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

    public static class Music {
        /**
         * 音乐标题
         */
        @JacksonXmlProperty
        private String title;
        /**
         * 音乐描述
         */
        @JacksonXmlProperty
        private String description;

        /**
         * 音乐链接
         */
        @JacksonXmlProperty(localName = "MusicUrl")
        private String musicUrl;

        /**
         * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
         */
        @JacksonXmlProperty(localName = "HQMusicUrl")
        private String hqMusicUrl;

        /**
         * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
         */
        @JacksonXmlProperty
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

    public static class Article {
        /**
         * 图文消息标题
         */
        @JacksonXmlProperty
        private String title;

        /**
         * 图文消息描述
         */
        @JacksonXmlProperty
        private String description;

        /**
         * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
         */
        @JacksonXmlProperty
        private String picUrl;

        /**
         * 点击图文消息跳转链接
         */
        @JacksonXmlProperty
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

    /**
     * 转发至客服系统
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Customer_Service/Forwarding_of_messages_to_service_center.html">文档地址</a>
     */
    public static class TransInfo{
        /**
         * 指定会话接入的客服账号
         * 完整客服帐号，格式为：帐号前缀@公众号微信号
         */
        private String kfAccount;

        public String getKfAccount() {
            return kfAccount;
        }

        public void setKfAccount(String kfAccount) {
            this.kfAccount = kfAccount;
        }
    }
}


