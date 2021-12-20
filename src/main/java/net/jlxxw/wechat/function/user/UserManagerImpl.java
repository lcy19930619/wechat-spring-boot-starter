package net.jlxxw.wechat.function.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.response.user.SubscriptionResponse;
import net.jlxxw.wechat.enums.LanguageEnum;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chunyang.leng
 * @date 2021/1/25 6:44 下午
 */
@Lazy
@DependsOn({"weChatProperties", "weChatTokenManager", "webClientUtils"})
@Component
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

    /**
     * 获取全部用户的openId
     *
     * @return
     */
    @Override
    public Set<String> findAll() {
        Set<String> openIdSet = new HashSet<>();
        String token = weChatTokenManager.getTokenFromLocal();

        int current = 0;
        int totle = 1;
        String nextOpenId = "";
        while (current < totle) {
            String url = MessageFormat.format(UrlConstant.FIND_ALL_USER_OPENID, token, nextOpenId);

            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            String body = forEntity.getBody();
            final JSONObject resultData = JSONObject.parseObject(body);
            if (resultData.getInteger("errcode") == null || resultData.getInteger("errcode") == 0) {
                totle = resultData.getInteger("total");
                current += resultData.getInteger("count");
                nextOpenId = resultData.getString("next_openid");

                final JSONArray array = resultData.getJSONArray("openid");
                if (!CollectionUtils.isEmpty(array)) {
                    array.forEach(o -> {
                        openIdSet.add(o.toString());
                    });
                }
            } else {
                WeChatException weChatException = new WeChatException("获取全部openId失败，微信返回值:" + resultData.toJSONString());
                weChatException.setErrorCode(resultData.getInteger("errcode"));
                throw weChatException;
            }
        }
        return openIdSet;
    }

    /**
     * 批量根据用户的openId获取用户的基本信息
     *
     * @param openIdList openId列表
     * @return 用户基本信息
     */
    @Override
    public List<SubscriptionResponse> findUserInfo(List<String> openIdList, LanguageEnum languageEnum) {
        List<SubscriptionResponse> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(openIdList)) {
            return result;
        }
        String token = weChatTokenManager.getTokenFromLocal();
        String url = UrlConstant.BATCH_USER_INFO_URL + token;
        int size = 100;
        // 统计需要分成几次
        int countFor = countFor(openIdList.size());
        int start = 0;
        int end = 0;

        //每100条处理一次
        for (int i = 0; i < countFor; i++) {
            start = i * size;
            end = Math.min((i + 1) * size, openIdList.size());
            List<String> tempList = openIdList.subList(start, end);

            JSONObject requestParam = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (String openId : tempList) {
                JSONObject param = new JSONObject();
                param.put("openid", openId);
                param.put("lang", languageEnum.getCode());
                jsonArray.add(param);
            }
            requestParam.put("user_list", jsonArray);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String json = JSON.toJSONString(requestParam);
            HttpEntity<String> request = new HttpEntity<>(json, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
            JSONObject resultData = JSONObject.parseObject(responseEntity.getBody());
            if (resultData.getInteger("errcode") == null || resultData.getInteger("errcode") == 0) {
                JSONArray infoList = resultData.getJSONArray("user_info_list");
                List<SubscriptionResponse> subscriptionUsers = JSONArray.parseArray(JSON.toJSONString(infoList), SubscriptionResponse.class);
                result.addAll(subscriptionUsers);
                logger.info("用户数量总计:{},当前执行数量:{}", openIdList.size(), end);
            } else {
                WeChatException weChatException = new WeChatException("批量获取用户信息失败，微信返回值:" + resultData.toJSONString() + ",用户openId列表:" + JSON.toJSONString(tempList));
                weChatException.setErrorCode(resultData.getInteger("errcode"));
                throw weChatException;
            }
        }

        return result;
    }

    /**
     * 获取一个用户的基本信息
     *
     * @param openId       用户的openId
     * @param languageEnum 返回语言
     * @return 用户的基本信息
     */
    @Override
    public SubscriptionResponse getUserInfo(String openId, LanguageEnum languageEnum) {
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        String token = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.ONE_USER_INFO_URL, token, openId, languageEnum.getCode());
        ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity(url, JSONObject.class);
        JSONObject body = forEntity.getBody();
        if (body.getInteger("errcode") == null || body.getInteger("errcode") == 0) {
            // 执行成功
            return body.toJavaObject(SubscriptionResponse.class);
        }
        WeChatException weChatException = new WeChatException("获取用户信息失败，微信返回值:" + body.toJSONString() + ",用户openId:" + openId);
        weChatException.setErrorCode(body.getInteger("errcode"));
        throw weChatException;
    }


    private int countFor(int size) {
        //批量插入数据大小
        int i = 100;

        if (size % i == 0) {
            return size / i;
        } else {
            return size / i + 1;
        }
    }
}
