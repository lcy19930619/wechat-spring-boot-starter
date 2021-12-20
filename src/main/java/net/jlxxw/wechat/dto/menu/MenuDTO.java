package net.jlxxw.wechat.dto.menu;

import com.alibaba.fastjson.annotation.JSONField;
import net.jlxxw.wechat.enums.MenuTypeEnum;

import java.util.List;

/**
 * 微信菜单传输对象
 *
 * @author chunyang.leng
 * @date 2021-12-13 2:09 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Creating_Custom-Defined_Menu.html">文档地址</a>
 */
public class MenuDTO {

    /**
     * 必填
     * 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
     *
     * @see MenuTypeEnum
     */
    private MenuTypeEnum type;

    /**
     * 必填
     * 菜单标题，不超过16个字节，子菜单不超过60个字节
     */
    private String name;

    /**
     * click等点击类型必须
     * 菜单KEY值，用于消息接口推送，不超过128字节
     */
    private String key;

    /**
     * view、miniprogram类型必须
     * 网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，不支持小程序的老版本客户端将打开本url。
     */
    private String url;

    /**
     * media_id类型和view_limited类型必须
     * 调用新增永久素材接口返回的合法media_id
     */
    @JSONField(name = "media_id")
    private String mediaId;
    /**
     * miniprogram类型必须
     * 小程序的appid（仅认证公众号可配置）
     */
    private String appid;

    /**
     * miniprogram类型必须
     * 小程序的页面路径
     */
    private String pagepath;

    /**
     * article_id类型和article_view_limited类型必须
     * 发布后获得的合法 article_id
     */
    @JSONField(name = "article_id")
    private String articleId;
    /**
     * 二级菜单数组，个数应为1~5个
     */
    @JSONField(name = "sub_button")
    private List<MenuDTO> subButtonList;

    public MenuTypeEnum getType() {
        return type;
    }

    public void setType(MenuTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public List<MenuDTO> getSubButtonList() {
        return subButtonList;
    }

    public void setSubButtonList(List<MenuDTO> subButtonList) {
        this.subButtonList = subButtonList;
    }
}
