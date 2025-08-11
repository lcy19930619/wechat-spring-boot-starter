package net.jlxxw.wechat.function.freepublish;

import com.alibaba.fastjson.JSON;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.freepublish.FreePublishGetArticleDTO;
import net.jlxxw.wechat.dto.freepublish.FreePublishListDTO;
import net.jlxxw.wechat.dto.freepublish.FreePublishDeleteDTO;
import net.jlxxw.wechat.dto.freepublish.FreePublishGetDTO;
import net.jlxxw.wechat.dto.freepublish.FreePublishSubmitDTO;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.freepublish.FreePublishGetArticleResponse;
import net.jlxxw.wechat.response.freepublish.FreePublishListResponse;
import net.jlxxw.wechat.response.freepublish.FreePublishGetResponse;
import net.jlxxw.wechat.response.freepublish.FreePublishSubmitResponse;
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

    /**
     * 删除已发布文章
     * 本接口用于删除已发布的文章，此操作不可逆，请谨慎操作
     *
     * @param freePublishDeleteDTO 删除已发布文章的参数
     * @return 删除结果
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublishdelete.html">文档地址</a>
     */
    public WeChatResponse deletePublication(
            @NotNull(message = "删除已发布文章参数不能为空") FreePublishDeleteDTO freePublishDeleteDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.DELETE_FREE_PUBLISH_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FreePublishDeleteDTO> entity = new HttpEntity<>(freePublishDeleteDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        WeChatResponse response = JSON.parseObject(body, WeChatResponse.class);
        logger.debug("删除已发布文章结果: {}", body);
        return response;
    }

    /**
     * 获取发布状态
     * 本接口用于查询发布任务的状态和详情
     *
     * @param freePublishGetDTO 获取发布状态的参数
     * @return 发布状态和详情
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_get.html">文档地址</a>
     */
    public FreePublishGetResponse getPublicationStatus(
            @NotNull(message = "获取发布状态参数不能为空") FreePublishGetDTO freePublishGetDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.GET_PUBLISH_STATUS_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FreePublishGetDTO> entity = new HttpEntity<>(freePublishGetDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        FreePublishGetResponse response = JSON.parseObject(body, FreePublishGetResponse.class);
        logger.debug("获取发布状态结果: {}", body);
        return response;
    }

    /**
     * 获取已发布文章详情
     * 本接口用于获取已发布的图文信息
     *
     * @param freePublishGetArticleDTO 获取文章详情的参数
     * @return 文章详情
     */
    public FreePublishGetArticleResponse getArticle(@NotNull(message = "获取发布状态参数不能为空") FreePublishGetArticleDTO freePublishGetArticleDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.GET_ARTICLE_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FreePublishGetArticleDTO> entity = new HttpEntity<>(freePublishGetArticleDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        FreePublishGetArticleResponse response = JSON.parseObject(body, FreePublishGetArticleResponse.class);
        logger.debug("获取发布状态结果: {}", body);
        return response;
    }

    /**
     * 发布草稿
     * 本接口用于将图文草稿提交发布
     *
     * @param freePublishSubmitDTO 发布草稿的参数
     * @return 发布结果
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/public/api_freepublish_submit.html">文档地址</a>
     */
    public FreePublishSubmitResponse submitPublication(
            @NotNull(message = "发布草稿参数不能为空") FreePublishSubmitDTO freePublishSubmitDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.SUBMIT_FREE_PUBLISH_PUBLISH_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FreePublishSubmitDTO> entity = new HttpEntity<>(freePublishSubmitDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        FreePublishSubmitResponse response = JSON.parseObject(body, FreePublishSubmitResponse.class);
        logger.debug("发布草稿结果: {}", body);
        return response;
    }
}