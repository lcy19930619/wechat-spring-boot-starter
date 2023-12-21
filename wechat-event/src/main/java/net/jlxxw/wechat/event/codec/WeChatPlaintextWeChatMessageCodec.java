package net.jlxxw.wechat.event.codec;

import net.jlxxw.wechat.exception.AesException;

/**
 * 明文编解码器
 */
public class WeChatPlaintextWeChatMessageCodec implements WeChatMessageCodec {
    /**
     * 加密传输
     *
     * @param input 明文
     * @return 密文
     */
    @Override
    public String encrypt(String input) {
        return input;
    }

    /**
     * 信息解密
     *
     * @param uri  uri
     * @param data 密文
     * @return 明文
     */
    @Override
    public String decrypt(String uri, String data) throws AesException {
        return data;
    }
}
