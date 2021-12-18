package net.jlxxw.wechat.dto.qrcode;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 临时二维码创建结果
 *
 * @author chunyang.leng
 * @date 2021-11-23 11:32 上午
 */
public class TempQrCodeDTO extends QrCodeDTO {
    /**
     * 过期时间
     */
    @JSONField(name = "expire_seconds")
    private Long expireSeconds;

    public Long getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Long expireSeconds) {
        this.expireSeconds = expireSeconds;
    }
}
