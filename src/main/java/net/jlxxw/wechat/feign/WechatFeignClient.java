package net.jlxxw.wechat.feign;

import javax.validation.constraints.NotBlank;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 微信服务openFeign接口
 * @author chunyang.leng
 * @date 2022-01-27 10:52 PM
 */
@FeignClient(value = "wechat-api", url = "https://api.weixin.qq.com")
public interface WechatFeignClient {

    /**
     * 获取token
     * @param appId 微信应用id
     * @param appSecret 微信凭证
     * @return 微信应答对象
     */
    @GetMapping("cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}")
    WeChatTokenResponse getToken(@NotBlank(message = "appId不能为空") @PathVariable("appId") String appId,
        @NotBlank(message = "appSecret不能为空")  @PathVariable("appSecret") String appSecret);

    /**
     * 获取Ticket
     * @param token 微信token
     * @return 微信应答对象
     */
    @GetMapping("cgi-bin/ticket/getticket?access_token={token}&type=jsapi")
    WeChatTokenResponse getTicket(@NotBlank(message = "token不能为空") @PathVariable("token") String token);


}

