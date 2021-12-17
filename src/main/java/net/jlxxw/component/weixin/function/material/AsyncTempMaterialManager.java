package net.jlxxw.component.weixin.function.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.component.weixin.enums.MaterialEnum;
import net.jlxxw.component.weixin.exception.WeiXinException;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import net.jlxxw.component.weixin.util.LoggerUtils;
import net.jlxxw.component.weixin.vo.TempMaterialVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.Consumer;

import static net.jlxxw.component.weixin.constant.UrlConstant.DOWN_TEMP_MATERIAL;
import static net.jlxxw.component.weixin.constant.UrlConstant.UPLOAD_TEMP_MATERIAL;

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
    private WeiXinTokenManager weiXinTokenManager;

    @PostConstruct
    public void postConstruct() {
        Objects.requireNonNull(webClient);
        Objects.requireNonNull(weiXinTokenManager);
    }

    /**
     * 上传临时素材
     *
     * @param materialEnum   素材类型
     * @param file           文件内容
     */
    public Mono<TempMaterialVO> upload(MaterialEnum materialEnum, File file) {

        String tokenFromLocal = weiXinTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(file);

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UPLOAD_TEMP_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增临时素材url:{}", url);

        // 发送请求
        Mono<JSONObject> mono = webClient
                // POST 请求
                .post()
                // 请求路径
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(param))
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(JSONObject.class);

        return mono.map(obj -> {
            if (obj.getInteger("errcode") != null) {
                // 存在错误码，说明上传出错
                WeiXinException weiXinException = new WeiXinException(JSON.toJSONString(obj));
                weiXinException.setErrorCode(obj.getInteger("errcode"));
                throw weiXinException;
            }
            LoggerUtils.debug(logger, "新增临时素材微信返回结果:{}", JSON.toJSONString(obj));
            // 封装返回对象
            TempMaterialVO materialVO = new TempMaterialVO();
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
    public Mono<TempMaterialVO> upload(MaterialEnum materialEnum, URI uri, Consumer<TempMaterialVO> callbackMethod) {
        String tokenFromLocal = weiXinTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(Paths.get(uri));

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UPLOAD_TEMP_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增临时素材url:{}", url);

        // 发送请求
        Mono<JSONObject> mono = webClient
                // POST 请求
                .post()
                // 请求路径
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(param))
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(JSONObject.class);

        return mono.map(obj -> {
            if (obj.getInteger("errcode") != null) {
                // 存在错误码，说明上传出错上传出错
                WeiXinException weiXinException = new WeiXinException(JSON.toJSONString(obj));
                weiXinException.setErrorCode(obj.getInteger("errcode"));
                throw weiXinException;
            }
            LoggerUtils.debug(logger, "新增临时素材微信返回结果:{}", JSON.toJSONString(obj));
            // 封装返回对象
            TempMaterialVO materialVO = new TempMaterialVO();
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
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(DOWN_TEMP_MATERIAL, token, mediaId);
        LoggerUtils.debug(logger, "下载临时素材,不含视频url:{}", url);

        return webClient
                // GET 请求
                .get()
                // 请求路径
                .uri(url)
                .accept(mediaType)
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(Resource.class);

    }


    /**
     * 下载临时视频素材
     *
     * @param mediaId 媒体文件ID
     */
    public Mono<String> downloadVideo(String mediaId) {
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(DOWN_TEMP_MATERIAL, token, mediaId);
        LoggerUtils.debug(logger, "下载临时视频素材url:{}", url);

        Mono<JSONObject> mono = webClient
                // GET 请求
                .get()
                // 请求路径
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(JSONObject.class);

        return mono.map(obj -> {
            String videoUrl = obj.getString("video_url");
            LoggerUtils.debug(logger, "下载视频临时素材微信出现异常，mediaId:{},返回:{}", mediaId, JSON.toJSONString(obj));
            return videoUrl;
        });
    }
}
