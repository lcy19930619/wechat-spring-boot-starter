package net.jlxxw.component.weixin.dto.message;

/**
 * 链接消息
 * @author chunyang.leng
 * @date 2021/1/20 11:29 上午
 */
public class LinkMessage extends WeiXinMessage{

    /**
     * 消息标题
     */
    private String Title;

    /**
     * 消息描述
     */
    private String Description;

    /**
     * 消息链接
     */
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
