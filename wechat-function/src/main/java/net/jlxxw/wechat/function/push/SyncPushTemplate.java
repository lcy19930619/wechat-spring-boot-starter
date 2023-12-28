package net.jlxxw.wechat.function.push;

import com.alibaba.fastjson.JSON;
import net.jlxxw.wechat.aop.check.group.Insert;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.template.WeChatTemplateDTO;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * @author chunyang.leng
 * @date 2021/1/18 10:14 下午
 */
public class SyncPushTemplate {
    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    public SyncPushTemplate(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
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
        String url = MessageFormat.format(UrlConstant.PUSH_TEMPLATE_PREFIX, weChatTokenRepository.get());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        WeChatResponse weChatResponse = JSON.parseObject(body, WeChatResponse.class);
        if (!weChatResponse.isSuccessful()){
            throw new WeChatException(weChatResponse);
        }
        return weChatResponse;
    }
}
