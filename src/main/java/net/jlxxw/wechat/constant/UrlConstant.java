package net.jlxxw.wechat.constant;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:17 下午
 */
public interface UrlConstant {


    /**
     * 模板推送地址前缀
     */
    String PUSH_TEMPLATE_PREFIX = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";

    /**
     * 客服信息推送地址前缀
     */
    String PUSH_CUSTOMER_PREFIX = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token={0}";

    /**
     * 获取token的URL
     */
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    /**
     * 微信回调白名单url
     */
    String WEIXIN_CALL_BACK_SERVER_IP_PREFIX = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";

    /**
     * 获取一个用户的基本信息接口
     */
    String ONE_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang={2}";

    /**
     * 批量获取用户信息接口，最多支持100个，post请求
     */
    String BATCH_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=";

    /**
     * 获取帐号的关注者列表
     */
    String FIND_ALL_USER_OPENID = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}&next_openid={1}";

    /**
     * 上传临时素材
     */
    String UPLOAD_TEMP_MATERIAL = "https https://api.weixin.qq.com/cgi-bin/media/upload?access_token={0}&type={1}";

    /**
     * 下载临时素材
     */
    String DOWN_TEMP_MATERIAL = "https://api.weixin.qq.com/cgi-bin/media/get/jssdk?access_token={0}&media_id={1}";

    /**
     * 上传永久素材
     */
    String UPLOAD_PERMANENT_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token={0}&type={1}";

    /**
     * 下载永久素材
     */
    String DOWNLOAD_PERMANENT_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token={0}";

    /**
     * 删除永久素材
     */
    String DELETE_PERMANENT_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token={0}";

    /**
     * JSAPI_V3 统一下单API url
     */
    String JSAPI_V3_PRE_PAY_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

    /**
     * 创建临时二维码url
     */
    String CREATE_TEMP_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token={0}";

    /**
     * 清空api的调用
     */
    String OPEN_API_CLEAN = "https://api.weixin.qq.com/cgi-bin/clear_quota?access_token={0}";

    /**
     * 查询api quota 调用情况
     */
    String OPEN_API_SELECT_QUOTA = "https://api.weixin.qq.com/cgi-bin/openapi/quota/get?access_token={0}";

    /**
     * 创建菜单
     */
    String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={0}";

    /**
     * 删除菜单
     */
    String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token={0}";

    /**
     * 获取全部菜单
     */
    String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token={0}";

    /**
     * 获取 js_api_ticket
     */
    String GET_JS_API_TICKET_URL ="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

    /**
     * 创建个性化菜单
     */
    String CREATE_PERSONALIZED_MENU = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token={0}";

    /**
     * 删除个性化菜单
     */
    String DELETE_PERSONALIZED_MENU = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token={0}";

    /**
     * 测试个性化菜单匹配结果
     */
    String TRY_MATCH_PERSONALIZED_MENU = "https://api.weixin.qq.com/cgi-bin/menu/trymatch?access_token={0}";
}
