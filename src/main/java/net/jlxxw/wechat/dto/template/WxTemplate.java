package net.jlxxw.wechat.dto.template;


import com.alibaba.fastjson.annotation.JSONField;
import net.jlxxw.wechat.enums.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2021/1/18 9:45 下午
 */
public class WxTemplate {
    /**
     * 微信模版id
     */
    @JSONField(name = "template_id")
    private String templateId;
    private String touser;
    private String url;
    private Map<String, TemplateData> data;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }

    /**
     * 构建模版跳转url
     *
     * @param url 跳转的url
     * @return 当前操作的模版对象
     */
    public WxTemplate buildUrl(String url) {
        this.setUrl(url);
        return this;
    }

    /**
     * 构建接收用户
     *
     * @param openId openId
     * @return 当前操作的模版对象
     */
    public WxTemplate buildToUser(String openId) {
        this.setTouser(openId);
        return this;
    }

    /**
     * 构建模版编号，微信的模版id
     *
     * @param templateCode 微信的模版id
     * @return 当前操作的模版对象
     */
    public WxTemplate buildTemplateCode(String templateCode) {
        this.setTemplateId(templateCode);
        return this;
    }

    /**
     * 构建 first 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildFirstData(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("first", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword1 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildKeyWord1Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword1", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword2 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildKeyWord2Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword2", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword3 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildKeyWord3Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword3", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword4 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildKeyWord4Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword4", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword5 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildKeyWord5Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword5", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 remark 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildRemarkData(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("remark", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 自定义 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     */
    public WxTemplate buildOtherData(String key, String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put(key, TemplateData.build(value, color));
        return this;
    }
}