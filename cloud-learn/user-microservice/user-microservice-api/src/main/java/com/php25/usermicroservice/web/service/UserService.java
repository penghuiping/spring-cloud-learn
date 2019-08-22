package com.php25.usermicroservice.web.service;

import com.php25.common.core.specification.SearchParam;
import com.php25.usermicroservice.web.dto.UserChangeDto;
import com.php25.usermicroservice.web.dto.UserDetailDto;
import com.php25.usermicroservice.web.dto.UserPageDto;
import com.php25.usermicroservice.web.dto.UserRegisterDto;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * 用户服务
 *
 * @author: penghuiping
 * @date: 2018/12/28 14:13
 * @description:
 */
public interface UserService extends UserDetailsService {

    /**
     * 注册
     *
     * @param registerUserDto 客户信息
     * @return true:注册成功，false:注册失败
     */
    Boolean register(UserRegisterDto registerUserDto);

    /**
     * 修改某个用户密码
     *
     * @param username    用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return true:修改密码成功,false:修改密码失败
     */
    Boolean changePasswordByUsername(String username, String oldPassword, String newPassword);

    /**
     * 根据用户名获取详情
     *
     * @param username 用户名
     * @return 用户详情信息
     */
    UserDetailDto detailInfo(String username);

    /**
     * 分页查询
     *
     * @param pageNum      当前第几页
     * @param pageSize     每页数量
     * @param searchParams 搜索参数
     * @param property     排序字段
     * @param direction    升序或者降序
     * @return 返回用户分页列表
     */
    List<UserPageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction);

    /**
     * 修改用户基本信息
     *
     * @param username      用户名
     * @param userChangeDto 用户基本信息
     * @return true:成功 false:失败
     */
    Boolean changeUserInfo(String username, UserChangeDto userChangeDto);


    /**
     * 根据appId,向某个用户授予角色
     *
     * @param appId  应用id
     * @param userId 用户id
     * @param roleId 角色id
     * @return true:成功 false:失败
     */
    Boolean authorizeRole(String appId, Long userId, Long roleId);

    /**
     * 根据appId,向某个用户吊销角色
     *
     * @param appId  应用id
     * @param userId 用户id
     * @param roleId 角色id
     * @return true:成功 false:失败
     */
    Boolean revokeRole(String appId, Long userId, Long roleId);

    /**
     * 根据appId,某个用户加入某个组
     *
     * @param appId   应用id
     * @param userId  用户id
     * @param groupId 组id
     * @return true:成功  false:失败
     */
    Boolean joinGroup(String appId, Long userId, Long groupId);

    /**
     * 根据appId,某个用户离开某个组
     *
     * @param appId   应用id
     * @param userId  用户id
     * @param groupId 组id
     * @return true:成功  false:失败
     */
    Boolean leaveGroup(String appId, Long userId, Long groupId);


    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
