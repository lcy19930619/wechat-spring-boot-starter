package net.jlxxw.wechat.dto.feign.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chunyang.leng
 * @date 2022-04-14 11:34 AM
 */
public class ApiQuotaDTO extends ApiDTO{
    /**
     *  api的请求地址，例如"/cgi-bin/message/custom/send";不要前缀“https://api.weixin.qq.com” ，也不要漏了"/",否则都会76003的报错
     */
    @JSONField(name = "cgi_path")
    @JsonProperty(value = "cgi_path")
    private String cgiPath;

    public String getCgiPath() {
        return cgiPath;
    }

    public void setCgiPath(String cgiPath) {
        this.cgiPath = cgiPath;
    }
}
