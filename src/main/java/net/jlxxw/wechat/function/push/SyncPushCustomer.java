package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.component.BatchExecutor;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.customer.CustomerMessageDTO;
import net.jlxxw.wechat.function.token.WeiXinTokenManager;
import net.jlxxw.wechat.response.WeiXinResponse;
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

import java.text.MessageFormat;
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
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;

    /**
     * 推送一个客服信息
     *
     * @param messageDTO 客服信息
     * @return 微信返回结果, 如果微信返回为null, 则该方法返回null
     */
    public WeiXinResponse pushCustomer(CustomerMessageDTO messageDTO) {
        Objects.requireNonNull(messageDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(messageDTO);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        String url = MessageFormat.format(UrlConstant.PUSH_CUSTOMER_PREFIX,weiXinTokenManager.getTokenFromLocal());
        ResponseEntity<WeiXinResponse> responseEntity = restTemplate.postForEntity( url, request, WeiXinResponse.class);
        return responseEntity.getBody();
    }

    /**
     * 批量推送
     * @param messageList 多个客服信息
     * @return
     */
    public List<WeiXinResponse> pushCustomer(List<CustomerMessageDTO> messageList) {
        if (CollectionUtils.isEmpty(messageList)) {
            return new ArrayList<>();
        }
        List<WeiXinResponse> responseList = new ArrayList<>();

        batchExecutor.batchExecute(true, messageList, (list) -> {
            for (CustomerMessageDTO message : list) {
                WeiXinResponse weiXinResponse = pushCustomer(message);
                responseList.add(weiXinResponse);
            }
        });
        return responseList;
    }
}
