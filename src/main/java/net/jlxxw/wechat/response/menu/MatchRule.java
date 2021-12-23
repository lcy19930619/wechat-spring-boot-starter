package net.jlxxw.wechat.response.menu;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author chunyang.leng
 * @date 2021-12-20 7:11 下午
 */
public class MatchRule {
    @JSONField(name = "group_id")
    private Integer groupId;
    @JSONField(name = "sex")
    private Integer sex;
    @JSONField(name = "country")
    private String country;
    @JSONField(name = "province")
    private String province;
    @JSONField(name = "city")
    private String city;
    @JSONField(name = "client_platform_type")
    private Integer clientPlatformType;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getClientPlatformType() {
        return clientPlatformType;
    }

    public void setClientPlatformType(Integer clientPlatformType) {
        this.clientPlatformType = clientPlatformType;
    }
}
