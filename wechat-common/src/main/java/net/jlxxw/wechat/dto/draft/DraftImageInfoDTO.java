package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @author chunyang.leng
 * @date 2022-08-16 14:00
 */
public class DraftImageInfoDTO {
    /**
     * 图片列表
     */
    @JSONField(name = "image_list")
    @JsonProperty(value = "image_list")
    private List<DraftPicItemDTO> picList;

    public List<DraftPicItemDTO> getPicList() {
        return picList;
    }

    public void setPicList(List<DraftPicItemDTO> picList) {
        this.picList = picList;
    }
}