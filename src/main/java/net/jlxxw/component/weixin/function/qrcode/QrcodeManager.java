package net.jlxxw.component.weixin.function.qrcode;

import net.jlxxw.component.weixin.constant.UrlConstant;
import net.jlxxw.component.weixin.function.token.WeiXinTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 二维码管理
 * @author chunyang.leng
 * @date 2021-03-05 5:52 下午
 */
@Component
public class QrcodeManager {
    @Autowired
    private WeiXinTokenManager weiXinTokenManager;
    /**
     * 创建一个临时二维码
     * @param eventKey 自定义的event key
     * @param expireSecond 过期时间（秒）
     *
     * @return 二维码的url
     */
    public String createTempQrcode(String eventKey,Integer expireSecond){
        String token = weiXinTokenManager.getTokenFromLocal();
        String url = MessageFormat.format(UrlConstant.CREATE_TEMP_QRCODE_URL,token);

        // todo
        return null;
    }

}
