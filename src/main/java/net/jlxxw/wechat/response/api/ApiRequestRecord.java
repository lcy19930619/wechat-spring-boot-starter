package net.jlxxw.wechat.response.api;

import net.jlxxw.wechat.response.WeiXinResponse;

/**
 * open api请求接口返回值
 *
 * @author chunyang.leng
 * @date 2021-11-23 3:54 下午
 */
public class ApiRequestRecord extends WeiXinResponse {

    private RequestRecord request;


    public RequestRecord getRequest() {
        return request;
    }

    public void setRequest(RequestRecord request) {
        this.request = request;
    }
}
