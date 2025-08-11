package net.jlxxw.wechat.function.media;

import com.alibaba.fastjson.JSON;
import jakarta.validation.constraints.NotNull;
import net.jlxxw.wechat.constant.UrlConstant;
import net.jlxxw.wechat.dto.media.UploadImageDTO;
import net.jlxxw.wechat.repository.token.WeChatTokenRepository;
import net.jlxxw.wechat.response.media.UploadImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;

/**
 * todo 上传图文消息图片，还有问题
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/material/permanent/api_uploadimage.html">原始文档</a>
 */
public class MediaManager {
    private static final Logger logger = LoggerFactory.getLogger(MediaManager.class);
    private final RestTemplate restTemplate;
    private final WeChatTokenRepository weChatTokenRepository;

    /**
     *
     * @param restTemplate 上传文件比较特殊，如果不存在 链接工厂，需要使用：new RestTemplate(new HttpComponentsClientHttpRequestFactory()
     * @param weChatTokenRepository token 管理器
     */
    public MediaManager(RestTemplate restTemplate, WeChatTokenRepository weChatTokenRepository) {
        this.restTemplate = restTemplate;
        this.weChatTokenRepository = weChatTokenRepository;
    }


    /**
     * 上传图文消息图片
     * 本接口用于上传图文消息内所需的图片
     * 该接口所上传的图片，不占用公众号的素材库中图片数量的100000个的限制，图片仅支持jpg/png格式，大小必须在1MB以下。
     * 图文消息支持正文中插入自己账号和其他公众号已群发文章链接的能力。
     *
     * @param uploadImageDTO 上传图片参数
     * @return 上传结果，包含图片URL
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/material/permanent/api_uploadimage.html">文档地址</a>
     */
    public UploadImageResponse uploadImage(@NotNull(message = "上传图片参数不能为空") UploadImageDTO uploadImageDTO) throws IOException {
        String token = getToken();
        String url = MessageFormat.format(UrlConstant.UPLOAD_IMAGE_URL, token);


        File media = uploadImageDTO.getMedia();

        //设置请求体，注意是LinkedMultiValueMap
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();

        //设置上传文件
        FileSystemResource fileSystemResource = new FileSystemResource(media);
        data.add("media", fileSystemResource);

        //上传文件,设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.setContentLength(fileSystemResource.getFile().length());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data,httpHeaders);
        //设置表单信息
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        UploadImageResponse response = JSON.parseObject(responseBody, UploadImageResponse.class);
        logger.debug("上传图文消息图片结果: {}", responseBody);
        return response;
    }


    /**
     * 获取访问令牌
     *
     * @return 访问令牌
     */
    private String getToken() {
        return weChatTokenRepository.get();
    }

}
