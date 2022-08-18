package net.jlxxw.wechat.dto.auth;

/**
 * 获取用户信息，应用授权作用域
 *
 * @author chunyang.leng
 * @date 2022-08-18 2:53 PM
 */
public enum AuthScope {

    /**
     * 不弹出授权页面，直接跳转，只能获取用户openid
     */
    BASE("snsapi_base"),

    /**
     * 弹出授权页面，可通过 openid 拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息
     */
    USER_INFO("snsapi_userinfo");


    private final String code;

    AuthScope(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
