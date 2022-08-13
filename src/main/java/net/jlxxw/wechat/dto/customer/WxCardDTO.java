package net.jlxxw.wechat.dto.customer;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信卡券
 * @author chunyang.leng
 * @date 2022-06-28 3:29 PM
 */
public class WxCardDTO {

    @JSONField(name = "card_id")
    @JsonProperty(value = "card_id")
    private String cardId;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
