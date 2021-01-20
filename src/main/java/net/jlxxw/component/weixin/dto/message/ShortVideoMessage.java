package net.jlxxw.component.weixin.dto.message;

/**
 * 小视频信息
 * @author chunyang.leng
 * @date 2021/1/20 11:26 上午
 */
public class ShortVideoMessage extends WeiXinMessage{
    /**
     *视频消息媒体id，可以调用获取临时素材接口拉取数据。
     */
    private String MediaId;

    /**
     *视频消息缩略图的媒体id，可以调用获取临时素材接口拉取数据。
     */
    private String ThumbMediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }
}
