package net.jlxxw.wechat.controller;

import net.jlxxw.wechat.component.EventBus;
import net.jlxxw.wechat.properties.WeiXinProperties;
import net.jlxxw.wechat.security.WeiXinServerSecurityCheck;
import net.jlxxw.wechat.util.NetworkUtil;
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
@ConditionalOnProperty(value = "weixin.netty.server.enable-netty",havingValue = "false")
public class WeiXinMessageController {
    @Autowired
    private EventBus eventBus;
    @Autowired(required = false)
    private WeiXinServerSecurityCheck weiXinServerSecurityCheck;
    @Autowired
    private WeiXinProperties weiXinProperties;

    @RequestMapping("weixin")
    public void coreController(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(weiXinProperties.isEnableWeiXinCallBackServerSecurityCheck() && weiXinServerSecurityCheck != null){
            // 开启微信回调ip安全检查时执行
            final String ipAddress = NetworkUtil.getIpAddress(request);
            if(!weiXinServerSecurityCheck.isSecurity(ipAddress)){
                // 非法ip，不予处理
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
