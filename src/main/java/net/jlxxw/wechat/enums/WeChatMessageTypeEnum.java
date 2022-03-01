package net.jlxxw.wechat.enums;

import net.jlxxw.wechat.dto.message.*;

/**
 * 微信信息枚举
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:45 上午
 */
public enum WeChatMessageTypeEnum {
    /**
     * 文本
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E6%96%87%E6%9C%AC%E6%B6%88%E6%81%AF">文档地址</a>
     */
    TEXT("text", "文本信息", TextMessage.class),

    /**
     * 图片信息
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF">文档地址</a>
     */
    IMAGE("image", "图片信息", ImageMessage.class),

    /**
     * 音频信息
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E8%AF%AD%E9%9F%B3%E6%B6%88%E6%81%AF">文档地址</a>
     */
    VOICE("voice", "音频信息", VoiceMessage.class),

    /**
     * 视频信息
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF">文档地址</a>
     */
    VIDEO("video", "视频信息", VideoMessage.class),

    /**
     * 短视频信息
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%B0%8F%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF">文档地址</a>
     */
    SHORT_VIDEO("shortvideo", "小视频信息", ShortVideoMessage.class),

    /**
     * 地理位置信息
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE%E6%B6%88%E6%81%AF">文档地址</a>
     */
    LOCATION("location", "地理位置信息", LocationMessage.class),

    /**
     * 链接信息
     *
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E9%93%BE%E6%8E%A5%E6%B6%88%E6%81%AF">文档地址</a>
     */
    LINK("link", "链接信息", LinkMessage.class),
    ;

    /**
     * 消息key
     */
    private final String key;
    /**
     * 描述
     */
    private final String description;

    /**
     * 要转换的消息类型
     */
    private final Class<? extends AbstractWeChatMessage> converClass;

    WeChatMessageTypeEnum(String key, String description, Class<? extends AbstractWeChatMessage> converClass) {
        this.key = key;
        this.description = description;
        this.converClass = converClass;
    }

    /**
     * 获取描述信息
     *
     * @return 该消息类型的描述信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取数据key
     *
     * @return 微信公众号发送过来的数据key
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取待转换 实体参数class 定义
     *
     * @return 消息实体参数class 定义
     */
    public Class<? extends AbstractWeChatMessage> getConverClass() {
        return converClass;
    }
}
