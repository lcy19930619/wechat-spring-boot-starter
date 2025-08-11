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
}