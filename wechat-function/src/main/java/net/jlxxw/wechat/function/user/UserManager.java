package net.jlxxw.wechat.function.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.LanguageEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.user.SubscriptionResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 用户信息管理
 *
 * @author chunyang.leng
 * @date 2021/1/25 6:44 下午
 */
public class UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    public RestTemplate restTemplate;
    public WeChatTokenRepository weChatTokenRepository;

    public UserManager(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
    }

    /**
     * 获取全部用户的openId
     *
     * @return 全部在关用户的openId
     * @throws WeChatException 微信服务端验证异常
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/Getting_a_User_List.html">文档地址</a>
     */
    public Set<String> findAll() throws WeChatException {
        Set<String> openIdSet = new HashSet<>();

        int current = 0;
        int totle = 1;
        String nextOpenId = "";
        while (current < totle) {
            String token = weChatTokenRepository.get();

            String url = MessageFormat.format(UrlConstant.FIND_ALL_USER_OPENID, token, nextOpenId);

            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            String body = forEntity.getBody();
            final JSONObject resultData = JSONObject.parseObject(body);
            if (resultData.getInteger("errcode") == null || resultData.getInteger("errcode") == 0) {
                totle = resultData.getInteger("total");
                current += resultData.getInteger("count");
                nextOpenId = resultData.getString("next_openid");

                logger.info("total:" + totle + ",current:" + current + ",nextId:" + nextOpenId + ",size:" + openIdSet.size());

                JSONObject data = resultData.getJSONObject("data");
                if (Objects.nonNull(data)) {
                    final JSONArray array = data.getJSONArray("openid");
                    if (!CollectionUtils.isEmpty(array)) {
                        array.forEach(o -> {
                            openIdSet.add(o.toString());
                        });
                    }
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
     * 获取一个用户的基本信息
     *
     * @param openId       用户的openId
     * @param languageEnum 返回语言
     * @return 用户的基本信息
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 方法调用前，参数检查异常
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/User_Management/Get_users_basic_information_UnionID.html#UinonId">文档地址</a>
     */
    public SubscriptionResponse getUserInfo(@NotBlank(message = "待查询待openId不应为空") String openId,
                                            @NotNull(message = "语言选择不应为空") LanguageEnum languageEnum) throws WeChatException, ParamCheckException {
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        String token = weChatTokenRepository.get();
        String url = MessageFormat.format(UrlConstant.ONE_USER_INFO_URL, token, openId, languageEnum.getCode());
        ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity(url, JSONObject.class);
        JSONObject body = forEntity.getBody();

        SubscriptionResponse response = body.toJavaObject(SubscriptionResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }
}
