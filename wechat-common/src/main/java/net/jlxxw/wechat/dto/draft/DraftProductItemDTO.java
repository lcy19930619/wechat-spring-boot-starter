package net.jlxxw.wechat.dto.draft;

/**
 * 草稿商品项DTO
 */
public class DraftProductItemDTO {
    /**
     * 商品id
     */
    private String productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品价格
     */
    private String price;
    
    /**
     * 商品图片url
     */
    private String picUrl;
    
    /**
     * 商品跳转链接
     */
    private String productUrl;
    
    /**
     * 商品描述
     */
    private String productDesc;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}