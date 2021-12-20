package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.material.TempMaterialResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import net.jlxxw.wechat.util.WebClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.function.Consumer;

/**
 * 临时素材管理
 *
 * @author chunyang.leng
 * @date 2021-03-05 5:53 下午
 */
@Component
public class AsyncTempMaterialManager {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTempMaterialManager.class);
    @Autowired
    private WebClient webClient;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

    @Autowired
    private WebClientUtils webClientUtils;

    /**
     * 上传临时素材
     *
     * @param materialEnum 素材类型
     * @param file         文件内容
     */
    public Mono<TempMaterialResponse> upload(MaterialEnum materialEnum, File file) {

        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(file);

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UrlConstant.UPLOAD_TEMP_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增临时素材url:{}", url);

        Mono<JSONObject> mono = webClientUtils.sendPostFormUrlEncoded(url, param, JSONObject.class);

        return mono.map(obj -> {
            if (obj.getInteger("errcode") != null) {
                // 存在错误码，说明上传出错
                WeChatException weChatException = new WeChatException(JSON.toJSONString(obj));
                weChatException.setErrorCode(obj.getInteger("errcode"));
                throw weChatException;
            }
            LoggerUtils.debug(logger, "新增临时素材微信返回结果:{}", JSON.toJSONString(obj));
            // 封装返回对象
            TempMaterialResponse materialVO = new TempMaterialResponse();
            materialVO.setMediaId(obj.getString("media_id"));
            materialVO.setCreatedAt(obj.getLong("created_at"));
            materialVO.setType(obj.getString("type"));
            return materialVO;
        });
    }


    /**
     * 上传临时素材
     *
     * @param materialEnum   素材类型
     * @param uri            uri链接
     * @param callbackMethod 回调方法
     */
    public Mono<TempMaterialResponse> upload(MaterialEnum materialEnum, URI uri, Consumer<TempMaterialResponse> callbackMethod) {
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(Paths.get(uri));

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UrlConstant.UPLOAD_TEMP_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增临时素材url:{}", url);

        // 发送请求
        Mono<JSONObject> mono = webClientUtils.sendPostFormUrlEncoded(url, param, JSONObject.class);

        return mono.map(obj -> {
            if (obj.getInteger("errcode") != null) {
                // 存在错误码，说明上传出错上传出错
                WeChatException weChatException = new WeChatException(JSON.toJSONString(obj));
                weChatException.setErrorCode(obj.getInteger("errcode"));
                throw weChatException;
            }
            LoggerUtils.debug(logger, "新增临时素材微信返回结果:{}", JSON.toJSONString(obj));
            // 封装返回对象
            TempMaterialResponse materialVO = new TempMaterialResponse();
            materialVO.setMediaId(obj.getString("media_id"));
            materialVO.setCreatedAt(obj.getLong("created_at"));
            materialVO.setType(obj.getString("type"));
            return materialVO;
        });
    }

    /**
     * 下载临时素材,不含视频
     *
     * @param mediaId 媒体文件ID
     */
    public Mono<Resource> download(String mediaId, MediaType mediaType) {
        String token = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.DOWN_TEMP_MATERIAL, token, mediaId);
        LoggerUtils.debug(logger, "下载临时素材,不含视频url:{}", url);
        return webClientUtils.sendGet(url, Resource.class, mediaType);
    }


    /**
     * 下载临时视频素材
     *
     * @param mediaId 媒体文件ID
     */
    public Mono<String> downloadVideo(String mediaId) {
        String token = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.DOWN_TEMP_MATERIAL, token, mediaId);
        LoggerUtils.debug(logger, "下载临时视频素材url:{}", url);

        Mono<JSONObject> mono = webClientUtils.sendGet(url, JSONObject.class, MediaType.APPLICATION_JSON);

        return mono.map(obj -> {
            String videoUrl = obj.getString("video_url");
            LoggerUtils.debug(logger, "下载视频临时素材微信出现异常，mediaId:{},返回:{}", mediaId, JSON.toJSONString(obj));
            return videoUrl;
        });
    }
}
