package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.customer.CustomerMessageDTO;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:14 下午
 */
public class SyncPushCustomer {
    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    public SyncPushCustomer(RestTemplate restTemplate,WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
    }

    /**
     * 推送一个客服信息
     *
     * @param messageDTO 客服信息
     * @return 微信返回结果
     * @throws WeChatException 微信服务端返回异常
     * @throws ParamCheckException 参数检查未通过
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html#%E5%AE%A2%E6%9C%8D%E6%8E%A5%E5%8F%A3-%E5%8F%91%E6%B6%88%E6%81%AF">接口文档</a>
     */
    public WeChatResponse pushCustomer(@NotNull(message = "待处理的信息不能为空") CustomerMessageDTO messageDTO) throws WeChatException,ParamCheckException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(messageDTO);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        String url = MessageFormat.format(UrlConstant.PUSH_CUSTOMER_PREFIX, weChatTokenRepository.get());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        WeChatResponse weChatResponse = JSON.parseObject(body, WeChatResponse.class);
        if (!weChatResponse.isSuccessful()){
            throw new WeChatException(weChatResponse);
        }
        return weChatResponse;
    }

}
