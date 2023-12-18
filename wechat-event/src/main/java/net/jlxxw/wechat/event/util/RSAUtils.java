package net.jlxxw.wechat.event.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密解密
 *
 * @author chunyang.leng
 * @create 2021-04-10 09:35
 **/

public class RSAUtils {
    private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);
    /**
     * 加密
     *
     * @param src       原始字符串
     * @param publicKey 公钥
     * @return 加密后的字符串
     */
    public static String encode(String src, String publicKey) throws Exception {
        //公钥加密
        byte[] rsaPublicKeyEncoded = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKeyEncoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(x509EncodedKeySpec));
        byte[] resultBytes = cipher.doFinal(src.getBytes());
        return Hex.encodeHexString(resultBytes);
    }

    /**
     * 解密
     *
     * @param src        原始字符串
     * @param privateKey 私钥
     * @return 加密后的字符串
     */
    public static String decode(String src, String privateKey) throws Exception {
        byte[] rsaPrivateKeyEncoded = Base64.getDecoder().decode(privateKey);
        //私钥解密
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec
                = new PKCS8EncodedKeySpec(rsaPrivateKeyEncoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(pkcs8EncodedKeySpec));
        byte[] resultBytes = cipher.doFinal(Hex.decodeHex(src.toCharArray()));
        return new String(resultBytes);
    }

}
