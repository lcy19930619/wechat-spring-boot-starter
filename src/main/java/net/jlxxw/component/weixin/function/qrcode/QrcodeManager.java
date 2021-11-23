package net.jlxxw.component.weixin.function.qrcode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.component.weixin.constant.UrlConstant;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import net.jlxxw.component.weixin.response.qrcode.QrCodeResult;
import net.jlxxw.component.weixin.response.qrcode.TempQrCodeResult;
import net.jlxxw.component.weixin.util.WebClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.function.Consumer;

/**
 * 二维码管理
 * @author chunyang.leng
 * @date 2021-03-05 5:52 下午
 */
@Component
public class QrcodeManager {
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    @Autowired
    private WebClientUtils webClientUtils;
    /**
     * 创建一个临时二维码,参数为自定义的字符串
     * @param eventKey 自定义的event key
     * @param expireSecond 过期时间（秒）
     * @param consumer 二维码生成之后的处理者
     *
     */
    public void createTempStringQrcode(String eventKey, Long expireSecond, Consumer<TempQrCodeResult> consumer){
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


        Mono<TempQrCodeResult> mono = webClientUtils.sendPostJSON(url, JSON.toJSONString(object), TempQrCodeResult.class);
        mono.subscribe(consumer);
    }

    /**
     * 创建一个临时二维码,参数为自定义id
     * @param id 自定义的id
     * @param expireSecond 过期时间（秒）
     * @param consumer 二维码生成之后的处理者
     *
     */
    public void createTempIdQrcode(Long id, Long expireSecond, Consumer<TempQrCodeResult> consumer){
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

        Mono<TempQrCodeResult> mono = webClientUtils.sendPostJSON(url, JSON.toJSONString(object), TempQrCodeResult.class);
        mono.subscribe(consumer);
    }


    /**
     * 创建一个永久二维码,参数为自定义字符串
     * @param eventKey 自定义的字符串
     * @param consumer 二维码生成之后的处理者
     *
     */
    public void createStringQrcode(String eventKey, Consumer<QrCodeResult> consumer){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_TEMP_QRCODE_URL,token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_SCENE");
        JSONObject scene = new JSONObject();
        scene.put("scene_str", eventKey);
        JSONObject  actionInfo = new JSONObject();
        actionInfo.put("scene",scene);
        object.put("action_info",actionInfo);

        Mono<QrCodeResult> mono = webClientUtils.sendPostJSON(url, JSON.toJSONString(object), QrCodeResult.class);
        mono.subscribe(consumer);
    }


    /**
     * 创建一个永久二维码,参数为自定义id
     * @param id 自定义的id
     * @param consumer 二维码生成之后的处理者
     *
     */
    public void createIdQrcode(Long id,  Consumer<QrCodeResult> consumer){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_TEMP_QRCODE_URL,token);
        JSONObject object = new JSONObject();
        object.put("action_name", "QR_SCENE");
        JSONObject scene = new JSONObject();
        scene.put("scene_id", id);
        JSONObject  actionInfo = new JSONObject();
        actionInfo.put("scene",scene);
        object.put("action_info",actionInfo);

        Mono<QrCodeResult> mono = webClientUtils.sendPostJSON(url, JSON.toJSONString(object), QrCodeResult.class);
        mono.subscribe(consumer);
    }


}
