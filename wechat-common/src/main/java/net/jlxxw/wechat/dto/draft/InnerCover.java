package net.jlxxw.wechat.dto.draft;

public class InnerCover {
    /**
     * 裁剪比例，支持：“1_1”，“16_9”,“2.35_1”
     */
    private String ratio;
    /**
     * 以图片左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其左上角所在的坐标填入x1，y1参数
     */
    private String x1;
    /**
     * 以图片左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其左上角所在的坐标填入x1，y1参数
     */
    private String y1;
    /**
     * 以图片左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其右下角所在的坐标填入x2，y2参数
     */
    private String x2;
    /**
     * 以图片左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其右下角所在的坐标填入x2，y2参数
     */
    private String y2;

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getY1() {
        return y1;
    }

    public void setY1(String y1) {
        this.y1 = y1;
    }

    public String getX2() {
        return x2;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }

    public String getY2() {
        return y2;
    }

    public void setY2(String y2) {
        this.y2 = y2;
    }
}
