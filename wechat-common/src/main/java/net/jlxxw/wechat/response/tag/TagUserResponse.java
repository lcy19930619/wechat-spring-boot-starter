package net.jlxxw.wechat.response.tag;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 根据标签获取的用户信息列表
 * @author chunyang.leng
 * @date 2022-08-29 11:28 AM
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/User_Tag_Management.html">文档地址</a>
 *
 */
public class TagUserResponse extends WeChatResponse {
    /**
     * 这次获取的粉丝数量
     */
    private Integer count;
    /**
     * 粉丝列表
     */
    private User data;

    /**
     * 拉取列表最后一个用户的openid
     */
    @JSONField(name = "next_openid")
    @JsonProperty("next_openid")
    private String nextOpenId;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }
}
