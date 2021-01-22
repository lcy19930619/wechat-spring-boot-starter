package net.jlxxw.component.weixin.enums;

/**
 * 微信信息枚举
 * @author chunyang.leng
 * @date 2021/1/20 11:45 上午
 */
public enum WeiXinMessageTypeEnum {
    /**
     * 文本
     */
    TEXT("文本信息"),
    /**
     * 图片
     */
    IMAGE("图片信息"),
    /**
     * 音频
     */
    VOICE("音频信息"),
    /**
     * 视频
     */
    VIDEO("视频信息"),
    /**
     * 小视频
     */
    SHORT_VIDEO("小视频信息"),
    /**
     * 地理位置信息
     */
    LOCATION("地理位置信息"),
    /**
     * 链接
     */
    LINK("链接信息"),
    ;

    /**
     * 描述
     */
    private final String description;

    WeiXinMessageTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
