package net.jlxxw.wechat.dto.draft;

/**
 * 草稿图片项DTO
 */
public class DraftPicItemDTO {
    /**
     * 图片的md5值
     */
    private String picMd5;

    /**
     * 图片的url
     */
    private String picUrl;

    public String getPicMd5() {
        return picMd5;
    }

    public void setPicMd5(String picMd5) {
        this.picMd5 = picMd5;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}