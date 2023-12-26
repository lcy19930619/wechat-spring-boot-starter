package net.jlxxw.wechat.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.jlxxw.wechat.event.component.EventBus;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * 微信入口核心控制器，该接口仅在禁用netty服务时启用
 *
 * @author chunyang.leng
 * @date 2021/1/20 12:48 下午
 */
@Controller
public class WeChatMessageController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatMessageController.class);
    @Autowired
    private EventBus eventBus;

    /**
     * 微信信息核心转发入口
     *
     * @param request  用于获取微信传递的数据信息
     * @param response 用于向微信写入应答数据信息
     * @throws Exception
     */
    @RequestMapping("${wechat.event.server.web.core-controller-url:/weChat}")
    public void coreController(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try (InputStream inputStream = request.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String uri = request.getRequestURI();
            String string = request.getQueryString();
            response.setCharacterEncoding("UTF-8");
            eventBus.dispatcher(bytes, uri + "?" + string, (responseXML) -> {
                try (PrintWriter writer = response.getWriter()) {
                    writer.write(responseXML);
                    writer.flush();
                } catch (IOException e) {
                    logger.error("输出数据出现未知异常",e);
                }
            });

        }
    }
}
