package net.jlxxw.component.weixin.vo;

/**
 * 临时素材信息返回对象
 *
 * @author chunyang.leng
 * @date 2021-03-05 6:02 下午
 */
public class TempMaterialVO {
    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图）
     */
    private String type;
    /**
     * 媒体文件上传后，获取标识
     */
    private String mediaId;
    /**
     * 媒体文件上传时间戳
     */
    private Long createdAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
