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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;

/**
 * todo 上传图文消息图片，还有问题
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/material/permanent/api_uploadimage.html">原始文档</a>
 */
public class MediaManager {
    private static final Logger logger = LoggerFactory.getLogger(MediaManager.class);
    private final WeChatTokenRepository weChatTokenRepository;

    /**
     * 我不太清楚我为什么 RestTemplate 上传会失败，改为url上传就可以
     * @param weChatTokenRepository token 管理器
     */
    public MediaManager( WeChatTokenRepository weChatTokenRepository) {
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


        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (DataOutputStream out = new DataOutputStream(conn.getOutputStream());
             FileInputStream fis = new FileInputStream(media)) {

            // 写文件头
            out.writeBytes("--" + boundary + "\r\n");
            out.writeBytes("Content-Disposition: form-data; name=\"media\"; filename=\"" + media.getName() + "\"\r\n");
            out.writeBytes("Content-Type: application/octet-stream\r\n\r\n");

            // 写文件内容
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.writeBytes("\r\n");

            // 结束 boundary
            out.writeBytes("--" + boundary + "--\r\n");
            out.flush();
        }

        // 读取响应
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
           String result = response.toString();
            UploadImageResponse uploadImageResponse = JSON.parseObject(result, UploadImageResponse.class);
            logger.debug("上传图文消息图片结果: {}", result);
            return uploadImageResponse;
        }
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
