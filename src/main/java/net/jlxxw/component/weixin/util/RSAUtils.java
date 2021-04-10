package net.jlxxw.component.weixin.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

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

@Slf4j
public class RSAUtils {
    private static final String RSA_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIAb3yUmOroh014J0rJzcehnm8w88BYqe0bsFEoGhunzjkYCo4HgB6tlEBbmw1oylc+kmpQunATHSVemfMOsos8CAwEAAQ==";
    private static final String RSA_PRIVATE_KEY = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAgBvfJSY6uiHTXgnSsnNx6GebzDzwFip7RuwUSgaG6fOORgKjgeAHq2UQFubDWjKVz6SalC6cBMdJV6Z8w6yizwIDAQABAkBf3XuEfzEfLETRcCRdKYqp0S6DDW7UB4IstmkQZAX9dxcxkhnilJbAuwzF1HfTmH7AO6vfIIZjKdECFQlLttiBAiEAvqGVYvuFIpt3lU+sZ2fHO3mCDM8nPukFlEbk2WmWmK8CIQCsCdBHUVaOIbKXCOkAdeuYrjcevwYPrSS65ubu4bDf4QIgWnWcyPKn06tIjL7ZBdy2Kx/WubNXYT/8WMdnc0/qmZsCICNsKdxlXQMK4TDD/uW/YfEf/e1wu5jCt8tb+7S396lBAiACZe7ohL+ZajUqW5OAbVplgBaM+E24E/poXaGRpr/SaQ==";

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


    public static void main(String[] args) throws Exception {
        String encode = encode("房价疯狂了多少就发看留守",RSA_PUBLIC_KEY);
        System.out.println(encode);
        System.out.println(decode(encode,RSA_PRIVATE_KEY));
    }
}
