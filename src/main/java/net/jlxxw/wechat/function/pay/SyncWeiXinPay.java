package net.jlxxw.wechat.function.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.context.SpringContextHolder;
import net.jlxxw.wechat.dto.pay.jsapi.v3.OrderInfoDTO;
import net.jlxxw.wechat.event.CreatePrePayEvent;
import net.jlxxw.wechat.exception.WeChatPayException;
import net.jlxxw.wechat.properties.WeChatPayProperties;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.util.RSAUtils;
import net.jlxxw.wechat.vo.jsapi.v3.ExecutePayVO;
import net.jlxxw.wechat.vo.jsapi.v3.PayResultVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2021-04-10 8:01 下午
 */
@Lazy
@DependsOn({"weChatProperties", "weChatTokenManager", "webClientUtils"})
@Component
public class SyncWeiXinPay {
    private static final int SUCCESS_CODE = 200;
    private static final String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private WeChatPayProperties weChatPayProperties;
    @Autowired
    private SpringContextHolder springContextHolder;

    /**
     * 创建预支付订单
     *
     * @param orderInfoDTO 订单信息
     * @param userAgent    微信要求发起请求的客户端在每一次请求中都使用HTTP头  User-Agent来标示自己，微信支付API v3很可能会拒绝处理无User-Agent 的请求。
     * @return 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
     * 长度：1-64
     * 示例值：wx201410272009395522657a690389285100
     * @throws WeChatPayException 微信接口返回状态码非200时，抛出异常，需要调用者自行处理
     */
    public PayResultVO createPrePay(OrderInfoDTO orderInfoDTO, String userAgent) throws WeChatPayException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("User-Agent", userAgent);
        String json = JSON.toJSONString(orderInfoDTO);
        HttpEntity<String> formEntity = new HttpEntity<>(json.toString(), headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(UrlConstant.JSAPI_V3_PRE_PAY_URL, formEntity, JSONObject.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        JSONObject body = responseEntity.getBody();
        if (Objects.isNull(body)) {
            throw new RuntimeException("un known exception ,response body is null");
        }
        if (SUCCESS_CODE != statusCode.value()) {
            String code = body.getString("code");
            String message = body.getString("message");
            JSONObject detail = body.getJSONObject("detail");
            throw new WeChatPayException(code, message, detail);
        }
        PayResultVO vo = new PayResultVO();
        vo.setPrePayId(body.getString("prepay_id"));
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        List<String> strings = responseHeaders.get("Request-ID");
        vo.setRequestId(strings.get(0));

        // 发布一个预支付事件
        springContextHolder.publishEvent(new CreatePrePayEvent(vo));
        return vo;
    }

    /**
     * 调起支付
     *
     * @param prePayId 预支付交易会话标识
     * @return 返回给前端，用于调起支付的对象
     * @throws Exception 签名异常
     * @see SyncWeiXinPay#createPrePay(OrderInfoDTO, java.lang.String)
     */
    public ExecutePayVO getExecutePayVO(String prePayId) throws Exception {
        String appId = weChatProperties.getAppId();
        // 时间戳
        long l = System.currentTimeMillis() / 1000;
        int time = (int) l;
        String randomString = RandomStringUtils.random(32, STR);

        String str = appId + "\n" + time + "\n" + randomString + "\n" + prePayId + "\n";
        String publicKey = weChatPayProperties.getPublicKey();
        // 加密后的字符串
        String encode = RSAUtils.encode(str, publicKey);

        ExecutePayVO vo = new ExecutePayVO();
        vo.setAppId(appId);
        vo.setNonceStr(randomString);
        vo.setSignType("RSA");
        vo.setPaySign(encode);
        vo.setPrepayId(prePayId);
        return vo;
    }
}
