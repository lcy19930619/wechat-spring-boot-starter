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
    String PUSH_CUSTOMER_PREFIX = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={0}";

    /**
     * 获取token的URL
     */
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    /**
     * 微信回调白名单url
     */
    String WECHAT_CALL_BACK_SERVER_IP_PREFIX = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token={0}";

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
    String UPLOAD_TEMP_MATERIAL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token={0}&type={1}";

    /**
     * 下载临时素材
     */
    String DOWN_TEMP_MATERIAL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token={0}&media_id={1}";

    /**
     * 下载高清语音接口
     */
    String DOWN_HD_VOICE = "https://api.weixin.qq.com/cgi-bin/media/get/jssdk?access_token={0}&media_id={1}";


    /**
     * 统计素材使用情况
     */
    String MATERIAL_COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token={0}";
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
     * 微信支付订单号查询
     */
    String JSAPI_V3_SELECT_BY_TRANSACTION_ID = "https://api.mch.weixin.qq.com/v3/pay/transactions/id/{0}?mchid={1}";

    /**
     * 商户订单号查询
     */
    String JSAPI_V3_SELECT_BY_OUT_TRADE_NO = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{0}";


    /**
     * 创建临时二维码url
     */
    String CREATE_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token={0}";

    /**
     * 清空api的调用
     */
    String OPEN_API_CLEAN = "https://api.weixin.qq.com/cgi-bin/clear_quota?access_token={0}";

    /**
     * 查询api quota 调用情况
     */
    String OPEN_API_SELECT_QUOTA = "https://api.weixin.qq.com/cgi-bin/openapi/quota/get?access_token={0}";

    /**
     * 查询api quota 调用情况
     */
    String OPEN_API_SELECT_RID = "https://api.weixin.qq.com/cgi-bin/openapi/rid/get?access_token={0}";

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

    /**
     * 引导用户打开此链接授权
     */
    String WEB_PAGE_AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state={3}#wechat_redirect";

    /**
     * 微信 oauth2 授权链接
     */
    String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    /**
     * 刷新 oauth2 token
     */
    String REFRESH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={0}&grant_type=refresh_token&refresh_token={1}";

    /**
     * 拉取用户信息
     */
    String GET_USER_INFO_BY_OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang={2}";

    /**
     * 检查token是否有效
     */
    String CHECK_OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/auth?access_token={0}&openid={1}";

    /**
     * 创建公众号标签
     */
    String CREATE_TAGS_URL = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token={0}";

    /**
     * 获取公众号标签
     */
    String GET_TAGS_URL = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token={0}";

    /**
     * 更新标签
     */
    String UPDATE_TAGS_URL = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token={0}";

    /**
     * 删除标签
     */
    String DELETE_TAGS_URL = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token={0}";

    /**
     * 根据标签id，获取用户信息列表
     */
    String GET_TAG_USERS_URL = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token={0}";

    /**
     * 用户批量打标
     */
    String USERS_BATCH_TAGGING_URL = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token={0}";

    /**
     * 用户批量取消打标
     */
    String USERS_BATCH_UNTAGGING_URL = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token={0}";

    /**
     * 获取用户身上的标签列表
     */
    String GET_USER_TAG_URL = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token={0}";

    /**
     * 添加草稿
     */
    String ADD_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/add?access_token={0}";
    /**
     * 获取草稿
     */
    String GET_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/get?access_token={0}";
    /**
     * 删除草稿
     */
    String DELETE_DRAFT_URL="https://api.weixin.qq.com/cgi-bin/draft/delete?access_token={0}";
    /**
     * 修改草稿
     */
    String UPDATE_DRAFT_URL="https://api.weixin.qq.com/cgi-bin/draft/update?access_token={0}";
    /**
     * 获取草稿列表
     */
    String GET_DRAFT_LIST_URL="https://api.weixin.qq.com/cgi-bin/draft/batchget?access_token={0}";
    /**
     * 获取草稿数量
     */
    String GET_DRAFT_COUNT_URL="https://api.weixin.qq.com/cgi-bin/draft/count?access_token={0}";

    /**
     * 获取已发布的消息列表
     */
    String BATCH_GET_FREE_PUBLISH = "https://api.weixin.qq.com/cgi-bin/freepublish/batchget?access_token={0}";


}
