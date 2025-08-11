package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InnerFooterProduct {

    /**
     * 商品key
     */
    @JSONField(name = "product_key")
    @JsonProperty(value = "product_key")
    private String productKey;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }
}
