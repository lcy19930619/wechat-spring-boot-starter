package net.jlxxw.wechat.response.media;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

/**
 * 上传图文消息图片响应
 *
 * @author chunyang.leng
 * @date 2025-08-11 6:05 PM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/material/permanent/api_uploadimage.html">文档地址</a>
 */
public class UploadImageResponse extends WeChatResponse {

    /**
     * 图片URL
     */
    @JSONField(name = "url")
    @JsonProperty("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}