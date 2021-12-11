package net.jlxxw.component.weixin.dto.message;

/**
 * 文本类型信息
 * @author chunyang.leng
 * @date 2021/1/20 11:18 上午
 */
public class TextMessageAbstract extends AbstractWeiXinMessage {

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
