package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ProductInfo {

    /**
     * 文末插入商品相关信息
     */
    @JSONField(name = "footer_product_info")
    @JsonProperty(value = "footer_product_info")
    private InnerFooterProduct  footerProductInfo;

    public InnerFooterProduct getFooterProductInfo() {
        return footerProductInfo;
    }

    public void setFooterProductInfo(InnerFooterProduct footerProductInfo) {
        this.footerProductInfo = footerProductInfo;
    }
}
