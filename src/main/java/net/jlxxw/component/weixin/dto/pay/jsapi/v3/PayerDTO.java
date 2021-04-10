package net.jlxxw.component.weixin.dto.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 支付者信息
 *
 * @author chunyang.leng
 * @date 2021-04-08 4:12 下午
 */
public class PayerDTO {
    /**
     * 用户服务标识
     * 用户在服务商appid下的唯一标识。
     * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     * <p>
     * 长度：1-128
     * 必填： 两个属性二选一
     */
    @JSONField(name = "sp_openid")
    private String spOpenid;

    /**
     * 用户在子商户appid下的唯一标识。若传sub_openid，那sub_appid必填
     * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     * <p>
     * 长度：1-128
     * 必填： 两个属性二选一
     */
    @JSONField(name = "sub_openid")
    private String subOpenid;

    public String getSpOpenid() {
        return spOpenid;
    }

    public void setSpOpenid(String spOpenid) {
        this.spOpenid = spOpenid;
    }

    public String getSubOpenid() {
        return subOpenid;
    }

    public void setSubOpenid(String subOpenid) {
        this.subOpenid = subOpenid;
    }
}
