package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.component.BatchExecutor;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.customer.CustomerMessageDTO;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.WeChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:14 下午
 */
@DependsOn(WeChatTokenManager.BEAN_NAME)
@Component
public class SyncPushCustomer {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BatchExecutor batchExecutor;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

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
        String url = MessageFormat.format(UrlConstant.PUSH_CUSTOMER_PREFIX, weChatTokenManager.getTokenFromLocal());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        WeChatResponse weChatResponse = JSON.parseObject(body, WeChatResponse.class);
        if (!weChatResponse.isSuccessful()){
            throw new WeChatException(weChatResponse);
        }
        return weChatResponse;
    }

    /**
     * 批量推送
     *
     * @param messageList 多个客服信息
     * @throws ParamCheckException 方法调用前，参数检查未通过
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html#%E5%AE%A2%E6%9C%8D%E6%8E%A5%E5%8F%A3-%E5%8F%91%E6%B6%88%E6%81%AF">接口文档</a>
     * @return
     */
    public List<WeChatResponse> pushCustomer(@NotNull(message = "待处理的用户信息不能为空")
                                             List<CustomerMessageDTO> messageList) throws ParamCheckException {
        if (CollectionUtils.isEmpty(messageList)) {
            return new ArrayList<>();
        }
        List<WeChatResponse> responseList = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(messageList.size());
        batchExecutor.batchExecute(true, messageList, (list) -> {
            for (CustomerMessageDTO message : list) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String json = JSON.toJSONString(message);
                HttpEntity<String> request = new HttpEntity<>(json, headers);
                String url = MessageFormat.format(UrlConstant.PUSH_CUSTOMER_PREFIX, weChatTokenManager.getTokenFromLocal());
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
                String body = responseEntity.getBody();
                WeChatResponse weChatResponse = JSON.parseObject(body, WeChatResponse.class);
                responseList.add(weChatResponse);
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            return responseList;
        }
        return responseList;
    }
}
