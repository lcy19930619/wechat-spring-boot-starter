package net.jlxxw.wechat.response.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author chunyang.leng
 * @date 2021-11-23 3:52 下午
 */
public class RequestRecord {
    /**
     * 发起请求的时间戳
     */
    @JsonProperty("nvoke_time")
    @JSONField(name = "nvoke_time")
    private long nvokeTime;

    /**
     * 请求毫秒级耗时
     */
    @JsonProperty("cost_in_ms")
    @JSONField(name = "cost_in_ms")
    private long costInMs;

    /**
     * 请求的URL参数
     */
    @JsonProperty("request_url")
    @JSONField(name = "request_url")
    private String requestUrl;

    /**
     * post请求的请求参数
     */
    @JsonProperty("request_body")
    @JSONField(name = "request_body")
    private String requestBody;

    /**
     * 接口请求返回参数
     */
    @JsonProperty("response_body")
    @JSONField(name = "response_body")
    private String responseBody;

    public long getNvokeTime() {
        return nvokeTime;
    }

    public void setNvokeTime(long nvokeTime) {
        this.nvokeTime = nvokeTime;
    }

    public long getCostInMs() {
        return costInMs;
    }

    public void setCostInMs(long costInMs) {
        this.costInMs = costInMs;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
