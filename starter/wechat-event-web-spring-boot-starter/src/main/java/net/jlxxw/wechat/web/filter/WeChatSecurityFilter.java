package net.jlxxw.wechat.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.jlxxw.wechat.repository.ip.IpSegmentRepository;
import net.jlxxw.wechat.security.blacklist.BlackList;
import net.jlxxw.wechat.security.template.SecurityFilterTemplate;
import net.jlxxw.wechat.web.util.NetworkUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * 简单的安全过滤器，只用于判断请求ip是否属于微信服务器，
 * 由 wechat-security-spring-boot-starter 模块进行装配
 * @author lcy
 */
public class WeChatSecurityFilter implements Filter, SecurityFilterTemplate {

    private final BlackList blackList;

    private final IpSegmentRepository ipSegmentRepository;

    /**
     * 安全过滤器，
     * @param blackList 黑名单列表
     * @param ipSegmentRepository IP 存储器
     */

    public WeChatSecurityFilter(BlackList blackList, IpSegmentRepository ipSegmentRepository) {
        this.blackList = blackList;
        this.ipSegmentRepository = ipSegmentRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }


    @Override
    public void destroy() {
        Filter.super.destroy();
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean http = request instanceof HttpServletRequest;
        if (!http) {
            // 不是 http 请求，直接转发走
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;

        String ipAddress = NetworkUtil.getIpAddress(httpServletRequest);
        boolean security = security(ipAddress);
        if (security) {
            // 本次接受到的请求是安全的，可以继续处理数据
            chain.doFilter(request, response);
            return;
        }

        // 拒绝 当前ip请求，并记录到黑名单中
        reject(ipAddress);

        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        // 返回http 403
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setContentType("text/html");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.println("IP FORBIDDEN");
        writer.close();
    }

    /**
     * 判断此ip是否在黑名单列表中
     *
     * @param ip 目标ip
     * @return true 在黑名单中, false 不在黑名单中
     */
    @Override
    public boolean blacklisted(String ip) {
        return blackList.include(ip);
    }

    /**
     * 加载全部ip段信息
     *
     * @return 全部可信任的ip段
     */
    @Override
    public Set<String> loadAllIpSegments() {
        return ipSegmentRepository.findAll();
    }

    /**
     * 拒绝此ip链接
     *
     * @param ip ip
     */
    @Override
    public void reject(String ip) {
        blackList.add(ip);
    }
}
