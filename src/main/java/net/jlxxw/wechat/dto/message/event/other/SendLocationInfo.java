package net.jlxxw.wechat.dto.message.event.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;

/**
 * 发送的位置信息
 * @author chunyang.leng
 * @date 2021-12-19 6:13 下午
 */
public class SendLocationInfo extends AbstractWeiXinMessage {


    /**
     * 地理位置纬度,X坐标信息
     */
    @JacksonXmlProperty(localName = "Location_X")
    @JsonProperty(value = "Location_X")
    private String locationX;

    /**
     * 地理位置经度,Y坐标信息
     */
    @JacksonXmlProperty(localName = "Location_Y")
    @JsonProperty(value = "Location_Y")
    private String locationY;

    /**
     * 精度，可理解为精度或者比例尺、越精细的话 scale越高
     */
    private String scale;

    /**
     * 地理位置的字符串信息
     */
    private String label;

    /**
     * 朋友圈POI的名字，可能为空
     */
    private String poiname;

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPoiname() {
        return poiname;
    }

    public void setPoiname(String poiname) {
        this.poiname = poiname;
    }
}
