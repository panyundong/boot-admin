package com.hb0730.boot.admin.security.service;

import com.hb0730.boot.admin.commons.constant.enums.TokenTypeEnum;
import com.hb0730.boot.admin.security.model.LoginUser;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * <p>
 * </P>
 *
 * @author bing_huang
 * @since V1.0
 */
public interface ITokenService {
    /*
     *旧token延迟过期时间
     */

    Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    /**
     * <p>
     * 获取当前登录用户
     * </p>
     *
     * @param request 请求
     * @return 登录用户
     */
    public LoginUser getLoginUser(HttpServletRequest request);

    /**
     * 创建token
     *
     * @param loginUser 用户信息
     * @return token
     */
    String createAccessToken(LoginUser loginUser);

    /**
     * <p>
     * 刷新令牌
     * </P>
     *
     * @param loginUser 用户信息
     */
    void refreshAccessToken(LoginUser loginUser);

    /**
     * <p>
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     * </p>
     *
     * @param loginUser 用户信息
     */
    void verifyAccessToken(LoginUser loginUser);

    /**
     * <p>
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     * </p>
     *
     * @param request 请求
     */
    void verifyAccessToken(HttpServletRequest request);

    /**
     * <p>
     * 删除用户信息
     * </p>
     *
     * @param request 请求
     */
    void delLoginUser(HttpServletRequest request);

    /**
     * <p>
     * 删除accessToken并删除用户信息
     * </P>
     *
     * @param accessToken token令牌
     */
    void deleteAccessToken(String accessToken);

    /**
     * <p>
     * 获取在线用户
     * </p>
     *
     * @return 在线用户
     */
    Map<String, UserDetails> getOnline();

    /**
     * 检查是否支持给定类型
     *
     * @param type 附件类型
     * @return true为支持类型
     */
    boolean supportType(@Nullable TokenTypeEnum type);

    /**
     * 从数据声明生成令牌
     *
     * @param values 数据声明
     * @return 令牌
     */
    default String createToken(Map<String, String> values) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
        }
    }
}
