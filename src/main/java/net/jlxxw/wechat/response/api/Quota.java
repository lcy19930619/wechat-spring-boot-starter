package net.jlxxw.wechat.response.api;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author chunyang.leng
 * @date 2021-11-23 3:40 下午
 */
public class Quota {
    /**
     * 当天该账号可调用该接口的次数
     */
    @JSONField(name = "daily_limit")
    private Integer dailyLimit;
    /**
     * 当天已经调用的次数
     */
    private Integer used;
    /**
     * 当天剩余调用次数
     */
    private Integer remain;

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }


}
