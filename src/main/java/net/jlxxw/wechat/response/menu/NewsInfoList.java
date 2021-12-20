package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 菜单返回数据
 *
 * @author chunyang.leng
 * @date 2021-12-20 11:01 上午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Querying_Custom_Menus.html">文档地址</a>
 */
public class NewsInfoList {
    /**
     * 图文消息的信息列表
     */
    @JSONField(name = "list")
    private List<NewsInfo> list;

    public List<NewsInfo> getList() {
        return list;
    }

    public void setList(List<NewsInfo> list) {
        this.list = list;
    }
}
