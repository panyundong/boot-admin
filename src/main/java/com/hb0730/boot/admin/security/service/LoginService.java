package com.hb0730.boot.admin.security.service;

import com.hb0730.boot.admin.commons.constant.enums.SystemStatusEnum;
import com.hb0730.boot.admin.commons.utils.MessageUtils;
import com.hb0730.boot.admin.configuration.properties.BootAdminProperties;
import com.hb0730.boot.admin.exception.BaseException;
import com.hb0730.boot.admin.exception.UserPasswordNotMatchException;
import com.hb0730.boot.admin.manager.AsyncManager;
import com.hb0730.boot.admin.manager.factory.AsyncFactory;
import com.hb0730.boot.admin.security.model.LoginSuccess;
import com.hb0730.boot.admin.security.model.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </P>
 *
 * @author bing_huang
 * @since V1.0
 */
@Component
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final BootAdminProperties properties;
    private final ITokenService tokenService;

    public LoginService(AuthenticationManager authenticationManager, BootAdminProperties properties, ITokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.properties = properties;
        this.tokenService = tokenService;
    }

    /**
     * <p>
     * 用户登录
     * </p>
     *
     * @param username 用户账号
     * @param password 用户密码
     * @return token令牌
     */
    public LoginSuccess login(String username, String password) {
        // 删除缓存

        // 用户验证
        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, SystemStatusEnum.FAIL.getValue(), MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, SystemStatusEnum.FAIL.getValue(), e.getMessage()));
                throw new BaseException(e.getMessage());
            }

        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        String accessToken = tokenService.createAccessToken(loginUser);
        LoginSuccess success = new LoginSuccess();
        success.setAccessToken(accessToken);
        success.setLoginUser(loginUser);
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, SystemStatusEnum.SUCCESS.getValue(), MessageUtils.message("login.success")));
        return success;
    }
}
