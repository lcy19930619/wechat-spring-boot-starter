package net.jlxxw.wechat.dto.message.event;

import net.jlxxw.wechat.dto.message.AbstractWeChatMessage;

/**
 * 上报地理位置事件
 *
 * @author chunyang.leng
 * @date 2021/1/22 7:24 下午
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#%E4%B8%8A%E6%8A%A5%E5%9C%B0%E7%90%86%E4%BD%8D%E7%BD%AE%E4%BA%8B%E4%BB%B6">文档地址</a>
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/Gets_a_users_location.html">文档地址</a>
 */
public class LocationEventMessage extends AbstractWeChatMessage {

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
