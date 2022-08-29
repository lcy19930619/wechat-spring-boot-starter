package net.jlxxw.wechat.response.tag;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2022-08-29 11:30 AM
 */
public class User {

    @JSONField(name = "openid")
    @JsonProperty("openid")
    private List<String> openId;

    public List<String> getOpenId() {
        return openId;
    }

    public void setOpenId(List<String> openId) {
        this.openId = openId;
    }
}
