package net.jlxxw.wechat.function.ai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import javax.validation.constraints.NotBlank;
import net.jlxxw.wechat.enums.AiBotEnvEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.properties.WeChatAiBotProperties;
import net.jlxxw.wechat.response.ai.ChatResponse;
import net.jlxxw.wechat.response.ai.WeChatAiBotSignatureResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 微信开放团队的ai机器人接口）
 *
 * @author chunyang.leng
 * @date 2023-04-11 14:48
 * @see <a href="https://developers.weixin.qq.com/doc/aispeech/confapi/mp/getbindlink.html">开发文档</a>
 * @see <a href="https://chatbot.weixin.qq.com/">ai平台</a>
 */
@Component
public class AiBotFunction {

    private static final Logger logger = LoggerFactory.getLogger(AiBotFunction.class);

    @Autowired
    private WeChatAiBotProperties weChatAiBotProperties;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 步骤一
     * <br/>
     * 获取signature接口
     * <br/>
     * 后续所有的 ai bot 业务都需要使用此签名，记得动态更新
     * <br/>
     *
     * @param userId 用户的id
     * @return
     * @see <a href="https://developers.weixin.qq.com/doc/aispeech/confapi/INTERFACEDOCUMENT.html">接口文档地址</a>
     */
    public WeChatAiBotSignatureResponse signature(
        @NotBlank(message = "用户id不能为空") String userId) throws WeChatException, ParamCheckException {
        String token = weChatAiBotProperties.getToken();
        if (StringUtils.isBlank(token)) {
            throw new ParamCheckException("token 不能为空");
        }
        String url = "https://chatbot.weixin.qq.com/openapi/sign/" + token;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = jsonObject.toJSONString();
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        logger.debug("ai bot signature,请求地址:{},请求参数:{},请求结果:{}",url,json,body);
        WeChatAiBotSignatureResponse weChatResponse = JSON.parseObject(body, WeChatAiBotSignatureResponse.class);
        if (!weChatResponse.isSuccessful()) {
            throw new WeChatException(weChatResponse);
        }
        return weChatResponse;
    }

    /**
     * 智能对话接口
     * <br/>
     * 对话平台问答策略一共有三段：
     * <br/>
     * 第一段“精准命中”：用户问题得分大于机器人账号阈值，此时为精准名准，status字段为 FAQ、CONTEXT_FAQ、GENERAL_FAQ 等值
     * <br/>
     * 第二段“问题推荐”：用户问题得分小于机器人账号阈值，但大于该阈值与推荐阈值的乘积，此时为问题推荐，status字段为FAQ_RECOMMEND，且此时options会有相应的推荐结果
     * <br/>
     * 第三段“未命中”：用户问题得分小于机器人账号阈值与与推荐阈值的乘积, 此时为未命中，status字段为NOMATCH
     *
     * @param signature            步骤一 获取到的 signature 返回值
     * @param query                用户发送的消息
     * @param env                  默认是online, debug是测试环境,online是线上环境
     * @param firstPrioritySkills  限定技能命中范围 比如：["技能1"]，只匹配命中“技能1”中的所有问答内容
     * @param secondPrioritySkills 限定技能命中范围 比如：["技能2"]，只匹配命中“技能2”中的所有问答内容,比firstPrioritySkills命中优先级低
     * @see <a href="https://developers.weixin.qq.com/doc/aispeech/confapi/INTERFACEDOCUMENT.html">接口文档</a>
     */
    public ChatResponse chat(@NotBlank(message = "signature 不应该为空") String signature,
                             @NotBlank(message = "用户查询内容不能为空") String query,
                             AiBotEnvEnum env,
                             List<String> firstPrioritySkills,
                             List<String> secondPrioritySkills) throws WeChatException, ParamCheckException {

        String token = weChatAiBotProperties.getToken();
        if (StringUtils.isBlank(token)) {
            throw new ParamCheckException("token 不能为空");
        }
        String url = "https://chatbot.weixin.qq.com/openapi/aibot/" + token;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);
        jsonObject.put("signature", signature);
        if (env != null) {
            jsonObject.put("env", env.name().toLowerCase());
        }
        jsonObject.put("first_priority_skills", firstPrioritySkills);
        jsonObject.put("second_priority_skills", secondPrioritySkills);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = jsonObject.toJSONString();
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        logger.debug("ai bot chat,请求地址:{},请求参数:{},请求结果:{}",url,json,body);
        ChatResponse weChatResponse = JSON.parseObject(body, ChatResponse.class);
        if (!weChatResponse.isSuccessful()) {
            throw new WeChatException(weChatResponse);
        }
        return weChatResponse;
    }

}
