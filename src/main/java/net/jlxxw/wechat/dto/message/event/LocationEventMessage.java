package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeiXinMessage;

/**
 * 上报地理位置事件
 * @author chunyang.leng
 * @date 2021/1/22 7:24 下午
 */
public class LocationEventMessage extends AbstractWeiXinMessage {

    /**
     * 地理位置纬度
     */
    private Double latitude;

    /**
     * 地理位置经度
     */
    private Double longitude;

    /**
     * 地理位置精度
     */
    private Double precision;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}
}
