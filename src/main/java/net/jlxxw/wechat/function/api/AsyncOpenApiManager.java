package net.jlxxw.wechat.function.api;

import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.function.token.WeiXinTokenManager;
import net.jlxxw.wechat.properties.WeiXinProperties;
import net.jlxxw.wechat.response.WeiXinResponse;
import net.jlxxw.wechat.response.api.ApiRequestRecord;
import net.jlxxw.wechat.response.api.ApiResult;
import net.jlxxw.wechat.util.WebClientUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

/**
 * 微信 openApi 管理
 *
 * @author chunyang.leng
 * @date 2021-11-23 2:22 下午
 */
@Lazy
@DependsOn({"weiXinProperties","weiXinTokenManager","webClientUtils"})
@Component
public class AsyncOpenApiManager {
    @Autowired
    private WeiXinProperties weiXinProperties;
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    @Autowired
    private WebClientUtils webClientUtils;

    /**
     * @link https://developers.weixin.qq.com/doc/offiaccount/openApi/get_api_quota.html
     *
     * 本接口用于清空公众号/小程序/第三方平台等接口的每日调用接口次数。<br/>
     * 1、如果要清空公众号的接口的quota，则需要用公众号的access_token；如果要清空小程序的接口的quota，则需要用小程序的access_token；如果要清空第三方平台的接口的quota，则需要用第三方平台的component_access_token<br/>
     * 2、如果是第三方服务商代公众号或者小程序清除quota，则需要用authorizer_access_token<br/>
     * 3、每个帐号每月共10次清零操作机会，清零生效一次即用掉一次机会；第三方帮助公众号/小程序调用时，实际上是在消耗公众号/小程序自身的quota<br/>
     * 4、由于指标计算方法或统计时间差异，实时调用量数据可能会出现误差，一般在1%以内<br/>
     *
     * @return <br/>
     *                 返回码	errmsg	说明
     *                 <br/>
     *                 0	    ok	    查询成功
     *                 <br/>
     *                 48006	forbid to clear quota because of reaching the limit	一个月10次的机会用完了
     *                 <br/>
     *                 40013	invalid appid	appid写错了；或者填入的appid与access_token代表的账号的appid不一致
     *
     */
    public Mono<WeiXinResponse> clean()  {
        String appId = weiXinProperties.getAppId();
        String token = weiXinTokenManager.getTokenFromLocal();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", appId);
        String url = MessageFormat.format(UrlConstant.OPEN_API_CLEAN, token);
        return webClientUtils.sendPostJSON(url, jsonObject.toJSONString(), WeiXinResponse.class);
    }

    /**
     * @link https://developers.weixin.qq.com/doc/offiaccount/openApi/get_api_quota.html
     *
     * 查询openAPI调用quota
     * <p>
     * 本接口用于查询公众号/小程序/第三方平台等接口的每日调用接口的额度以及调用次数。
     * </p>
     * <pre>
     *  注意事项
     * 1、如果查询的api属于公众号的接口，则需要用公众号的access_token；如果查询的api属于小程序的接口，则需要用小程序的access_token；如果查询的接口属于第三方平台的接口，则需要用第三方平台的component_access_token；否则会出现76022报错。
     * 2、如果是第三方服务商代公众号或者小程序查询公众号或者小程序的api，则需要用authorizer_access_token
     * 3、每个接口都有调用次数限制，请开发者合理调用接口
     * 4、”/xxx/sns/xxx“这类接口不支持使用该接口，会出现76022报错。
     * </pre>
     * @param cgiPath api的请求地址，例如"/cgi-bin/message/custom/send";不要前缀“https://api.weixin.qq.com” ，也不要漏了"/",否则都会76003的报错
     */
    public Mono<ApiResult> selectQuota(String cgiPath) {
        if (StringUtils.isBlank(cgiPath)) {
            throw new NullPointerException();
        }
        String appId = weiXinProperties.getAppId();
        String token = weiXinTokenManager.getTokenFromLocal();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", appId);

        String url = MessageFormat.format(UrlConstant.OPEN_API_SELECT_QUOTA, token);

        return webClientUtils.sendPostJSON(url, jsonObject.toJSONString(), ApiResult.class);
    }

    /**
     * @link https://developers.weixin.qq.com/doc/offiaccount/openApi/get_rid_info.html
     * 查询rid信息
     * 本接口用于查询调用公众号/小程序/第三方平台等接口报错返回的rid详情信息，辅助开发者高效定位问题
     * <pre>
     *     注意事项
     * 1、由于查询rid信息属于开发者私密行为，因此仅支持同账号的查询。举个例子，rid=1111，是小程序账号A调用某接口出现的报错，那么则需要使用小程序账号A的access_token调用当前接口查询rid=1111的详情信息，如果使用小程序账号B的身份查询，则出现报错，错误码为xxx。公众号、第三方平台账号的接口同理。
     *
     * 2、如果是第三方服务商代公众号或者小程序查询公众号或者小程序的api返回的rid，则使用同一账号的authorizer_access_token调用即可
     *
     * 3、rid的有效期只有7天，即只可查询最近7天的rid，查询超过7天的rid会出现报错，错误码为76001
     * </pre>
     * @param rid
     */
    public Mono<ApiRequestRecord> selectRid(String rid){
        if (StringUtils.isBlank(rid)) {
            throw new NullPointerException();
        }
        String appId = weiXinProperties.getAppId();
        String token = weiXinTokenManager.getTokenFromLocal();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", appId);

        String url = MessageFormat.format(UrlConstant.OPEN_API_SELECT_QUOTA, token);

        return webClientUtils.sendPostJSON(url, jsonObject.toJSONString(), ApiRequestRecord.class);
    }
}
