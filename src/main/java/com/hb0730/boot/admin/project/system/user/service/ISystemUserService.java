package com.hb0730.boot.admin.project.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hb0730.boot.admin.project.system.user.model.dto.LoginUserDTO;
import com.hb0730.boot.admin.project.system.user.model.entity.SystemUserEntity;
import com.hb0730.boot.admin.project.system.user.model.vo.UserVO;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统用户  服务类
 * </p>
 *
 * @author bing_huang
 * @since 2020-03-24
 */
public interface ISystemUserService extends IService<SystemUserEntity> {

    /**
     * 用户保存
     *
     * @param vo 用户信息
     * @return 是否成功
     */
    boolean save(@NonNull UserVO vo);

    /**
     * 获取用户详情
     *
     * @param userId 用户id
     * @return 用户详情
     */
    UserVO getUserInfo(@NotNull Long userId);

    /**
     * 修改用户
     *
     * @param vo     用户信息
     * @param userId 用户id
     * @return 是否成功
     */
    boolean updateUser(@NonNull UserVO vo, @NonNull Long userId);


    /**
     * 根据用户账号查询用户信息
     *
     * @param username 用户账号
     * @return 用户登录信息
     */
    LoginUserDTO loadUserByUsername(@NonNull String username);


    /**
     * <p>
     * 重置面
     * </p>
     *
     * @param id 用户id
     * @return 是否成功
     */
    boolean resetPassword(@NonNull Long id);
}