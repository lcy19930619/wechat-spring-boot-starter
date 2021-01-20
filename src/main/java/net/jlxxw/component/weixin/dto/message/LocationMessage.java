package net.jlxxw.component.weixin.dto.message;

/**
 * @author chunyang.leng
 * @date 2021/1/20 11:28 上午
 */
public class LocationMessage extends WeiXinMessage{

    /**
     * 地理位置纬度
     */
    private String Location_X;

    /**
     * 地理位置经度
     */
    private String Location_Y;

    /**
     * 地图缩放大小
     */
    private String Scale;

    /**
     * 地理位置信息
     */
    private String Label;
}
