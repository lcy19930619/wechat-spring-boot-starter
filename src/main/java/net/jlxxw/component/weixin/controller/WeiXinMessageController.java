package net.jlxxw.component.weixin.controller;

import net.jlxxw.component.weixin.component.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("weixin")
public class WeiXinMessageController {
    @Autowired
    private EventBus eventBus;

    @RequestMapping("core")
    public void coreController(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = eventBus.dispatcher(request);
        response.setCharacterEncoding("UTF-8");
        final PrintWriter writer = response.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
    }
}
