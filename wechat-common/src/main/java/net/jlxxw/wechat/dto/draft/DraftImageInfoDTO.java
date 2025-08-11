package net.jlxxw.wechat.dto.draft;

import java.util.List;

/**
 * 草稿图片信息DTO
 */
public class DraftImageInfoDTO {
    /**
     * 图片列表
     */
    private List<DraftPicItemDTO> picList;

    public List<DraftPicItemDTO> getPicList() {
        return picList;
    }

    public void setPicList(List<DraftPicItemDTO> picList) {
        this.picList = picList;
    }
}