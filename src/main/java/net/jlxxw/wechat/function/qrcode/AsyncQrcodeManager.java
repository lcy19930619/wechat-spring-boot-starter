package net.jlxxw.wechat.function.qrcode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.qrcode.QrCodeDTO;
import net.jlxxw.wechat.dto.qrcode.TempQrCodeDTO;
import net.jlxxw.wechat.function.token.WeiXinTokenManager;
import net.jlxxw.wechat.util.WebClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

/**
 * 二维码管理
 * @author chunyang.leng
 * @date 2021-03-05 5:52 下午
 */
@Lazy
@DependsOn({"weiXinProperties","weiXinTokenManager","webClientUtils"})
@Component
public class AsyncQrcodeManager {
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    @Autowired
    private WebClientUtils webClientUtils;
    /**
     * 创建一个临时二维码,参数为自定义的字符串
     * @param eventKey 自定义的event key
     * @param expireSecond 过期时间（秒）
     *
     */
    public Mono<TempQrCodeDTO> createTempStringQrcode(String eventKey, Long expireSecond){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_TEMP_QRCODE_URL,token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_SCENE");
        object.put("expire_seconds", expireSecond);

        JSONObject scene = new JSONObject();
        scene.put("scene_str", eventKey);
        JSONObject  actionInfo = new JSONObject();
        actionInfo.put("scene",scene);
        object.put("action_info",actionInfo);
        return webClientUtils.sendPostJSON(url, JSON.toJSONString(object), TempQrCodeDTO.class);
    }

    /**
     * 创建一个临时二维码,参数为自定义id
     * @param id 自定义的id
     * @param expireSecond 过期时间（秒）
     *
     */
    public Mono<TempQrCodeDTO> createTempIdQrcode(Long id, Long expireSecond){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_TEMP_QRCODE_URL,token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_SCENE");
        object.put("expire_seconds", expireSecond);
        JSONObject scene = new JSONObject();
        scene.put("scene_id", id);
        JSONObject  actionInfo = new JSONObject();
        actionInfo.put("scene",scene);
        object.put("action_info",actionInfo);

        return webClientUtils.sendPostJSON(url, JSON.toJSONString(object), TempQrCodeDTO.class);
    }


    /**
     * 创建一个永久二维码,参数为自定义字符串
     * @param eventKey 自定义的字符串
     *
     */
    public Mono<QrCodeDTO> createStringQrcode(String eventKey){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_TEMP_QRCODE_URL,token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_SCENE");
        JSONObject scene = new JSONObject();
        scene.put("scene_str", eventKey);
        JSONObject  actionInfo = new JSONObject();
        actionInfo.put("scene",scene);
        object.put("action_info",actionInfo);

        return webClientUtils.sendPostJSON(url, JSON.toJSONString(object), QrCodeDTO.class);
    }


    /**
     * 创建一个永久二维码,参数为自定义id
     * @param id 自定义的id
     *
     */
    public Mono<QrCodeDTO>  createIdQrcode(Long id){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_TEMP_QRCODE_URL,token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_SCENE");
        JSONObject scene = new JSONObject();
        scene.put("scene_id", id);
        JSONObject  actionInfo = new JSONObject();
        actionInfo.put("scene",scene);
        object.put("action_info",actionInfo);

        return webClientUtils.sendPostJSON(url, JSON.toJSONString(object), QrCodeDTO.class);
    }


}
