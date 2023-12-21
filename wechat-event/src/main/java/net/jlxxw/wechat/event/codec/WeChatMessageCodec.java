package net.jlxxw.wechat.event.codec;

import net.jlxxw.wechat.exception.AesException;

public interface WeChatMessageCodec {
    /**
     * 加密传输
     * @param input 明文
     * @return 密文
     */
    String encrypt(String input) throws AesException;

    /**
     * 信息解密
     * @param uri  uri
     * @param data  密文
     * @return 明文
     */
    String decrypt(String uri, String data) throws AesException;
}
