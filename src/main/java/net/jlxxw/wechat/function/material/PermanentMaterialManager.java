package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.material.PermanentMaterialResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 永久素材管理工具
 * @author chunyang.leng
 * @date 2022-08-12 4:44 PM
 */
@DependsOn( "weChatTokenManager")
@Component
public class PermanentMaterialManager {
    private static final Logger logger = LoggerFactory.getLogger(TempMaterialManager.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeChatTokenManager weChatTokenManager;

    /**
     * 上传永久素材
     * @param materialEnum 素材类型
     * @param file 上传的文件
     * @param videoTitle 上传视频时，视频素材的标题
     * @param videoIntroduction 上传视频时，视频素材的描述
     * @return
     * @throws WeChatException
     */
    public PermanentMaterialResponse upload( @NotNull(message = "文件类型不能为空") MaterialEnum materialEnum,
                                             @NotNull(message = "文件不能为空") File file,
                                             String videoTitle,
                                             String videoIntroduction) throws WeChatException{
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

        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增永久素材url:{}", url);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String result = responseEntity.getBody();
        LoggerUtils.debug(logger, "新增永久素材微信返回结果:{}", result);

        PermanentMaterialResponse response = JSON.parseObject(result,PermanentMaterialResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(result);
        }
        return response;
    }

    /**
     * 上传永久素材
     * @param materialEnum 素材类型
     * @param fileData 上传的文件二进制数据
     * @param fileName  文件名称
     * @param videoTitle 上传视频时，视频素材的标题
     * @param videoIntroduction 上传视频时，视频素材的描述
     * @return
     * @throws WeChatException
     */
    public PermanentMaterialResponse upload( @NotNull(message = "文件类型不能为空") MaterialEnum materialEnum,
                                             @NotNull(message = "文件数据不能为空") byte[] fileData,
                                             @NotBlank(message = "文件名称不能为空") String fileName,
                                             String videoTitle,
                                             String videoIntroduction) throws WeChatException{

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
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增永久素材url:{}", url);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String result = responseEntity.getBody();
        LoggerUtils.debug(logger, "新增永久素材微信返回结果:{}", result);

        PermanentMaterialResponse response = JSON.parseObject(result,PermanentMaterialResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(result);
        }
        return response;
    }

}
