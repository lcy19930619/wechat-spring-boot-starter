package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.aop.check.group.Insert;
import net.jlxxw.wechat.component.BatchExecutor;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.template.WeChatTemplateDTO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:14 下午
 */
public class SyncPushTemplate {
    private RestTemplate restTemplate;
    private BatchExecutor batchExecutor;
    private WeChatTokenManager weChatTokenManager;

    public SyncPushTemplate(RestTemplate restTemplate, BatchExecutor batchExecutor, WeChatTokenManager weChatTokenManager) {
        this.restTemplate = restTemplate;
        this.batchExecutor = batchExecutor;
        this.weChatTokenManager = weChatTokenManager;
    }

    /**
     * 推送一个模版信息
     *
     * @param template 模版信息
     * @return 微信返回结果
     * @throws ParamCheckException 参数检查不通过
     * @throws WeChatException 微信服务端异常
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html">接口文档</a>
     */
    public WeChatResponse pushTemplate(@Validated(Insert.class) WeChatTemplateDTO template) throws ParamCheckException,WeChatException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(template);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        String url = MessageFormat.format(UrlConstant.PUSH_TEMPLATE_PREFIX, weChatTokenManager.getTokenFromLocal());
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
     * @param templateList 多个模版信息
     * @return
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html">接口文档</a>
     * @throws ParamCheckException 参数检查不通过
     */
    public List<WeChatResponse> pushTemplate(@Validated(Insert.class) List<WeChatTemplateDTO> templateList) throws ParamCheckException {
        if (CollectionUtils.isEmpty(templateList)) {
            return new ArrayList<>();
        }
        List<WeChatResponse> responseList = new LinkedList<>();

        CountDownLatch countDownLatch = new CountDownLatch(templateList.size());
        batchExecutor.batchExecute(true, templateList, (list) -> {
            for (WeChatTemplateDTO weChatTemplate : list) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String json = JSON.toJSONString(weChatTemplate);
                HttpEntity<String> request = new HttpEntity<>(json, headers);
                String url = MessageFormat.format(UrlConstant.PUSH_TEMPLATE_PREFIX, weChatTokenManager.getTokenFromLocal());
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
