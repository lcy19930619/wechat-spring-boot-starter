package net.jlxxw.wechat.dto.message.event;

import java.util.List;

/**
 * @author chunyang.leng
 * @date 2021-12-18 4:07 下午
 */
public class SendPicsInfo {

    /**
     * 发送的图片数量
     */
    private Long count;

    /**
     * 图片列表
     */
    private List<PicItem> picList;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<PicItem> getPicList() {
        return picList;
    }

    public void setPicList(List<PicItem> picList) {
        this.picList = picList;
    }
}
