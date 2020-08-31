package com.hb0730.boot.admin.security.handler;

import com.hb0730.boot.admin.domain.result.Result;
import com.hb0730.boot.admin.domain.result.Results;
import com.hb0730.boot.admin.security.model.User;
import com.hb0730.boot.admin.token.ITokenService;
import com.hb0730.commons.json.gson.GsonUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功
 *
 * @author bing_huang
 * @since 3.0.0
 */
@Component
@RequiredArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutSuccessHandlerImpl.class);
    private final ITokenService tokenService;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOGGER.debug("logout success <<<<<");
        User loginUser = tokenService.getLoginUser(request);
        if (null!=loginUser){
            tokenService.delLoginUser(request);
        }
        Result<String> result = Results.resultSuccess("注销成功");
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(GsonUtils.objectToJson(result));
    }
}