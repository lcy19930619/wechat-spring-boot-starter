package net.jlxxw.wechat.enums;

/**
 * @author chunyang.leng
 * @date 2021-03-05 6:25 下午
 */
public enum MaterialEnum {

    /**
     * 图片（image）: 10M，支持PNG\JPEG\JPG\GIF格式
     */
    IMAGE("图片（image）: 10M，支持PNG\\JPEG\\JPG\\GIF格式"),
    /**
     * 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
     */
    VOICE("语音（voice）：2M，播放长度不超过60s，支持AMR\\MP3格式"),
    /**
     * 视频（video）：10MB，支持MP4格式
     */
    VIDEO("视频（video）：10MB，支持MP4格式"),
    /**
     * 缩略图（thumb）：64KB，支持JPG格式
     */
    THUMB("缩略图（thumb）：64KB，支持JPG格式");

    /**
     * 描述信息
     */
    private final String desc;

    MaterialEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
