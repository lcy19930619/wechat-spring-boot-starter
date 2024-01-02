package net.jlxxw.wechat.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.util.LoggerUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信入口核心控制器，该接口仅在禁用netty服务时启用
 *
 * @author chunyang.leng
 * @date 2021/1/20 12:48 下午
 */
@Controller
public class WeChatMessageController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatMessageController.class);
    @Autowired
    private EventBus eventBus;
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 微信信息核心转发入口
     *
     * @param request  用于获取微信传递的数据信息
     * @param response 用于向微信写入应答数据信息
     * @throws Exception
     */
    @RequestMapping("${wechat.event.server.web.core-controller-url:/weChat}")
    public void coreController(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String method = request.getMethod();
        if ("get".equalsIgnoreCase(method)) {
            // 验证签名
            String verifyToken = verifyToken(request);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(verifyToken);
                writer.flush();
            } catch (IOException e) {
                logger.error("输出数据出现未知异常",e);
            }
        }


        try (InputStream inputStream = request.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String uri = request.getRequestURI();
            String string = request.getQueryString();
            response.setCharacterEncoding("UTF-8");
            eventBus.dispatcher(bytes, uri + "?" + string, (responseXML) -> {
                try (PrintWriter writer = response.getWriter()) {
                    writer.write(responseXML);
                    writer.flush();
                } catch (IOException e) {
                    logger.error("输出数据出现未知异常",e);
                }
            });

        }
    }



    private String verifyToken(HttpServletRequest request) throws NoSuchAlgorithmException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        LoggerUtils.info(logger,"接收到微信请求：signature={},timestamp={},nonce={},echostr={}", msgSignature, msgTimestamp, msgNonce, echostr);
        if (verify(msgSignature, msgTimestamp, msgNonce)) {
            LoggerUtils.info(logger,"验证通过");
            return echostr;
        }
        LoggerUtils.info(logger,"验证失败");
        return "";
    }

    /**
     * 验证Token
     *
     * @param msgSignature 签名串，对应URL参数的signature
     * @param timeStamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @return 是否为安全签名
     */
    private boolean verify(String msgSignature, String timeStamp, String nonce) throws NoSuchAlgorithmException {
        String signature = sha1Sign(weChatProperties.getVerifyToken(), timeStamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new RuntimeException("token认证失败");
        }
        return true;
    }

    /**
     * 进行 sha1 签名运算
     * @param token 项目中配置的 微信验证token
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String sha1Sign(String token, String timestamp, String nonce) throws NoSuchAlgorithmException {
        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("verify-token不能为空");
        }
        String[] array = new String[]{token, timestamp, nonce};
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < 3; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        // SHA1签名生成
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes());
        byte[] digest = md.digest();

        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();

    }
}
