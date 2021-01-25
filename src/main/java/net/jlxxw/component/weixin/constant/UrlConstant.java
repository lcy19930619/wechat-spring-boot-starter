package net.jlxxw.component.weixin.constant;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:17 下午
 */
public interface UrlConstant {


    /**
     * 模板推送地址前缀
     */
    String PUSH_TEMPLATE_PREFIX = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * 客服信息推送地址前缀
     */
    String PUSH_CUSTOMER_PREFIX = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";

    /**
     * 获取token的URL
     */
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type={0}l&appid={1}&secret={2}";

    /**
     * 微信回调白名单url
     */
    String WEIXIN_CALL_BACK_SERVER_IP_PREFIX = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";
}
