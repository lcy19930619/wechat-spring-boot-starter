package net.jlxxw.wechat.function.freepublish;

import com.alibaba.fastjson.JSON;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.freepublish.FreePublishListDTO;
import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.freepublish.FreePublishListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * 公众号 发布文章管理
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_batchget.html">原始文档</a>
 */
public class FreePublishManager {
    private static final Logger logger = LoggerFactory.getLogger(FreePublishManager.class);
    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    /**
     * 发布文章管理
     *
     * @param restTemplate          http 工具
     * @param weChatTokenRepository token存储器
     */
    public FreePublishManager(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
    }

    /**
     * 获取访问令牌
     *
     * @return 访问令牌
     */
    private String getToken() {
        return weChatTokenRepository.get();
    }

    /**
     * 获取已发布的消息列表
     * 本接口用于获取已成功发布的消息列表
     *
     * @param freePublishListDTO 已发布文章列表查询条件
     * @return 已发布文章列表
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_batchget.html">文档地址</a>
     */
    public FreePublishListResponse getPublicationList(
        @NotNull(message = "已发布文章列表查询条件不能为空") FreePublishListDTO freePublishListDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.BATCH_GET_FREE_PUBLISH, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FreePublishListDTO> entity = new HttpEntity<>(freePublishListDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        FreePublishListResponse response = JSON.parseObject(body, FreePublishListResponse.class);
        logger.debug("获取已发布文章列表结果: {}", body);
        return response;
    }
}