package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.ParamCheckException;
import net.jlxxw.wechat.exception.WeChatException;

import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.WeChatResponse;
import net.jlxxw.wechat.response.material.PermanentMaterialResponse;
import net.jlxxw.wechat.log.util.LoggerUtils;
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
 * 永久素材管理工具
 * @author chunyang.leng
 * @date 2022-08-12 4:44 PM
 */
public class PermanentMaterialManager {
    private static final Logger logger = LoggerFactory.getLogger(TempMaterialManager.class);

    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    public PermanentMaterialManager(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
    }

    /**
     * 上传永久素材
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Adding_Permanent_Assets.html">接口文档</a>
     * @param materialEnum 素材类型
     * @param file 上传的文件
     * @param videoTitle 上传视频时，视频素材的标题
     * @param videoIntroduction 上传视频时，视频素材的描述
     * @return
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 方法调用前，参数检查异常
     */
    public PermanentMaterialResponse upload( @NotNull(message = "文件类型不能为空") MaterialEnum materialEnum,
                                             @NotNull(message = "文件不能为空") File file,
                                             String videoTitle,
                                             String videoIntroduction) throws WeChatException, ParamCheckException {
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        //参数
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());
        if (materialEnum.equals(MaterialEnum.VIDEO)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title",videoTitle);
            jsonObject.put("introduction",videoIntroduction);
            param.add("description",JSON.toJSONString(jsonObject));
        }

        String tokenFromLocal = weChatTokenRepository.get();
        String url = MessageFormat.format(UrlConstant.UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug( "新增永久素材url:{}", url);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String result = responseEntity.getBody();
        LoggerUtils.debug( "新增永久素材微信返回结果:{}", result);

        PermanentMaterialResponse response = JSON.parseObject(result,PermanentMaterialResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(result);
        }
        return response;
    }

    /**
     * 上传永久素材
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Adding_Permanent_Assets.html">接口文档</a>
     * @param materialEnum 素材类型
     * @param fileData 上传的文件二进制数据
     * @param fileName  文件名称
     * @param videoTitle 上传视频时，视频素材的标题
     * @param videoIntroduction 上传视频时，视频素材的描述
     * @return
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 方法调用前，参数检查异常
     */
    public PermanentMaterialResponse upload( @NotNull(message = "文件类型不能为空") MaterialEnum materialEnum,
                                             @NotNull(message = "文件数据不能为空") byte[] fileData,
                                             @NotBlank(message = "文件名称不能为空") String fileName,
                                             String videoTitle,
                                             String videoIntroduction) throws WeChatException, ParamCheckException{

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
        if (materialEnum.equals(MaterialEnum.VIDEO)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title",videoTitle);
            jsonObject.put("introduction",videoIntroduction);
            param.add("description",JSON.toJSONString(jsonObject));
        }
        String tokenFromLocal = weChatTokenRepository.get();
        String url = MessageFormat.format(UrlConstant.UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug( "新增永久素材url:{}", url);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String result = responseEntity.getBody();
        LoggerUtils.debug( "新增永久素材微信返回结果:{}", result);

        PermanentMaterialResponse response = JSON.parseObject(result,PermanentMaterialResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(result);
        }
        return response;
    }

    /**
     * 获取永久素材
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Getting_Permanent_Assets.html">接口文档</a>
     * @param mediaId 素材id
     * @return 素材的二进制流
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 方法调用前，参数检查异常
     */
    public byte[] download(@NotBlank(message = "下载素材id不能为空") String mediaId) throws WeChatException, ParamCheckException{
        String tokenFromLocal = weChatTokenRepository.get();
        String url = MessageFormat.format(UrlConstant.DOWNLOAD_PERMANENT_MATERIAL, tokenFromLocal);
        LoggerUtils.debug( "下载永久素材url:{}", url);
        HttpHeaders headers = new HttpHeaders();
        JSONObject media = new JSONObject();
        media.put("media_id",mediaId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(media.toJSONString(),headers);
        ResponseEntity<byte[]> entity = restTemplate.exchange(url, HttpMethod.POST,requestEntity, byte[].class);
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
            String videoUrl = jsonObject.getString("down_url");
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
     * 删除素材
     * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Deleting_Permanent_Assets.html">接口文档</a>
     * @param mediaId 素材id
     * @throws WeChatException 微信服务端验证异常
     * @throws ParamCheckException 方法调用前，参数检查异常
     * @return
     */
    public WeChatResponse deleteMaterial(@NotBlank(message = "删除素材id不能为空") String mediaId) throws WeChatException, ParamCheckException{
        String tokenFromLocal = weChatTokenRepository.get();
        String url = MessageFormat.format(UrlConstant.DELETE_PERMANENT_MATERIAL, tokenFromLocal);
        LoggerUtils.debug( "删除永久素材url:{}", url);
        HttpHeaders headers = new HttpHeaders();
        JSONObject media = new JSONObject();
        media.put("media_id",mediaId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(media.toJSONString(),headers);
        ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.POST,requestEntity, String.class);
        String body = entity.getBody();
        LoggerUtils.debug( "删除永久素材:{},返回值:", mediaId,body);
        WeChatResponse weChatResponse = JSON.parseObject(body, WeChatResponse.class);
        if (!weChatResponse.isSuccessful()){
            throw new WeChatException(weChatResponse);
        }
        return weChatResponse;
    }
}
