package net.jlxxw.component.weixin.controller;

import net.jlxxw.component.weixin.properties.WeiXinProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信服务器认证
 *
 * @author chunyang.leng
 * @date 2021/1/26 12:56 下午
 */
@RestController
@RequestMapping("/")
public class WeiXinDevelopmentCertification {
    @Autowired
    private WeiXinProperties weiXinProperties;

    private static final Logger logger = LoggerFactory.getLogger(WeiXinDevelopmentCertification.class);

    @GetMapping("verifyToken")
    public String verifyToken(HttpServletRequest request) throws NoSuchAlgorithmException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        logger.info("接收到微信请求：signature={},timestamp={},nonce={},echostr={}",msgSignature,msgTimestamp,msgNonce,echostr);
        if (verify(msgSignature, msgTimestamp, msgNonce)) {
            logger.info("验证通过");
            return echostr;
        }
        logger.info("验证失败");
        return null;
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
        // 这里的 WXPublicConstants.TOKEN 填写你自己设置的Token就可以了
        String signature = sha1Sign(weiXinProperties.getVerifyToken(), timeStamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new RuntimeException("token认证失败");
        }
        return true;
    }

    private static String sha1Sign(String token, String timestamp, String nonce) throws NoSuchAlgorithmException {
        if(StringUtils.isBlank(token)){
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
