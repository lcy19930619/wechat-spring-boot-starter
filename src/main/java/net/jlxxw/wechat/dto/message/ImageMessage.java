package net.jlxxw.wechat.dto.message;

/**
 * 接收图片消息
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:22 上午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF">文档地址</a>
 */
public class ImageMessage extends AbstractWeiXinMessage {

    /**
     * 图片链接（由微信系统生成）
     */
    private String picUrl;

    /**
     * 图片消息媒体id，可以调用获取临时素材接口拉取数据
     */
    private String mediaId;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
