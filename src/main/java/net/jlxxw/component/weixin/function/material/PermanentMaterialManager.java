package net.jlxxw.component.weixin.function.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.component.weixin.enums.MaterialEnum;
import net.jlxxw.component.weixin.exception.WeiXinException;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import net.jlxxw.component.weixin.util.LoggerUtils;
import net.jlxxw.component.weixin.vo.PermanentMaterialVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
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
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.Consumer;

import static net.jlxxw.component.weixin.constant.UrlConstant.*;

/**
 * 永久素材管理
 *
 * @author chunyang.leng
 * @date 2021-03-05 5:53 下午
 */
@Lazy
@DependsOn({"weiXinProperties","weiXinTokenManager","webClientUtils"})
@Component
public class PermanentMaterialManager {
    private static final Logger logger = LoggerFactory.getLogger(PermanentMaterialManager.class);
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
     * 上传永久素材（不包含视频和图文）
     *
     * @param materialEnum   素材类型
     * @param file           文件内容
     * @param callbackMethod 回调方法
     */
    public void upload(MaterialEnum materialEnum, File file, Consumer<PermanentMaterialVO> callbackMethod) {

        String tokenFromLocal = weiXinTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(file);

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增永久素材url:{}", url);
        postRequest(callbackMethod, param, url);

    }


    /**
     * 上传永久素材，不含图文和视频
     *
     * @param materialEnum   素材类型
     * @param uri            uri链接
     * @param callbackMethod 回调方法
     */
    public void upload(MaterialEnum materialEnum, URI uri, Consumer<PermanentMaterialVO> callbackMethod) {

        String tokenFromLocal = weiXinTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(Paths.get(uri));

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());

        LoggerUtils.debug(logger, "新增永久素材url:{}", url);

        // 发送请求
        postRequest(callbackMethod, param, url);
    }

    /**
     * 下载永久素材，不含图文和视频
     *
     * @param mediaId
     * @param callback
     */
    public void download(String mediaId, Consumer<InputStream> callback) {
        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("mediaId", mediaId);

        String url = MessageFormat.format(DOWNLOAD_PERMANENT_MATERIAL, weiXinTokenManager.getTokenFromLocal());
        LoggerUtils.debug(logger, "下载永久素材url:{}", url);

        // 发送请求
        Mono<Resource> mono = webClient
                // POST 请求
                .post()
                // 请求路径
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(param))
                // 获取响应体
                .retrieve()
                //响应数据类型转换
                .bodyToMono(Resource.class);


        mono.subscribe(resource -> {
            try (InputStream inputStream = resource.getInputStream();) {
                callback.accept(inputStream);
            } catch (Exception e) {
                LoggerUtils.error(logger, "下载永久素材出现异常,mediaId:" + mediaId, e);
                throw new WeiXinException(e.getMessage());
            }
        });
    }

    /**
     * 删除永久素材
     *
     * @param mediaId
     */
    public void delete(String mediaId) {
        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("mediaId", mediaId);

        String url = MessageFormat.format(DELETE_PERMANENT_MATERIAL, weiXinTokenManager.getTokenFromLocal());
        LoggerUtils.debug(logger, "删除永久素材url:{}", url);

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

        mono.subscribe(obj -> {
            if (obj.getInteger("errcode") != 0) {
                WeiXinException weiXinException = new WeiXinException(JSON.toJSONString(obj));
                weiXinException.setErrorCode(obj.getInteger("errcode"));
                throw weiXinException;
            }
        });

    }


    /**
     * 发送post请求
     * @param callbackMethod 回调的处理方法
     * @param param 请求参数
     * @param url url地址
     */
    private void postRequest(Consumer<PermanentMaterialVO> callbackMethod, MultiValueMap<String, Object> param, String url) {
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

        mono.subscribe(obj -> {
            if (obj.getInteger("errcode") != null) {
                // 存在错误码，说明上传出错
                WeiXinException weiXinException = new WeiXinException(JSON.toJSONString(obj));
                weiXinException.setErrorCode(obj.getInteger("errcode"));
                throw weiXinException;
            }
            LoggerUtils.debug(logger, "新增永久素材微信返回结果:{}", JSON.toJSONString(obj));
            // 封装返回对象
            PermanentMaterialVO materialVO = new PermanentMaterialVO();
            materialVO.setMediaId(obj.getString("media_id"));
            materialVO.setUrl(obj.getString("url"));

            // 调用回调方法
            callbackMethod.accept(materialVO);
        });
    }

}
