package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.material.TempMaterialResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * 临时素材管理
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Get_temporary_materials.html">接口文档地址</a>
 * @author chunyang.leng
 * @date 2022-08-12 3:33 PM
 */
public class TempMaterialManager {
    private static final Logger logger = LoggerFactory.getLogger(TempMaterialManager.class);

    private RestTemplate restTemplate;
    private WeChatTokenManager weChatTokenManager;

    public TempMaterialManager(RestTemplate restTemplate, WeChatTokenManager weChatTokenManager) {
        this.restTemplate = restTemplate;
        this.weChatTokenManager = weChatTokenManager;
    }

    /**
     * 上传临时素材
     * @param materialEnum 素材类型
     * @param file 上传的文件
     * @return
     * @throws WeChatException
     */
    public TempMaterialResponse upload( @NotNull(message = "文件类型不能为空") MaterialEnum materialEnum,
                                        @NotNull(message = "文件不能为空") File file) throws WeChatException{
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        //参数
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.UPLOAD_TEMP_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增临时素材url:{}", url);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String result = responseEntity.getBody();
        LoggerUtils.debug(logger, "新增临时素材微信返回结果:{}", result);

        TempMaterialResponse response = JSON.parseObject(result,TempMaterialResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(result);
        }
        return response;
    }

    /**
     * 上传临时素材
     * @param materialEnum 素材类型
     * @param fileData 上传的文件二进制数据
     * @param fileName  文件名称
     * @return
     * @throws WeChatException
     */
    public TempMaterialResponse upload( @NotNull(message = "文件类型不能为空") MaterialEnum materialEnum,
                                        @NotNull(message = "文件数据不能为空") byte[] fileData,
                                        @NotBlank(message = "文件名称不能为空") String fileName) throws WeChatException{

        Resource resource = new AbstractResource() {
            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(fileData);
            }

            @Override
            public String getFilename() {
                return fileName;
            }
        };
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        //参数
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.UPLOAD_TEMP_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增临时素材url:{}", url);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String result = responseEntity.getBody();
        LoggerUtils.debug(logger, "新增临时素材微信返回结果:{}", result);

        TempMaterialResponse response = JSON.parseObject(result,TempMaterialResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(result);
        }
        return response;
    }


    /**
     * 下载普通素材
     * @param mediaId 素材id
     * @return 下载文件的二进制数据
     */
    public byte[] downloadMaterial(@NotBlank(message = "素材id不能为空") String mediaId) throws WeChatException{
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.DOWN_TEMP_MATERIAL, tokenFromLocal, mediaId);
        LoggerUtils.debug(logger, "下载临时素材url:{}", url);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> entity = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(headers), byte[].class);
        // 返回数据
        byte[] body = entity.getBody();
        HttpHeaders httpHeaders = entity.getHeaders();
        MediaType contentType = httpHeaders.getContentType();
        if (MediaType.APPLICATION_JSON.equalsTypeAndSubtype(contentType)){
            // 是 json 类型
            String json = new String(body, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(json);

            WeChatResponse weChatResponse = JSON.toJavaObject(jsonObject,WeChatResponse.class);
            if (!weChatResponse.isSuccessful()){
                throw new WeChatException(weChatResponse);
            }
            // 如果是视频，重新下载
            String videoUrl = jsonObject.getString("video_url");
            ResponseEntity<byte[]> videoResponse = restTemplate.exchange(videoUrl, HttpMethod.GET,new HttpEntity<>(headers), byte[].class);
            return videoResponse.getBody();
        }
        try{
            JSONObject jsonObject = JSON.parseObject(new String(body, StandardCharsets.UTF_8));
            WeChatResponse weChatResponse = jsonObject.toJavaObject(WeChatResponse.class);
            throw new WeChatException(weChatResponse);
        }catch (JSONException e){
            // 忽略
        }
        return body;
    }

    /**
     * 下载高清语音素材
     * 公众号可以使用本接口获取从 JSSDK 的uploadVoice接口上传的临时语音素材，
     * 格式为speex，16K采样率。该音频比上文的临时素材获取接口（格式为amr，8K采样率）更加清晰，
     * 适合用作语音识别等对音质要求较高的业务。
     *
     * 如果 speex 音频格式不符合业务需求，开发者可在获取后，再自行于本地对该语音素材进行转码。
     * 转码请使用 speex 的官方解码库 http://speex.org/downloads/ ，
     * 并结合微信的解码库（含示例代码：<a href="http://wximg.gtimg.com/shake_tv/mpwiki/declib.zip "speex解码库"" target="_blank">下载地址）。
     *
     * @param mediaId 素材id
     * @return 下载文件的二进制数据
     */
    public byte[] downloadHDVoice(String mediaId) throws WeChatException{
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.DOWN_HD_VOICE, tokenFromLocal, mediaId);
        LoggerUtils.debug(logger, "下载高清语音素材url:{}", url);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> entity = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(headers), byte[].class);
        // 返回数据，在下面写到输出流里面
        byte[] body = entity.getBody();

        HttpHeaders httpHeaders = entity.getHeaders();
        MediaType contentType = httpHeaders.getContentType();
        if (MediaType.APPLICATION_JSON.equalsTypeAndSubtype(contentType)){
            // 是 json 类型
            String json = new String(body, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(json);
            WeChatResponse weChatResponse = JSON.toJavaObject(jsonObject,WeChatResponse.class);
            if (!weChatResponse.isSuccessful()){
                throw new WeChatException(weChatResponse);
            }
        }
        return body;
    }


}
