package net.jlxxw.wechat.enums;

/**
 * 微信模板消息颜色枚举
 * @author chunyang.leng
 * @date 2020/11/17 16:58
 */
public enum ColorEnums implements Color{
    /**
     * 红色
     */
    RED("#e94a3a"),
    /**
     * 蓝色
     */
    BLUE("#2084f3"),

    /**
     * 橙色
     */
    ORANGE("#ff8a00"),
    ;

    /**
     * 颜色的16进制编码
     */
    private final String color;

    ColorEnums(String color) {
        this.color = color;
    }

    @Override
    public String getColorValue() {
        return color;
    }
}
