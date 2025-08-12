package net.jlxxw.wechat.function.draft;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.draft.*;
import net.jlxxw.wechat.function.token.TokenManager;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.draft.*;
import net.jlxxw.wechat.response.token.WeChatTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

/**
 * 草稿管理
 * 开发者可以使用草稿箱的相关接口，实现对公众号的草稿进行创建、查询、修改、删除等操作。
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Draft_Box/Draft_overview.html">文档地址</a>
 */
public class DraftManager {
    private static final Logger logger = LoggerFactory.getLogger(DraftManager.class);
    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    /**
     * 草稿管理
     *
     * @param restTemplate          http 工具
     * @param weChatTokenRepository token存储器
     */
    public DraftManager(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
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
     * 新增草稿
     * 开发者可新增图文消息草稿或图片消息草稿
     *
     * @param draftAddDTO 草稿信息
     * @return 草稿新增结果
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_add.html">文档地址</a>
     */
    public DraftAddResponse addDraft(@NotNull(message = "草稿信息不能为空") DraftAddDTO draftAddDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.ADD_DRAFT_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(draftAddDTO);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        DraftAddResponse response = JSON.parseObject(body, DraftAddResponse.class);
        logger.debug("新增草稿结果: {}", body);
        return response;
    }

    /**
     * 获取草稿详情
     * 新增草稿后，开发者可以根据草稿ID获取草稿信息
     *
     * @param mediaId 要获取的草稿的media_id
     * @return 草稿详情
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_getdraft.html">文档地址</a>
     */
    public DraftGetResponse getDraft(@NotBlank(message = "草稿media_id不能为空") String mediaId) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.GET_DRAFT_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = JSON.toJSONString(new MediaIdRequest(mediaId));
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        DraftGetResponse response = JSON.parseObject(body, DraftGetResponse.class);
        logger.debug("获取草稿详情结果: {}", body);
        return response;
    }

    /**
     * 删除草稿
     * 新增草稿后，开发者可以根据本接口来删除不再需要的草稿，节省空间
     *
     * @param mediaId 要删除的草稿的media_id
     * @return 删除结果
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_delete.html">文档地址</a>
     */
    public WeChatResponse deleteDraft(@NotBlank(message = "草稿media_id不能为空") String mediaId) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.DELETE_DRAFT_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = JSON.toJSONString(new MediaIdRequest(mediaId));
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        WeChatResponse response = JSON.parseObject(body, WeChatResponse.class);
        logger.debug("删除草稿结果: {}", body);
        return response;
    }

    /**
     * 更新草稿
     * 开发者可通过本接口对草稿进行修改
     *
     * @param draftUpdateDTO 草稿更新信息
     * @return 更新结果
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_update.html">文档地址</a>
     */
    public WeChatResponse updateDraft(@NotNull(message = "草稿更新信息不能为空") DraftUpdateDTO draftUpdateDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.UPDATE_DRAFT_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(draftUpdateDTO);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        WeChatResponse response = JSON.parseObject(body, WeChatResponse.class);
        logger.debug("更新草稿结果: {}", body);
        return response;
    }

    /**
     * 获取草稿列表
     * 新增草稿之后，开发者可以获取草稿箱中的草稿列表
     *
     * @param draftListDTO 草稿列表查询条件
     * @return 草稿列表
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_batchget.html">文档地址</a>
     */
    public DraftListResponse getDraftList(@NotNull(message = "草稿列表查询条件不能为空") DraftListDTO draftListDTO) {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.GET_DRAFT_LIST_URL, token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(draftListDTO);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String body = responseEntity.getBody();
        DraftListResponse response = JSON.parseObject(body, DraftListResponse.class);
        logger.debug("获取草稿列表结果: {}", body);
        return response;
    }

    /**
     * 获取草稿总数
     * 开发者可以根据本接口来获取草稿箱中的草稿总数
     *
     * @return 草稿总数
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/draftbox/draftmanage/api_draft_count.html">文档地址</a>
     */
    public DraftCountResponse getDraftCount() {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.GET_DRAFT_COUNT_URL, token);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String body = responseEntity.getBody();
        DraftCountResponse response = JSON.parseObject(body, DraftCountResponse.class);
        logger.debug("获取草稿总数结果: {}", body);
        return response;
    }

    /**
     * 内部类，用于封装media_id请求参数
     */
    private static class MediaIdRequest {
        @JSONField(name = "media_id")
        @JsonProperty(value = "media_id")
        private String mediaId;

        public MediaIdRequest(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }
    }
}