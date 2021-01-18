package net.jlxxw.component.weixin.dto.template;

import net.jlxxw.component.weixin.enums.Color;

import java.util.Objects;

/**
 * @author DJL
 * @ClassName: TemplateData
 * @Description: 模板数据
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

    public static TemplateData build(String value, Color color){
        TemplateData templateData = new TemplateData();
        templateData.setValue(value);
        templateData.setColor(Objects.isNull(color)?null:color.getColorValue());
        return templateData;
    }
}
