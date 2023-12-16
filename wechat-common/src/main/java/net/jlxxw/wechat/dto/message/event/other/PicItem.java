package net.jlxxw.wechat.dto.message.event.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 图片信息
 *
 * @author chunyang.leng
 * @date 2021-12-18 4:17 下午
 */
public class PicItem {

    @JacksonXmlProperty(localName = "item")
    @JsonProperty(value = "item")
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
