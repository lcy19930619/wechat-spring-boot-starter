package net.jlxxw.wechat.feign;

import net.jlxxw.wechat.dto.feign.api.ApiDTO;
import net.jlxxw.wechat.dto.feign.api.ApiQuotaDTO;
import net.jlxxw.wechat.dto.feign.api.ApiRidDTO;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.api.ApiRequestRecord;
import net.jlxxw.wechat.response.api.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.websocket.server.PathParam;

/**
 * 微信open api 相关接口信息查询
 * @author chunyang.leng
 * @date 2022-04-14 11:19 AM
 */
@FeignClient(value = "wechat-api-client", url = "https://api.weixin.qq.com")
public interface WechatApiFeignClient {


    /**
     * @return
     * <br/>
     * 返回码	errmsg	说明
     * <br/>
     * 0	    ok	    查询成功
     * <br/>
     * 48006	forbid to clear quota because of reaching the limit	一个月10次的机会用完了
     * <br/>
     * 40013	invalid appid	appid写错了；或者填入的appid与access_token代表的账号的appid不一致
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/openApi/get_api_quota.html">文档地址</a>
     * <br/>
     * 本接口用于清空公众号/小程序/第三方平台等接口的每日调用接口次数。<br/>
     * 1、如果要清空公众号的接口的quota，则需要用公众号的access_token；如果要清空小程序的接口的quota，则需要用小程序的access_token；如果要清空第三方平台的接口的quota，则需要用第三方平台的component_access_token<br/>
     * 2、如果是第三方服务商代公众号或者小程序清除quota，则需要用authorizer_access_token<br/>
     * 3、每个帐号每月共10次清零操作机会，清零生效一次即用掉一次机会；第三方帮助公众号/小程序调用时，实际上是在消耗公众号/小程序自身的quota<br/>
     * 4、由于指标计算方法或统计时间差异，实时调用量数据可能会出现误差，一般在1%以内<br/>
     * @param apiDTO 请求参数 appId
     * @param token 调用的token
     */
    @PostMapping("cgi-bin/clear_quota?access_token={token}")
    WeChatResponse openApiClean(@RequestBody ApiDTO apiDTO, @PathVariable("token") String token);


    /**
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/openApi/get_api_quota.html">文档地址</a>
     * <p>
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
     * @param apiDTO 查询参数 cgiPath api的请求地址，例如"/cgi-bin/message/custom/send";不要前缀“https://api.weixin.qq.com” ，也不要漏了"/",否则都会76003的报错
     * @param token 查询token
     * @return 查询结果
     */
    @PostMapping("cgi-bin/openapi/quota/get?access_token={token}")
    ApiResponse selectQuota(@RequestBody ApiQuotaDTO apiDTO, @PathParam("token") String token) ;

    /**
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/openApi/get_rid_info.html">文档地址</a>
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
     * @param apiDTO 请求参数 ,查询rid信息
     * @param token 查询token
     * @return rid请求信息记录结果
     */
    @PostMapping("cgi-bin/openapi/quota/get?access_token={token}")
    ApiRequestRecord selectRid(@RequestBody ApiRidDTO apiDTO, @PathVariable("token") String token) ;
}
