package com.php25.usermicroservice.client.rpc;

import com.php25.usermicroservice.client.bo.CustomerBo;
import reactor.core.publisher.Mono;

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
    Mono<Boolean> register(CustomerBo customerDto);

    /**
     * 根据用户名与密码进行登入
     *
     * @param username 用户名
     * @param password 密码
     * @return jwt字符串
     */
    Mono<String> loginByUsername(String username, String password);


    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @return 短信验证码
     */
    Mono<String> sendSms(String mobile);

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 邮箱验证码
     */
    Mono<String> sendEmailCode(String email);

    /**
     * 根据手机号进行登入
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return jwt字符串
     */
    Mono<String> loginByMobile(String mobile, String code);


    /**
     * 根据邮箱与密码进行登入
     *
     * @param email 邮箱
     * @param code  验证码
     * @return jwt字符串
     */
    Mono<String> loginByEmail(String email, String code);

    /**
     * 根据手机号重置密码
     *
     * @param mobile      手机号
     * @param newPassword 重置成的新密码
     * @param oldPassword 原密码
     * @return true:重置成功,false:重置失败
     */
    Mono<Boolean> resetPasswordByMobile(String mobile, String newPassword, String oldPassword);

    /**
     * 根据邮箱重置密码
     *
     * @param email       邮箱
     * @param newPassword 重置成的新密码
     * @param oldPassword 原密码
     * @return true:重置成功;false:重置失败
     */
    Mono<Boolean> resetPasswordByEmail(String email, String newPassword, String oldPassword);

    /**
     * 根据jwt获取前台用户详情
     *
     * @param jwt 通过登入接口获取的jwt令牌
     * @return 前台用户信息
     */
    Mono<CustomerBo> findOne(String jwt);

    /**
     * 验证jwt令牌是否合法
     *
     * @param jwt 登入接口获取的jwt令牌
     * @return true:合法，false:不合法
     */
    Mono<Boolean> validateJwt(String jwt);


    /**
     * 修改前台用户相关信息
     *
     * @param customerDto 前台用户信息
     * @return true:修改用户信息成功，false:修改用户信息失败
     */
    Mono<Boolean> update(CustomerBo customerDto);

    /**
     * 通过手机号查询前台客户信息
     *
     * @param mobile 手机号
     * @return 返回前台客户信息
     */
    Mono<CustomerBo> findCustomerByMobile(String mobile);

    /**
     * 登出
     *
     * @param jwt jwt令牌
     * @return true:登出成功,false:登出失败
     */
    Mono<Boolean> logout(String jwt);


    Mono<Object> testMessage();

}
