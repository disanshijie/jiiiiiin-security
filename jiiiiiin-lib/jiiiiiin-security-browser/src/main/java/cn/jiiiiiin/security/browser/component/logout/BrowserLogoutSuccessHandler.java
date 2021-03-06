/**
 *
 */
package cn.jiiiiiin.security.browser.component.logout;

import cn.jiiiiiin.security.core.utils.HttpDataUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的退出成功处理器，如果设置了`jiiiiiin.security.browser.signOutUrl`，则跳到配置的地址上，
 * 如果没配置，则返回json格式的响应。
 *
 * @author zhailiang
 * @author jiiiiiin
 */
@Slf4j
public class BrowserLogoutSuccessHandler implements LogoutSuccessHandler {

    public BrowserLogoutSuccessHandler(String signOutSuccessUrl) {
        this.signOutSuccessUrl = signOutSuccessUrl;
    }

    private String signOutSuccessUrl;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected LiteDeviceResolver liteDeviceResolver;

    /**
     * 根据渠道渲染响应数据
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("退出成功");
        final Device currentDevice = liteDeviceResolver.resolveDevice(request);
        if (!currentDevice.isNormal()) {
            respJson(response);
        } else {
            response.sendRedirect(signOutSuccessUrl);
        }

    }

    protected void respJson(HttpServletResponse response) throws IOException {
        HttpDataUtil.respJson(response, objectMapper.writeValueAsString(R.failed("退出成功")));
    }

}
