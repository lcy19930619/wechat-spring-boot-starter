package net.jlxxw.wechat.response.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 支付者信息
 *
 * @author chunyang.leng
 * @date 2023-03-31 18:24
 * @see <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml">文档地址</a>
 */

public class Payer {
    /**
     * 用户标识
     * 用户在直连商户appid下的唯一标识。
     * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     * 长度 1-128
     */
    @JSONField(name = "openid")
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
