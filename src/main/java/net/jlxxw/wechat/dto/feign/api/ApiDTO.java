package net.jlxxw.wechat.dto.feign.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chunyang.leng
 * @date 2022-04-14 11:21 AM
 */
public class ApiDTO {

    @JSONField(name = "appid")
    @JsonProperty(value = "appid")
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
