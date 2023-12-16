package net.jlxxw.wechat.response.tag;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 标签应答对象
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/User_Tag_Management.html">文档地址</a>
 * @author chunyang.leng
 * @date 2022-08-29 10:57 AM
 */
public class TagResponse extends WeChatResponse {

    @JSONField(name = "tag")
    @JsonProperty("tag")
    private Tag tag;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
