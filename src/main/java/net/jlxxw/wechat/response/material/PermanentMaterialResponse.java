package net.jlxxw.wechat.response.material;

import net.jlxxw.wechat.response.WeiXinResponse;

/**
 * 临时素材信息返回对象
 *
 * @author chunyang.leng
 * @date 2021-03-05 6:02 下午
 */
public class PermanentMaterialResponse extends WeiXinResponse {
    /**
     * 新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
     */
    private String url;
    /**
     * 媒体文件上传后，获取标识
     */
    private String mediaId;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
