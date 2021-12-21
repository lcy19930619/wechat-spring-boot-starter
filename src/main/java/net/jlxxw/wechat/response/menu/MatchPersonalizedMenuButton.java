package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;
import net.jlxxw.wechat.response.WeChatResponse;

import java.util.List;

/**
 * 个性化菜单匹配结果
 * @author chunyang.leng
 * @date 2021-12-20 4:18 下午
 */
public class MatchPersonalizedMenuButton extends WeChatResponse {
    /**
     * 菜单名称
     */
    @JSONField(name = "name")
    private String name;

    @JSONField(name = "sub_button")
    private List<MatchPersonalizedMenuButton> subButton;

    /**
     * 菜单的类型，公众平台官网上能够设置的菜单类型
     * 有view（跳转网页）、text（返回文本，下同）、img、photo、video、voice。
     * 使用API设置的则有8种，详见《自定义菜单创建接口》
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 对于不同的菜单类型，value的值意义不同。
     * 官网上设置的自定义菜单：
     * Text:保存文字到value；
     * Img、voice：保存mediaID到value；
     * Video：保存视频下载链接到value；
     * News：保存图文消息到news_info，同时保存mediaID到value；
     * View：保存链接到url。
     * <br/>
     * 使用API设置的自定义菜单：
     * click、scancode_push、scancode_waitmsg、pic_sysphoto、pic_photo_or_album、 pic_weixin、location_select：保存值到key；view：保存链接到url
     */
    @JSONField(name = "value")
    private String value;

    /**
     * 对于不同的菜单类型，value的值意义不同。
     * 官网上设置的自定义菜单：
     * Text:保存文字到value；
     * Img、voice：保存mediaID到value；
     * Video：保存视频下载链接到value；
     * News：保存图文消息到news_info，同时保存mediaID到value；
     * View：保存链接到url。
     * <br/>
     * 使用API设置的自定义菜单：
     * click、scancode_push、scancode_waitmsg、pic_sysphoto、pic_photo_or_album、 pic_weixin、location_select：保存值到key；view：保存链接到url
     */
    @JSONField(name = "key")
    private String key;

    /**
     * 对于不同的菜单类型，value的值意义不同。
     * 官网上设置的自定义菜单：
     * Text:保存文字到value；
     * Img、voice：保存mediaID到value；
     * Video：保存视频下载链接到value；
     * News：保存图文消息到news_info，同时保存mediaID到value；
     * View：保存链接到url。
     * <br/>
     * 使用API设置的自定义菜单：
     * click、scancode_push、scancode_waitmsg、pic_sysphoto、pic_photo_or_album、 pic_weixin、location_select：保存值到key；view：保存链接到url
     */
    @JSONField(name = "url")
    private String url;

    /**
     * 图文消息的信息
     */
    @JSONField(name = "news_info")
    private NewsInfoList newsInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MatchPersonalizedMenuButton> getSubButton() {
        return subButton;
    }

    public void setSubButton(List<MatchPersonalizedMenuButton> subButton) {
        this.subButton = subButton;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NewsInfoList getNewsInfo() {
        return newsInfo;
    }

    public void setNewsInfo(NewsInfoList newsInfo) {
        this.newsInfo = newsInfo;
    }
}
