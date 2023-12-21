package net.jlxxw.wechat.function.tag;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;

import net.jlxxw.wechat.function.user.UserManager;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.tag.Tag;
import net.jlxxw.wechat.response.tag.TagResponse;
import net.jlxxw.wechat.response.tag.TagUserResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

/**
 * 标签管理
 * 开发者可以使用用户标签管理的相关接口，实现对公众号的标签进行创建、查询、修改、删除等操作，也可以对用户进行打标签、取消标签等操作。
 *
 * @author chunyang.leng
 * @date 2022-08-29 10:53 AM
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/User_Tag_Management.html">文档地址</a>
 */
public class TagManager {
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    public TagManager(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
    }

    /**
     * 创建公众号标签
     * 一个公众号，最多可以创建100个标签。
     *
     * @param tagValue 要创建的标签
     * @throws WeChatException     微信验证异常
     * @throws ParamCheckException 参数检查不通过
     */
    public TagResponse createTag(
        @NotBlank(message = "标签名字不能为空") @Size(max = 30, message = "最大长度不能超过30个字符") String tagValue) throws WeChatException, ParamCheckException {
        String url = MessageFormat.format(UrlConstant.CREATE_TAGS_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"创建公众号标签url:{}",url);
        JSONObject requestParam = new JSONObject();
        JSONObject param = new JSONObject();
        param.put("name", tagValue);
        requestParam.put("tag", param);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(requestParam);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger,"创建公众号标签应答结果:{}",body);
        TagResponse response = JSONObject.parseObject(body, TagResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 获取公众号已创建的标签
     * @throws WeChatException 微信服务端验证异常
     */
    public List<Tag> getTag() throws WeChatException{
        String url = MessageFormat.format(UrlConstant.GET_TAGS_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"获取公众号已创建的标签url:{}",url);
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String body = entity.getBody();
        LoggerUtils.debug(logger,"获取公众号已创建的标签应答结果:{}",body);

        WeChatResponse weChatResponse = JSONObject.parseObject(body, WeChatResponse.class);
        if (!weChatResponse.isSuccessful()) {
            throw new WeChatException(weChatResponse);
        }
        JSONObject response = JSONObject.parseObject(body);
        return response.getJSONArray("tags").toJavaList(Tag.class);

    }

    /**
     * 更新标签
     *
     * @param id       标签id
     * @param tagValue 新的标签值
     * @throws WeChatException     微信验证异常
     * @throws ParamCheckException 参数检查不通过
     * @return
     */
    public WeChatResponse updateTag(@NotNull(message = "标签id不能为空") Integer id,
        @NotBlank(message = "标签名字不能为空") @Size(max = 30, message = "最大长度不能超过30个字符") String tagValue) throws WeChatException, ParamCheckException {
        String url = MessageFormat.format(UrlConstant.UPDATE_TAGS_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"更新标签url:{}",url);
        JSONObject requestParam = new JSONObject();
        JSONObject param = new JSONObject();
        param.put("name", tagValue);
        param.put("id", id);
        requestParam.put("tag", param);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(requestParam);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger,"更新标签应答结果:{}",body);

        WeChatResponse response = JSONObject.parseObject(body, WeChatResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 删除标签
     *
     * @param id 标签id
     * @return
     * @throws WeChatException     微信验证异常
     * @throws ParamCheckException 参数检查不通过
     */
    public WeChatResponse delete(
        @NotNull(message = "标签id不能为空") Integer id) throws WeChatException, ParamCheckException {
        String url = MessageFormat.format(UrlConstant.DELETE_TAGS_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"删除标签url:{}",url);
        JSONObject requestParam = new JSONObject();
        JSONObject param = new JSONObject();
        param.put("id", id);
        requestParam.put("tag", param);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(requestParam);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger,"删除标签应答结果:{}",body);

        WeChatResponse response = JSONObject.parseObject(body, WeChatResponse.class);


        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 获取标签下粉丝列表
     *
     * @param tagId      标签id
     * @param nextOpenId 第一个拉取的OPENID，不填默认从头开始拉取
     * @return
     * @throws WeChatException     微信验证异常
     * @throws ParamCheckException 参数检查不通过
     */
    public TagUserResponse getTagUser(@NotNull(message = "标签id不能为空") Integer tagId,
        String nextOpenId) throws WeChatException, ParamCheckException {
        String url = MessageFormat.format(UrlConstant.GET_TAG_USERS_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"获取标签下粉丝列表url:{}",url);
        JSONObject requestParam = new JSONObject();
        requestParam.put("tagid", tagId);
        requestParam.put("next_openid", nextOpenId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(requestParam);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger,"取标签下粉丝列表应答结果:{}",body);

        TagUserResponse response = JSONObject.parseObject(body, TagUserResponse.class);

        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 用户批量打标签
     * 标签功能目前支持公众号为用户打上最多20个标签。
     *
     * @param openIdList 用户列表，每次传入的 openid 列表个数不能超过50个
     * @param tagId      标签id
     * @return
     * @throws WeChatException     微信验证异常
     * @throws ParamCheckException 参数检查不通过
     */
    public WeChatResponse batchTagging(
        @NotEmpty(message = "用户id列表不能为空") @Size(max = 50, message = "每次传入的 openid 列表个数不能超过50个") List<String> openIdList,
        @NotNull(message = "标签id不能为空") Integer tagId) throws WeChatException, ParamCheckException {

        String url = MessageFormat.format(UrlConstant.USERS_BATCH_TAGGING_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"用户批量打标签url:{}",url);
        JSONObject requestParam = new JSONObject();
        requestParam.put("openid_list", openIdList);
        requestParam.put("tagid", tagId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(requestParam);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger,"用户批量打标签应答结果:{}",body);

        WeChatResponse response = JSONObject.parseObject(body, WeChatResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;

    }

    /**
     * 批量取消用户标签
     * 标签功能目前支持公众号为用户打上最多20个标签。
     *
     * @param openIdList 用户列表，每次传入的 openid 列表个数不能超过50个
     * @param tagId      标签id
     * @return
     * @throws WeChatException     微信验证异常
     * @throws ParamCheckException 参数检查不通过
     */
    public WeChatResponse batchUnTagging(
        @NotEmpty(message = "用户id列表不能为空") @Size(max = 50, message = "每次传入的 openid 列表个数不能超过50个") List<String> openIdList,
        @NotNull(message = "标签id不能为空") Integer tagId) throws WeChatException, ParamCheckException {

        String url = MessageFormat.format(UrlConstant.USERS_BATCH_UNTAGGING_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"批量取消用户标签url:{}",url);
        JSONObject requestParam = new JSONObject();
        requestParam.put("openid_list", openIdList);
        requestParam.put("tagid", tagId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(requestParam);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger,"批量取消用户标签应答结果:{}",body);

        WeChatResponse response = JSONObject.parseObject(body, WeChatResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;

    }

    /**
     * 获取用户身上的标签，最多标签数量 20
     * @param openId 用户openId
     * @return 标签列表
     * @throws WeChatException     微信验证异常
     * @throws ParamCheckException 参数检查不通过
     */
    public List<Integer> getUserTags(String openId) throws WeChatException, ParamCheckException{
        String url = MessageFormat.format(UrlConstant.GET_USER_TAG_URL, weChatTokenRepository.get());
        LoggerUtils.debug(logger,"获取用户身上的标签url:{}",url);
        JSONObject requestParam = new JSONObject();
        requestParam.put("openid", openId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = JSON.toJSONString(requestParam);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger,"获取用户身上的标签应答结果:{}",body);

        WeChatResponse response = JSONObject.parseObject(body, WeChatResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        JSONObject responseObject = JSONObject.parseObject(body);

        return responseObject.getJSONArray("tagid_list").toJavaList(Integer.class);
    }

}
