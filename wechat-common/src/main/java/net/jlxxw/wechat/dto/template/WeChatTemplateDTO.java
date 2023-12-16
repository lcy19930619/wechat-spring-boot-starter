package net.jlxxw.wechat.dto.template;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.jlxxw.wechat.aop.check.group.Insert;
import net.jlxxw.wechat.enums.Color;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2021/1/18 9:45 下午
 */
public class WeChatTemplateDTO {
    /**
     * 微信模版id
     */
    @NotBlank(groups = Insert.class, message = "模版id不能为空")
    @JSONField(name = "template_id")
    @JsonProperty("template_id")
    private String templateId;

    @NotBlank(groups = Insert.class, message = "用户openId不能为空")
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
    public WeChatTemplateDTO buildUrl(String url) {
        this.setUrl(url);
        return this;
    }

    /**
     * 构建接收用户
     *
     * @param openId openId
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildToUser(String openId) {
        this.setTouser(openId);
        return this;
    }

    /**
     * 构建模版编号，微信的模版id
     *
     * @param templateCode 微信的模版id
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildTemplateCode(String templateCode) {
        this.setTemplateId(templateCode);
        return this;
    }

    /**
     * 构建 first 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     * @deprecated 微信会在近期（2023.05.04 移除first 内容）
     */
    @Deprecated
    public WeChatTemplateDTO buildFirstData(String value, Color color) {
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
     * @deprecated 微信会在近期（2023.05.04 移除自定义颜色功能）
     */
    @Deprecated
    public WeChatTemplateDTO buildKeyWord1Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword1", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword1 节点的数据
     *
     * @param value 数据值
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildKeyWord1Data(String value) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword1", TemplateData.build(value, null));
        return this;
    }

    /**
     * 构建 keyword2 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     * @deprecated 微信会在近期（2023.05.04 移除自定义颜色功能）
     */
    @Deprecated
    public WeChatTemplateDTO buildKeyWord2Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword2", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword2 节点的数据
     *
     * @param value 数据值
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildKeyWord2Data(String value) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword2", TemplateData.build(value, null));
        return this;
    }

    /**
     * 构建 keyword3 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     * @deprecated 微信会在近期（2023.05.04 移除自定义颜色功能）
     */
    @Deprecated
    public WeChatTemplateDTO buildKeyWord3Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword3", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword3 节点的数据
     *
     * @param value 数据值
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildKeyWord3Data(String value) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword3", TemplateData.build(value, null));
        return this;
    }

    /**
     * 构建 keyword4 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     * @deprecated 微信会在近期（2023.05.04 移除自定义颜色功能）
     */
    @Deprecated
    public WeChatTemplateDTO buildKeyWord4Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword4", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword4 节点的数据
     *
     * @param value 数据值
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildKeyWord4Data(String value) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword4", TemplateData.build(value, null));
        return this;
    }

    /**
     * 构建 keyword5 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     * @deprecated 微信会在近期（2023.05.04 移除自定义颜色功能）
     */
    @Deprecated
    public WeChatTemplateDTO buildKeyWord5Data(String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword5", TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 keyword5 节点的数据
     *
     * @param value 数据值
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildKeyWord5Data(String value) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put("keyword5", TemplateData.build(value, null));
        return this;
    }

    /**
     * 构建 remark 节点的数据
     *
     * @param value 数据值
     * @param color 颜色信息
     * @return 当前操作的模版对象
     * @deprecated 微信会在近期（2023.05.04 移除remark 内容）
     */
    @Deprecated
    public WeChatTemplateDTO buildRemarkData(String value, Color color) {
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
     * @deprecated 微信会在近期（2023.05.04 移除自定义颜色功能）
     */
    @Deprecated
    public WeChatTemplateDTO buildOtherData(String key, String value, Color color) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put(key, TemplateData.build(value, color));
        return this;
    }

    /**
     * 构建 自定义 节点的数据
     *
     * @param value 数据值
     * @return 当前操作的模版对象
     */
    public WeChatTemplateDTO buildOtherData(String key, String value) {
        if (Objects.isNull(this.getData())) {
            this.setData(new HashMap<>(16));
        }
        this.getData().put(key, TemplateData.build(value, null));
        return this;
    }
}