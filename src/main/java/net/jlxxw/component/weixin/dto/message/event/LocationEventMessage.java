package net.jlxxw.component.weixin.dto.message.event;

import net.jlxxw.component.weixin.dto.message.WeiXinMessage;

/**
 * 上报地理位置事件
 * @author chunyang.leng
 * @date 2021/1/22 7:24 下午
 */
public class LocationEventMessage extends WeiXinMessage {

    /**
     * 地理位置纬度
     */
    private Double Latitude;

    /**
     * 地理位置经度
     */
    private Double Longitude;

    /**
     * 地理位置精度
     */
    private Double Precision;

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getPrecision() {
        return Precision;
    }

    public void setPrecision(Double precision) {
        Precision = precision;
    }
}
