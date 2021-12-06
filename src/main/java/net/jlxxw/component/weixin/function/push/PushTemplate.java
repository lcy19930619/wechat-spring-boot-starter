package net.jlxxw.component.weixin.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.component.weixin.component.BatchExecutor;
import net.jlxxw.component.weixin.constant.UrlConstant;
import net.jlxxw.component.weixin.dto.template.WxTemplate;
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
import java.util.function.Supplier;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:14 下午
 */
@Lazy
@DependsOn({"weiXinProperties","weiXinTokenManager","webClientUtils"})
@Component
public class PushTemplate {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BatchExecutor batchExecutor;

    /**
     * token 失效错误码
     */
    private static final int TOKEN_ERROR = 40001;

    /**
     * 多线程共享token
     */
    private volatile String volatileToken;
    /**
     * 推送一个模版信息
     *
     * @param template 模版信息
     * @param token    微信token
     * @return 微信返回结果, 如果微信返回为null, 则该方法返回null
     */
    public WeiXinResponse pushTemplate(WxTemplate template, String token) {
        Objects.requireNonNull(template);
        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("token error");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(template);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<WeiXinResponse> responseEntity = restTemplate.postForEntity(UrlConstant.PUSH_TEMPLATE_PREFIX + token, request, WeiXinResponse.class);
        WeiXinResponse body = responseEntity.getBody();
        if (Objects.isNull(body)) {
            return null;
        }
        body.setOpenId(template.getTouser());
        return body;
    }

    /**
     * 批量推送
     * @param templateList 多个模版信息
     * @param token 发送的token
     * @param getTokenFunction 获取token的方法
     * @return
     */
    public List<WeiXinResponse> pushTemplate(List<WxTemplate> templateList, String token, Supplier<String> getTokenFunction) {
        if (CollectionUtils.isEmpty(templateList)) {
            return new ArrayList<>();
        }
        List<WeiXinResponse> responseList = new ArrayList<>();
        volatileToken = token;

        batchExecutor.batchExecute(true, templateList, (list) -> {
            for (WxTemplate wxTemplate : list) {
                WeiXinResponse weiXinResponse = pushTemplate(wxTemplate, volatileToken);
                if(weiXinResponse != null && weiXinResponse.getErrcode() == TOKEN_ERROR){
                    volatileToken =  getTokenFunction.get();
                }
                responseList.add(weiXinResponse);
            }
        });
        return responseList;
    }
}
