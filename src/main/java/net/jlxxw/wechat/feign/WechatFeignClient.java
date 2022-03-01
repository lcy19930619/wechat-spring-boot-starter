package net.jlxxw.wechat.feign;

import net.jlxxw.wechat.dto.customer.CustomerMessageDTO;
import net.jlxxw.wechat.dto.template.WeChatTemplateDTO;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import net.jlxxw.wechat.response.user.SubscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 微信服务openFeign接口
 * @author chunyang.leng
 * @date 2022-01-27 10:52 PM
 */
@FeignClient(value = "wechat-api", url = "https://api.weixin.qq.com")
public interface WechatFeignClient {

    /**
     * 模版推送
     * @param token token
     * @param weChatTemplateDTO 模版数据
     * @return 推送结果
     */
    @PostMapping("cgi-bin/message/template/send?access_token={token}")
    WeChatResponse sendTemplate(@PathVariable("token") String token,
                                @RequestBody WeChatTemplateDTO weChatTemplateDTO);

    /**
     * 使用客服接口发送信息
     * @param token token
     * @param customerMessageDTO 客服接口数据传输对象
     * @return 微信应答结果
     */
    @PostMapping("cgi-bin/message/custom/send?access_token={token}")
    WeChatResponse sendCustomerMessage(@PathVariable("token") String token,
                                       @RequestBody CustomerMessageDTO customerMessageDTO);

    /**
     * 获取token
     * @param appId 微信应用id
     * @param appSecret 微信凭证
     * @return 微信应答对象
     */
    @GetMapping("cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}")
    WeChatTokenResponse getToken(@PathVariable("appId") String appId,
                                 @PathVariable("appSecret") String appSecret);

    /**
     * 获取Ticket
     * @param token 微信token
     * @return 微信应答对象
     */
    @GetMapping("cgi-bin/ticket/getticket?access_token={token}&type=jsapi")
    WeChatTokenResponse getTicket(@PathVariable("token") String token);


    /**
     * 根据openId 获取用户信息
     * @param token token
     * @param openId 用户的openId
     * @return
     */
    @GetMapping("cgi-bin/user/info?access_token={token}&openid={openId}&lang=zh_CN")
    SubscriptionResponse selectByOpenId(@PathVariable("token") String token,
                                        @PathVariable("openId") String openId);
}

