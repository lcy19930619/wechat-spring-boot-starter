package net.jlxxw.wechat.controller;

import net.jlxxw.wechat.component.EventBus;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.security.WeChatServerSecurityCheck;
import net.jlxxw.wechat.util.LoggerUtils;
import net.jlxxw.wechat.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 微信入口核心控制器
 *
 * @author chunyang.leng
 * @date 2021/1/20 12:48 下午
 */
@Controller
@ConditionalOnProperty(value = "we-chat.netty.server.enable-netty", havingValue = "false")
public class WeChatMessageController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatMessageController.class);
    @Autowired
    private EventBus eventBus;
    @Autowired(required = false)
    private WeChatServerSecurityCheck weChatServerSecurityCheck;
    @Autowired
    private WeChatProperties weChatProperties;

    @RequestMapping("weChat")
    public void coreController(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (weChatProperties.isEnableWeChatCallBackServerSecurityCheck() && weChatServerSecurityCheck != null) {
            // 开启微信回调ip安全检查时执行
            final String ipAddress = NetworkUtil.getIpAddress(request);
            if (!weChatServerSecurityCheck.isSecurity(ipAddress)) {
                // 非法ip，不予处理
                LoggerUtils.warn(logger,"发现非法ip访问:{}",ipAddress);
                return;
            }
        }

        String result = eventBus.dispatcher(request);
        response.setCharacterEncoding("UTF-8");
        final PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
    }
}
