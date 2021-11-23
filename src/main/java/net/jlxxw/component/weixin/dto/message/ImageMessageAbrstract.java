package net.jlxxw.component.weixin.dto.message;

/**
 * @author chunyang.leng
 * @date 2021/1/20 11:22 上午
 */
public class ImageMessageAbrstract extends AbrstractWeiXinMessage {

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
