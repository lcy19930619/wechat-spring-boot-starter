package net.jlxxw.wechat.function.qrcode;

import com.alibaba.fastjson.JSONObject;
import java.text.MessageFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.qrcode.QrCodeResponse;
import net.jlxxw.wechat.response.qrcode.TempQrCodeResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 二维码管理
 *
 * @author chunyang.leng
 * @date 2021-03-05 5:52 下午
 */
@DependsOn(WeChatTokenManager.BEAN_NAME)
@Component
public class QrcodeManager {

    private static final Logger logger = LoggerFactory.getLogger(QrcodeManager.class);
    @Autowired
    private WeChatTokenManager weChatTokenManager;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 创建一个临时二维码,参数为自定义的字符串
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 调用接口前，参数检查异常
     * @param eventKey     自定义的event key
     * @param expireSecond 过期时间（秒）最长可以设置为在二维码生成后的30天（即2592000秒）后过期,超过会自动转换为2592000,最小有效值为60，小于60会自动转换为60
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">接口文档</a>
     */
    public TempQrCodeResponse createTempStringQrcode( @NotBlank(message = "自定义的eventKey不能为空") String eventKey,
                                                      @NotNull(message = "自定义的过期时间不能为空")
                                                      @Max(value = 2592000,message = "最大有效值不能超过2592000秒(30天)，超过会自动转换为2592000")
                                                      @Min(value = 60,message = "最小有效值为60，小于60会自动转换为60") Long expireSecond) throws WeChatException,ParamCheckException{
        String token = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_QRCODE_URL, token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_STR_SCENE");
        object.put("expire_seconds", expireSecond);

        JSONObject scene = new JSONObject();
        scene.put("scene_str", eventKey);
        JSONObject actionInfo = new JSONObject();
        actionInfo.put("scene", scene);
        object.put("action_info", actionInfo);

        String json = object.toJSONString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger, "创建一个临时二维码,eventKey:{},返回结果:{}",eventKey, body);

        TempQrCodeResponse response = JSONObject.parseObject(body, TempQrCodeResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(response);
        }
        return response;
    }

    /**
     * 创建一个临时二维码,参数为自定义id
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 调用接口前，参数检查异常
     * @param id           自定义的id
     * @param expireSecond 过期时间（秒）最长可以设置为在二维码生成后的30天（即2592000秒）后过期,超过会自动转换为2592000,最小有效值为60，小于60会自动转换为60
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">接口文档</a>
     */
    public TempQrCodeResponse createTempIdQrcode(@NotNull(message = "自定义的id不能为空") Long id,
                                                 @NotNull(message = "自定义的过期时间不能为空")
                                                 @Max(value = 2592000,message = "最大有效值不能超过2592000秒(30天)，超过会自动转换为2592000")
                                                 @Min(value = 60,message = "最小有效值为60，小于60会自动转换为60") Long expireSecond) throws WeChatException,ParamCheckException {
        String token = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_QRCODE_URL, token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_SCENE");
        object.put("expire_seconds", expireSecond);
        JSONObject scene = new JSONObject();
        scene.put("scene_id", id);
        JSONObject actionInfo = new JSONObject();
        actionInfo.put("scene", scene);
        object.put("action_info", actionInfo);


        String json = object.toJSONString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger, "创建一个临时二维码,id:{},返回结果:{}",id, body);

        TempQrCodeResponse response = JSONObject.parseObject(body, TempQrCodeResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(response);
        }
        return response;
    }


    /**
     * 创建一个永久二维码,参数为自定义字符串
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 调用接口前，参数检查异常
     * @param eventKey 自定义的字符串
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">接口文档</a>
     */
    public QrCodeResponse createStringQrcode(@NotBlank(message = "自定义的eventKey，不应该为空") String eventKey) throws WeChatException,ParamCheckException{
        String token = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_QRCODE_URL, token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_LIMIT_STR_SCENE");
        JSONObject scene = new JSONObject();
        scene.put("scene_str", eventKey);
        JSONObject actionInfo = new JSONObject();
        actionInfo.put("scene", scene);
        object.put("action_info", actionInfo);

        String json = object.toJSONString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger, "创建一个永久二维码,eventKey:{},返回结果:{}",eventKey, body);

        QrCodeResponse response = JSONObject.parseObject(body, QrCodeResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(response);
        }
        return response;
    }


    /**
     * 创建一个永久二维码,参数为自定义id
     *
     * @param id 自定义的id
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">接口文档</a>
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 调用接口前，参数检查异常
     */
    public QrCodeResponse createIdQrcode(@NotNull(message = "二维码id不能为空") Long id) throws WeChatException, ParamCheckException {
        String token = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_QRCODE_URL, token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_LIMIT_SCENE");
        JSONObject scene = new JSONObject();
        scene.put("scene_id", id);
        JSONObject actionInfo = new JSONObject();
        actionInfo.put("scene", scene);
        object.put("action_info", actionInfo);

        String json = object.toJSONString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        LoggerUtils.debug(logger, "创建一个永久二维码,id:{},返回结果:{}",id, body);

        QrCodeResponse response = JSONObject.parseObject(body, QrCodeResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(response);
        }
        return response;
    }


}
