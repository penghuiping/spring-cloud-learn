package com.php25.userservice.client.rpc;

import com.php25.common.core.dto.ResultDto;
import com.php25.userservice.client.dto.CustomerDto;

/**
 * 前台用户操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 13:06
 * @description:
 */
public interface CustomerRpc {

    /**
     * 注册
     *
     * @param customerDto 客户信息
     * @return true:注册成功，false:注册失败
     */
    Boolean register(CustomerDto customerDto);

    /**
     * 根据用户名与密码进行登入
     *
     * @param username 用户名
     * @param password 密码
     * @return jwt字符串
     */
    ResultDto<String> loginByUsername(String username, String password);

    /**
     * 根据手机号进行登入
     *
     * @param mobile 手机号
     * @return jwt字符串
     */
    ResultDto<String> loginByMobile(String mobile);


    /**
     * 根据邮箱与密码进行登入
     *
     * @param email    邮箱
     * @param password 密码
     * @return jwt字符串
     */
    ResultDto<String> loginByEmail(String email, String password);

    /**
     * 根据手机号重置密码
     *
     * @param mobile      手机号
     * @param newPassword 重置成的新密码
     * @return true:重置成功,false:重置失败
     */
    Boolean resetPasswordByMobile(String mobile, String newPassword);

    /**
     * 根据邮箱重置密码
     *
     * @param email       邮箱
     * @param newPassword 重置成的新密码
     * @return true:重置成功;false:重置失败
     */
    Boolean resetPasswordByEmail(String email, String newPassword);

    /**
     * 根据jwt获取前台用户详情
     *
     * @param jwt 通过登入接口获取的jwt令牌
     * @return 前台用户信息
     */
    ResultDto<CustomerDto> findOne(String jwt);

    /**
     * 验证jwt令牌是否合法
     *
     * @param jwt 登入接口获取的jwt令牌
     * @return true:合法，false:不合法
     */
    Boolean validateJwt(String jwt);


    /**
     * 修改前台用户相关信息
     *
     * @param customerDto 前台用户信息
     * @return true:修改用户信息成功，false:修改用户信息失败
     */
    Boolean update(CustomerDto customerDto);

    /**
     * 通过手机号查询前台客户信息
     *
     * @param mobile 手机号
     * @return 返回前台客户信息
     */
    ResultDto<CustomerDto> findCustomerDtoByMobile(String mobile);


    /**
     * 登出
     *
     * @param jwt jwt令牌
     * @return true:登出成功,false:登出失败
     */
    Boolean logout(String jwt);

}
