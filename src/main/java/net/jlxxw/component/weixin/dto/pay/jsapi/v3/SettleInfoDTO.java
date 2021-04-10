package net.jlxxw.component.weixin.dto.pay.jsapi.v3;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 结算信息
 *
 * @author chunyang.leng
 * @date 2021-04-08 4:05 下午
 */
public class SettleInfoDTO {
    /**
     * 是否指定分账
     * true：是
     * false：否
     */
    @JSONField(name = "profit_sharing")
    private Boolean profitSharing;

    public Boolean getProfitSharing() {
        return profitSharing;
    }

    public void setProfitSharing(Boolean profitSharing) {
        this.profitSharing = profitSharing;
    }
}
