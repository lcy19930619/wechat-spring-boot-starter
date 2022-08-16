package net.jlxxw.wechat.function.material;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.text.MessageFormat;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.enums.MaterialEnum;
import net.jlxxw.wechat.exception.WeChatException;
import net.jlxxw.wechat.function.token.WeChatTokenManager;
import net.jlxxw.wechat.response.material.TempMaterialResponse;
import net.jlxxw.wechat.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
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
     * 上传临时素材
     * @param materialEnum 素材类型
     * @param file 上传的文件
     * @return
     * @throws WeChatException
     */
    public TempMaterialResponse upload( @Validated MaterialEnum materialEnum,
                                        @Validated File file) throws WeChatException{
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        //参数
        param.add("media", resource);
        param.add("type", materialEnum.name().toLowerCase());
        String tokenFromLocal = weChatTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.UPLOAD_PERMANENT_MATERIAL, tokenFromLocal, materialEnum.name().toLowerCase());
        LoggerUtils.debug(logger, "新增永久素材url:{}", url);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        String result = responseEntity.getBody();
        LoggerUtils.debug(logger, "新增永久素材微信返回结果:{}", result);

        TempMaterialResponse response = JSON.parseObject(result,TempMaterialResponse.class);
        if (!response.isSuccessful()){
            throw new WeChatException(result);
        }
        return response;
    }

}
