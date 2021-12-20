package net.jlxxw.wechat.dto.message;

/**
 * 小视频信息
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:26 上午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%B0%8F%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF">文档地址</a>
 */
public class ShortVideoMessage extends AbstractWeChatMessage {
    /**
     * 视频消息媒体id，可以调用获取临时素材接口拉取数据。
     */
    private String mediaId;

    /**
     * 视频消息缩略图的媒体id，可以调用获取临时素材接口拉取数据。
     */
    private String thumbMediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
