package net.jlxxw.wechat.dto.draft;

import java.util.List;

/**
 * 草稿商品信息DTO
 */
public class DraftProductInfoDTO {
    /**
     * 商品列表
     */
    private List<DraftProductItemDTO> productList;

    public List<DraftProductItemDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<DraftProductItemDTO> productList) {
        this.productList = productList;
    }
}