package net.jlxxw.component.weixin.dto.message;

/**
 * 文本类型信息
 * @author chunyang.leng
 * @date 2021/1/20 11:18 上午
 */
public class TextMessage extends WeiXinMessage{

    /**
     * 文本消息内容
     */
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
