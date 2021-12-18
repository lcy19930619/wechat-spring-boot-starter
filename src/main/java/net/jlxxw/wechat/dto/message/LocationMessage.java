package net.jlxxw.wechat.dto.message;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * 地理位置信息
 * @link https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html#%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE%E6%B6%88%E6%81%AF
 * @author chunyang.leng
 * @date 2021/1/20 11:28 上午
 */
public class LocationMessage extends AbstractWeiXinMessage {

    /**
     * 地理位置纬度
     */
    @JacksonXmlProperty(localName = "Location_X")
    private String locationX;

    /**
     * 地理位置经度
     */
    @JacksonXmlProperty(localName = "Location_Y")
	private String locationY;

    /**
     * 地图缩放大小
     */
    private String scale;

    /**
     * 地理位置信息
     */
    private String label;

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
}
