package net.jlxxw.component.weixin.dto.qrcode;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 创建二维码使用的数据传输对象
 * @author chunyang.leng
 * @date 2021-06-04 7:22 下午
 */
public class QrCodeDTO {
    /**
     * 二维码类型
     */
    @JSONField(name = "action_name")
    private String actionName;
    /**
     * 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     */
    @JSONField(name = "expire_seconds")
    private Integer expireSeconds;

    /**
     * 二维码详细信息
     */
    @JSONField(name = "action_info")
    private String actionInfo;

    /**
     * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     */
    @JSONField(name = "scene_id")
    private Integer sceneId;

    /**
     * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     */
    @JSONField(name = "scene_str")
    private String sceneStr;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getActionInfo() {
        return actionInfo;
    }

    public void setActionInfo(String actionInfo) {
        this.actionInfo = actionInfo;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneStr() {
        return sceneStr;
    }

    public void setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
    }
}
