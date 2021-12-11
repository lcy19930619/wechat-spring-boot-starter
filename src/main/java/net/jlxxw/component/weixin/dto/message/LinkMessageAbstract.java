package net.jlxxw.component.weixin.dto.message;

/**
 * 链接消息
 * @author chunyang.leng
 * @date 2021/1/20 11:29 上午
 */
public class LinkMessageAbstract extends AbstractWeiXinMessage {

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息描述
     */
    private String description;

    /**
     * 消息链接
     */
    private String url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
