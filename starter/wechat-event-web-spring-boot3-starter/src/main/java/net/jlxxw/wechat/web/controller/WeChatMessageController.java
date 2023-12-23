package net.jlxxw.wechat.web.controller;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.jlxxw.wechat.event.component.EventBus;
import net.jlxxw.wechat.properties.WeChatProperties;
import net.jlxxw.wechat.security.store.IpSegmentStore;
import net.jlxxw.wechat.util.LoggerUtils;
import net.jlxxw.wechat.web.util.NetworkUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.io.PrintWriter;

/**
 * 微信入口核心控制器，该接口仅在禁用netty服务时启用
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
    private IpSegmentStore ipSegmentStore;
    @Autowired
    private WeChatProperties weChatProperties;

    @PostConstruct
    private void postConstruct(){
        LoggerUtils.info(logger,"已禁用netty服务，并启用web控制器");
    }
    /**
     * 微信信息核心转发入口
     * @param request 用于获取微信传递的数据信息
     * @param response 用于向微信写入应答数据信息
     * @throws Exception
     */
    @RequestMapping("${we-chat.core-controller-url:weChat}")
    public void coreController(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (weChatProperties.isEnableWeChatCallBackServerSecurityCheck() && ipSegmentStore != null) {
            // 开启微信回调ip安全检查时执行
            final String ipAddress = NetworkUtil.getIpAddress(request);
            if (!ipSegmentStore.security(ipAddress)) {
                // 非法ip，不予处理
                LoggerUtils.warn(logger,"发现非法ip访问:{}",ipAddress);
                return;
            }
        }

        try (InputStream inputStream = request.getInputStream()){
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String uri = request.getRequestURI();
            String string = request.getQueryString();
            String result =  eventBus.dispatcher(bytes,uri+"?"+string);
            response.setCharacterEncoding("UTF-8");
            final PrintWriter writer = response.getWriter();
            writer.write(result);
            writer.flush();
            writer.close();
        }
    }
}
