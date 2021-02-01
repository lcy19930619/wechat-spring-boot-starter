package net.jlxxw.component.weixin.dto.message;

/**
 * @author chunyang.leng
 * @date 2021/1/20 11:28 上午
 */
public class LocationMessage extends WeiXinMessage{

    /**
     * 地理位置纬度
     */
    private String location_X;

    /**
     * 地理位置经度
     */
	private String location_Y;

    /**
     * 地图缩放大小
     */
    private String scale;

    /**
     * 地理位置信息
     */
    private String label;

	public String getLocation_X() {
		return location_X;
	}

	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}

	public String getLocation_Y() {
		return location_Y;
	}

	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
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
