package net.jlxxw.wechat.response.api;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author chunyang.leng
 * @date 2021-11-23 3:52 下午
 */
public class RequestRecord  {
    /**
     * 发起请求的时间戳
     */
    @JSONField(name = "nvoke_time")
    private long nvokeTime	;
    /**
     * 请求毫秒级耗时
     */
    @JSONField(name = "cost_in_ms")
    private long costInMs	;
    /**
     * 请求的URL参数
     */
    @JSONField(name = "request_url")
    private String requestUrl	;
    /**
     * post请求的请求参数
     */
    @JSONField(name = "request_body")
    private String requestBody	;
    /**
     * 接口请求返回参数
     */
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
