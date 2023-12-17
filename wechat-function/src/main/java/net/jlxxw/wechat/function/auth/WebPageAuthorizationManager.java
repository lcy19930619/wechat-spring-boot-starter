package net.jlxxw.wechat.function.auth;

import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.auth.AuthScope;
import net.jlxxw.wechat.enums.LanguageEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.auth.AuthAccessTokenResponse;
import net.jlxxw.wechat.response.auth.AuthUserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * 微信网页授权管理
 * 网页授权流程分为四步：
 * <p>
 * 1、引导用户进入授权页面同意授权，获取code
 * <p>
 * 2、通过 code 换取网页授权access_token（与基础支持中的access_token不同）
 * <p>
 * 3、如果需要，开发者可以刷新网页授权access_token，避免过期
 * <p>
 * 4、通过网页授权access_token和 openid 获取用户基本信息（支持 UnionID 机制）
 * <p>
 *
 * @author chunyang.leng
 * @date 2022-08-18 2:42 PM
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html">接口文档</a>
 */
@DependsOn(WeChatTokenManager.BEAN_NAME)
@Component
public class WebPageAuthorizationManager {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 第一步：用户同意授权，获取code
     * 在确保微信公众账号拥有授权作用域（scope参数）的权限的前提下
     * （已认证服务号，默认拥有 scope 参数中的snsapi_base和snsapi_userinfo 权限），引导关注者打开如下页面：
     * <p>
     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
     * <p>
     * 若提示“该链接无法访问”，请检查参数是否填写错误，是否拥有 scope 参数对应的授权作用域权限。
     * <p>
     * 尤其注意：由于授权操作安全等级较高，所以在发起授权请求时，微信会对授权链接做正则强匹配校验，如果链接的参数顺序不对，授权页面将无法正常访问
     * <p>
     * 如果用户同意授权，页面将跳转至 redirect_uri/?code=微信携带的code参数&state=用户自定义的参数
     *
     * @param redirectUri 授权后重定向的回调链接地址
     * @param scope       应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过 openid 拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
     * @param state       重定向后会带上 state 参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     * @return 引导用户授权的链接地址，可以重定向到此地址，引导用户授权
     * @throws ParamCheckException 方法执行前，参数检查未通过
     * @throws UnsupportedEncodingException URL编码失败
     */
    public String getAuthorizeUrl(@NotBlank(message = "重定向地址不能为空")
    @Pattern(regexp = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$", message = "重定向地址必须是一个链接地址") String redirectUri,
        @NotNull(message = "授权作用域不能为空") AuthScope scope,
        @Size(max = 128,message = "最大长度128字节") String state) throws ParamCheckException, UnsupportedEncodingException {
        String encode = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString());
        return MessageFormat.format(UrlConstant.WEB_PAGE_AUTH_URL, weChatProperties.getAppId(), encode, scope.getCode(), state);
    }

    /**
     * 第二步：通过 code 换取网页授权access_token
     * 首先请注意，这里通过 code 换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
     * 公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，也获取到了openid，
     * snsapi_base式的网页授权流程即到此为止。
     * <p>
     * 尤其注意：由于公众号的 secret 和获取到的access_token安全级别都非常高，必须只保存在服务器，不允许传给客户端。
     * 后续刷新access_token、通过access_token获取用户信息等步骤，也必须从服务器发起。
     *
     * @param code 步骤一，用户同意授权后，回调到重定向地址所携带的参数
     * @return 微信授权 token 信息
     * @throws WeChatException 微信验证失败
     * @throws ParamCheckException 方法执行前，参数检查未通过
     */
    public AuthAccessTokenResponse getAuthorizeAccessToken(
        @NotBlank(message = "微信授权code不能为空") String code) throws WeChatException , ParamCheckException{
        String url = MessageFormat.format(UrlConstant.OAUTH2_ACCESS_TOKEN_URL, weChatProperties.getAppId(), weChatProperties.getSecret(), code);
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String body = entity.getBody();
        AuthAccessTokenResponse response = JSONObject.parseObject(body, AuthAccessTokenResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 第三步：刷新access_token（如果需要）
     * 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，
     * refresh_token有效期为30天，当refresh_token失效之后，需要用户重新授权。
     *
     * @param token 步骤二中获取的token
     * @return 微信授权 token 信息
     * @throws WeChatException 微信验证失败
     * @throws ParamCheckException 方法执行前，参数检查未通过
     */
    public AuthAccessTokenResponse refreshToken(
        @NotBlank(message = "待刷新待token不能为空") String token) throws WeChatException, ParamCheckException {
        String url = MessageFormat.format(UrlConstant.REFRESH_ACCESS_TOKEN_URL, weChatProperties.getAppId(), token);
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String body = entity.getBody();
        AuthAccessTokenResponse response = JSONObject.parseObject(body, AuthAccessTokenResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 第四步：拉取用户信息(需 scope 为 snsapi_userinfo)
     * 如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和 openid 拉取用户信息了。
     *
     * @param accessToken 步骤二获取的token
     * @param openId      用户的openId
     * @return 通过微信授权，获取的用户信息
     * @throws WeChatException 微信验证失败
     * @throws ParamCheckException 方法执行前，参数检查未通过
     */
    public AuthUserInfoResponse getUserInfo(
        @NotBlank(message = "用户的id不能为空") String openId,
        @NotBlank(message = "授权token不能为空") String accessToken,
        @NotNull(message = "语言不能为空") LanguageEnum languageEnum) throws WeChatException, ParamCheckException {
        String url = MessageFormat.format(UrlConstant.GET_USER_INFO_BY_OAUTH2_ACCESS_TOKEN_URL, accessToken, openId, languageEnum.getCode());
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String body = entity.getBody();
        AuthUserInfoResponse response = JSONObject.parseObject(body, AuthUserInfoResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 检验授权凭证（access_token）是否有效
     *
     * @param accessToken 步骤二获取的 token
     * @param openId      用户openId
     * @return 微信验证结果
     * @throws WeChatException 微信验证失败
     * @throws ParamCheckException 方法执行前，参数检查未通过
     */
    public WeChatResponse checkAccessToken(@NotBlank(message = "用户的id不能为空") String openId,
        @NotBlank(message = "授权token不能为空") String accessToken) throws WeChatException, ParamCheckException {
        String url = MessageFormat.format(UrlConstant.CHECK_OAUTH2_ACCESS_TOKEN_URL, accessToken, openId);
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String body = entity.getBody();
        WeChatResponse response = JSONObject.parseObject(body, WeChatResponse.class);
        if (!response.isSuccessful()) {
            throw new WeChatException(response);
        }
        return response;
    }

}
