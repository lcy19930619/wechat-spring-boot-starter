package net.jlxxw.wechat.dto.template;

import net.jlxxw.wechat.enums.Color;

import java.util.Objects;

/**
 * 模板数据
 * @author chunyang.leng
 * @date 2021/1/18 9:45 下午
 */
public class TemplateData {

    private String value;
    private String color;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static TemplateData build(String value, Color color) {
        TemplateData templateData = new TemplateData();
        templateData.setValue(value);
        templateData.setColor(Objects.isNull(color) ? null : color.getColorValue());
        return templateData;
    }
}
