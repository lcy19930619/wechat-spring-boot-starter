package net.jlxxw.wechat.response.auth;

import java.util.List;
import net.jlxxw.wechat.response.user.SubscriptionResponse;

/**
 * 通过微信授权，获取的用户信息
 * @author chunyang.leng
 * @date 2022-08-18 3:58 PM
 */
public class AuthUserInfoResponse extends SubscriptionResponse {

    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
     */
    private List<String> privilege;

    public List<String> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }
}
