package net.jlxxw.component.weixin.enums;

/**
 * @author chunyang.leng
 * @date 2021-06-12 7:25 上午
 */
public enum AesExceptionEnum {
    /**
     * 签名验证错误
     */
    VALIDATE_SIGNATURE_ERROR(-40001,"签名验证错误"),

    /**
     * xml解析失败
     */
    PARSE_XML_ERROR(-40002,"xml解析失败"),


    /**
     * SymmetricKey非法
     */
    ILLEGAL_AES_KEY(-40004,"SymmetricKey非法"),

    /**
     * appid校验失败
     */
    VALIDATE_APPID_ERROR(-40005,"appid校验失败"),

    /**
     * aes加密失败
     */
    ENCRYPT_AES_ERROR(-40006,"aes加密失败"),

    /**
     * aes解密失败
     */
    DECRYPT_AES_ERROR(-40007,"aes解密失败"),

    /**
     * 解密后得到的buffer非法
     */
    ILLEGAL_BUFFER(-40008,"解密后得到的buffer非法"),




    /**
     * sha加密生成签名失败
     */
    COMPUTE_SIGNATURE_ERROR(-40003,"sha加密生成签名失败"),

    ;
    private final int code;

    private final String message;

    AesExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
