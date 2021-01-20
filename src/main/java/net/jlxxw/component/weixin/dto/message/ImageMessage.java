package net.jlxxw.component.weixin.dto.message;

/**
 * @author chunyang.leng
 * @date 2021/1/20 11:22 上午
 */
public class ImageMessage extends WeiXinMessage{

    /**
     * 图片链接（由微信系统生成）
     */
    private String PicUrl;

    /**
     * 图片消息媒体id，可以调用获取临时素材接口拉取数据
     */
    private String MediaId;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
