package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.component.BatchExecutor;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.template.WeChatTemplateDTO;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.WeChatResponse;
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
@DependsOn({"weChatProperties", "weChatTokenManager", "webClientUtils"})
@Component
public class SyncPushTemplate {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BatchExecutor batchExecutor;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

    /**
     * 推送一个模版信息
     *
     * @param template 模版信息
     * @return 微信返回结果, 如果微信返回为null, 则该方法返回null
     */
    public WeChatResponse pushTemplate(WeChatTemplateDTO template) {
        Objects.requireNonNull(template);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(template);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        String url = MessageFormat.format(UrlConstant.PUSH_TEMPLATE_PREFIX, weChatTokenManager.getTokenFromLocal());
        ResponseEntity<WeChatResponse> responseEntity = restTemplate.postForEntity(url, request, WeChatResponse.class);
        return responseEntity.getBody();
    }

    /**
     * 批量推送
     *
     * @param templateList 多个模版信息
     * @return
     */
    public List<WeChatResponse> pushTemplate(List<WeChatTemplateDTO> templateList) {
        if (CollectionUtils.isEmpty(templateList)) {
            return new ArrayList<>();
        }
        List<WeChatResponse> responseList = new ArrayList<>();

        batchExecutor.batchExecute(true, templateList, (list) -> {
            for (WeChatTemplateDTO weChatTemplate : list) {
                WeChatResponse weChatResponse = pushTemplate(weChatTemplate);
                responseList.add(weChatResponse);
            }
        });
        return responseList;
    }
}
