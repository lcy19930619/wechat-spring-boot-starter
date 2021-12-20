package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.WeiXinException;
import net.jlxxw.wechat.function.token.WeiXinTokenManager;
import net.jlxxw.wechat.response.WeiXinResponse;
import net.jlxxw.wechat.response.material.PermanentMaterialResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import net.jlxxw.wechat.util.WebClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.text.MessageFormat;

/**
 * 永久素材管理
 *
 * @author chunyang.leng
 * @date 2021-03-05 5:53 下午
 */
@DependsOn({"weiXinProperties", "weiXinTokenManager", "webClientUtils"})
@Component
public class AsyncPermanentMaterialManager {
    private static final Logger logger = LoggerFactory.getLogger(AsyncPermanentMaterialManager.class);
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    @Autowired
    private WebClientUtils webClientUtils;


    /**
     * 上传永久素材（不包含视频和图文）
     *
     * @param materialEnum 素材类型
     * @param file         文件内容
     */
    public Mono<PermanentMaterialResponse> upload(MaterialEnum materialEnum, File file) {

        String tokenFromLocal = weiXinTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(file);

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UrlConstant.UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增永久素材url:{}", url);
        return postRequest(param, url);

    }


    /**
     * 上传永久素材，不含图文和视频
     *
     * @param materialEnum 素材类型
     * @param uri          uri链接
     */
    public Mono<PermanentMaterialResponse> upload(MaterialEnum materialEnum, URI uri) {

        String tokenFromLocal = weiXinTokenManager.getTokenFromLocal();
        FileSystemResource resource = new FileSystemResource(Paths.get(uri));

        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());

        String url = MessageFormat.format(UrlConstant.UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());

        LoggerUtils.debug(logger, "新增永久素材url:{}", url);

        // 发送请求
        return postRequest(param, url);
    }

    /**
     * 下载永久素材，不含图文和视频
     *
     * @param mediaId 素材id
     */
    public Mono<Resource> download(String mediaId) {
        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("mediaId", mediaId);

        String url = MessageFormat.format(UrlConstant.DOWNLOAD_PERMANENT_MATERIAL, weiXinTokenManager.getTokenFromLocal());
        LoggerUtils.debug(logger, "下载永久素材url:{}", url);

        return webClientUtils.sendPostFormUrlEncoded(url, param, Resource.class);
    }

    /**
     * 删除永久素材
     *
     * @param mediaId 素材id
     */
    public Mono<WeiXinResponse> delete(String mediaId) {
        // 封装请求参数
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("mediaId", mediaId);

        String url = MessageFormat.format(UrlConstant.DELETE_PERMANENT_MATERIAL, weiXinTokenManager.getTokenFromLocal());
        LoggerUtils.debug(logger, "删除永久素材url:{}", url);

        Mono<JSONObject> mono = webClientUtils.sendPostFormUrlEncoded(url, param, JSONObject.class);

        return mono.map((obj) -> {
            if (obj.getInteger("errcode") != 0) {
                WeiXinException weiXinException = new WeiXinException(JSON.toJSONString(obj));
                weiXinException.setErrorCode(obj.getInteger("errcode"));
                throw weiXinException;
            }
            return obj.toJavaObject(WeiXinResponse.class);
        });

    }


    /**
     * 发送post请求
     *
     * @param param 请求参数
     * @param url   url地址
     */
    private Mono<PermanentMaterialResponse> postRequest(MultiValueMap<String, Object> param, String url) {

        Mono<JSONObject> mono = webClientUtils.sendPostFormUrlEncoded(url, param, JSONObject.class);

        return mono.map(obj -> {
            if (obj.getInteger("errcode") != null) {
                // 存在错误码，说明上传出错
                WeiXinException weiXinException = new WeiXinException(JSON.toJSONString(obj));
                weiXinException.setErrorCode(obj.getInteger("errcode"));
                throw weiXinException;
            }
            LoggerUtils.debug(logger, "新增永久素材微信返回结果:{}", JSON.toJSONString(obj));
            // 封装返回对象
            PermanentMaterialResponse materialVO = new PermanentMaterialResponse();
            materialVO.setMediaId(obj.getString("media_id"));
            materialVO.setUrl(obj.getString("url"));

            return materialVO;
        });
    }

}
