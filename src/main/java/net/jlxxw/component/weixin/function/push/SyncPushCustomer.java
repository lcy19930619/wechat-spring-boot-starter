package net.jlxxw.component.weixin.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.component.weixin.component.BatchExecutor;
import net.jlxxw.component.weixin.constant.UrlConstant;
import net.jlxxw.component.weixin.dto.customer.CustomerMessageDTO;
import net.jlxxw.component.weixin.response.WeiXinResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:14 下午
 */
@Lazy
@DependsOn({"weiXinProperties","weiXinTokenManager","webClientUtils"})
@Component
public class SyncPushCustomer {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BatchExecutor batchExecutor;

    /**
     * 推送一个客服信息
     *
     * @param messageDTO 客服信息
     * @param token    微信token
     * @return 微信返回结果, 如果微信返回为null, 则该方法返回null
     */
    public WeiXinResponse pushCustomer(CustomerMessageDTO messageDTO, String token) {
        Objects.requireNonNull(messageDTO);
        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("token error");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(messageDTO);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<WeiXinResponse> responseEntity = restTemplate.postForEntity(UrlConstant.PUSH_CUSTOMER_PREFIX + token, request, WeiXinResponse.class);
        WeiXinResponse body = responseEntity.getBody();
        if (Objects.isNull(body)) {
            return null;
        }
        body.setOpenId(messageDTO.getTouser());
        return body;
    }

    /**
     * 批量推送
     * @param messageList 多个客服信息
     * @param token 发送的token
     * @return
     */
    public List<WeiXinResponse> pushCustomer(List<CustomerMessageDTO> messageList, String token) {
        if (CollectionUtils.isEmpty(messageList)) {
            return new ArrayList<>();
        }
        List<WeiXinResponse> responseList = new ArrayList<>();

        batchExecutor.batchExecute(true, messageList, (list) -> {
            for (CustomerMessageDTO message : list) {
                WeiXinResponse weiXinResponse = pushCustomer(message, token);
                responseList.add(weiXinResponse);
            }
        });
        return responseList;
    }
}
