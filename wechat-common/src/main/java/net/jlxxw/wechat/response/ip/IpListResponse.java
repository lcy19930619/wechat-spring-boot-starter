package net.jlxxw.wechat.response.ip;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.response.WeChatResponse;

import java.util.List;

public class IpListResponse extends WeChatResponse {

    /**
     * ip 地址列表，注意:并不是ip段
     */
    @JSONField(name = "ip_list")
    @JsonProperty("ip_list")
    private List<String> ipList;

    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }
}
