package net.jlxxw.wechat.dto.message;

/**
 * 文本类型信息
 *
 * @author chunyang.leng
 * @date 2021/1/20 11:18 上午
 */
public class TextMessage extends AbstractWeChatMessage {

    /**
     * 文本消息内容
     */
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
