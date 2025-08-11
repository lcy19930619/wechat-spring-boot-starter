package net.jlxxw.wechat.constant;

/**
 * 微信URL常量
 *
 * @author chunyang.leng
 * @date 2020/11/11 13:08
 */
public class UrlConstant {
    /**
     * 新增草稿URL
     */
    public static final String ADD_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/add?access_token={0}";

    /**
     * 获取草稿URL
     */
    public static final String GET_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/get?access_token={0}";

    /**
     * 删除草稿URL
     */
    public static final String DELETE_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/delete?access_token={0}";

    /**
     * 更新草稿URL
     */
    public static final String UPDATE_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/update?access_token={0}";

    /**
     * 获取草稿列表URL
     */
    public static final String GET_DRAFT_LIST_URL = "https://api.weixin.qq.com/cgi-bin/draft/batchget?access_token={0}";

    /**
     * 获取草稿总数URL
     */
    public static final String GET_DRAFT_COUNT_URL = "https://api.weixin.qq.com/cgi-bin/draft/count?access_token={0}";
    
    /**
     * 获取已发布文章列表URL
     */
    public static final String GET_PUBLISH_LIST_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/batchget?access_token={0}";
    
    /**
     * 删除已发布文章URL
     */
    public static final String DELETE_PUBLISH_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/delete?access_token={0}";
    
    /**
     * 获取发布状态URL
     */
    public static final String GET_PUBLISH_STATUS_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/get?access_token={0}";
}