package net.jlxxw.wechat.dto.draft;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CoverInfo {

    /**
     * 封面裁剪信息，。以图片左上角（0,0），右下角（1,1）建立平面坐标系，
     * 经过裁剪后的图片，其左上角所在的坐标填入x1，y1参数，右下角所在的坐标填入x2，y2参数
     */
    @JSONField(name = "crop_percent_list")
    @JsonProperty(value = "crop_percent_list")
    private List<InnerCover> cropPercentList;

    public List<InnerCover> getCropPercentList() {
        return cropPercentList;
    }

    public void setCropPercentList(List<InnerCover> cropPercentList) {
        this.cropPercentList = cropPercentList;
    }
}
