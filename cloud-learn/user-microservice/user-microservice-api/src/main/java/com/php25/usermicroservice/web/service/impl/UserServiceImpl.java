package com.php25.usermicroservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.usermicroservice.web.dto.AppRefDto;
import com.php25.usermicroservice.web.dto.GroupRefDto;
import com.php25.usermicroservice.web.dto.RoleRefDto;
import com.php25.usermicroservice.web.dto.UserChangeDto;
import com.php25.usermicroservice.web.dto.UserDetailDto;
import com.php25.usermicroservice.web.dto.UserPageDto;
import com.php25.usermicroservice.web.dto.UserRegisterDto;
import com.php25.usermicroservice.web.model.App;
import com.php25.usermicroservice.web.model.AppRef;
import com.php25.usermicroservice.web.model.Group;
import com.php25.usermicroservice.web.model.GroupRef;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.model.RoleRef;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.AppRepository;
import com.php25.usermicroservice.web.repository.GroupRepository;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.repository.UserRepository;
import com.php25.usermicroservice.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/2 15:17
 * @description:
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Boolean register(UserRegisterDto registerUserDto) {
        Optional<User> userOptional = userRepository.findByMobile(registerUserDto.getMobile());
        if (userOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("手机号:%s在系统中已经存在,无法注册", registerUserDto.getMobile()));
        }

        Optional<User> userOptional1 = userRepository.findByUsername(registerUserDto.getUsername());
        if (userOptional1.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("用户名:%s在系统中已经存在,无法注册", registerUserDto.getUsername()));
        }

        Optional<App> appOptional = appRepository.findById(registerUserDto.getAppId());
        if (!appOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("应用id:%s在系统中不存在", registerUserDto.getAppId()));
        }

        User user = new User();
        BeanUtils.copyProperties(registerUserDto, user);
        //刚注册的用户都是合法用户
        user.setEnable(1);
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());

        AppRef appRef = new AppRef();
        appRef.setAppId(registerUserDto.getAppId());
        user.setApps(Sets.newHashSet(appRef));

        User user1 = userRepository.save(user);
        if (user1.getId() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean changePasswordByUsername(String username, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("无法通过用户名:%s找到相关的用户信息", username));
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(oldPassword)) {
            throw Exceptions.throwIllegalStateException(String.format("输入的原密码:%s与数据库的密码不一样", oldPassword));
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetailDto detailInfo(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if(user.getEnable()!=1) {
                throw Exceptions.throwIllegalStateException("无法通过username:" + username + "找到对应的用户");
            }
            UserDetailDto userDetailDto = new UserDetailDto();
            BeanUtils.copyProperties(user, userDetailDto, "roles", "groups", "apps");
            //加入角色信息
            List<Long> roleIds = user.getRoles().stream().map(RoleRef::getRoleId).collect(Collectors.toList());
            Iterable<Role> roles = roleRepository.findAllById(roleIds);
            Set<RoleRefDto> roleRefDtoSet = Lists.newArrayList(roles).stream().map(role -> {
                RoleRefDto roleRefDto = new RoleRefDto();
                roleRefDto.setName(role.getName());
                roleRefDto.setRoleId(role.getId());
                return roleRefDto;
            }).collect(Collectors.toSet());
            userDetailDto.setRoles(roleRefDtoSet);

            //加入app信息
            List<String> appIds = user.getApps().stream().map(AppRef::getAppId).collect(Collectors.toList());
            Iterable<App> apps = appRepository.findAllById(appIds);
            Set<AppRefDto> appRefDtoSet = Lists.newArrayList(apps).stream().map(app -> {
                AppRefDto appRefDto = new AppRefDto();
                appRefDto.setAppName(app.getAppName());
                appRefDto.setAppId(app.getAppId());
                return appRefDto;
            }).collect(Collectors.toSet());
            userDetailDto.setApps(appRefDtoSet);

            //加入group信息
            List<Long> groupIds = user.getGroups().stream().map(GroupRef::getGroupId).collect(Collectors.toList());
            Iterable<Group> groups = groupRepository.findAllById(groupIds);
            Set<GroupRefDto> groupRefDtoSet = Lists.newArrayList(groups).stream().map(group -> {
                GroupRefDto groupRefDto = new GroupRefDto();
                groupRefDto.setName(group.getName());
                groupRefDto.setGroupId(group.getId());
                return groupRefDto;
            }).collect(Collectors.toSet());
            userDetailDto.setGroups(groupRefDtoSet);
            return userDetailDto;
        } else {
            throw Exceptions.throwIllegalStateException("无法通过username:" + username + "找到对应的用户");
        }
    }

    @Override
    public Boolean changeUserInfo(String username, UserChangeDto userChangeDto) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("无法通过username:" + username + "找到对应的用户");
        } else {
            User user = userOptional.get();
            boolean result = userRepository.changUserBasicInfo(user.getId(), userChangeDto.getNickname(), userChangeDto.getMobile(), userChangeDto.getEmail(), userChangeDto.getHeadImageId());
            return result;
        }
    }


    @Override
    public List<UserPageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction) {
        SearchParamBuilder searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
        Sort sort = Sort.by(direction, property);
        PageRequest page = PageRequest.of(pageNum, pageSize, sort);
        Page<User> userPage = userRepository.findAll(searchParamBuilder, page);
        if (null != userPage && userPage.getTotalElements() > 0) {
            List<UserPageDto> result = userPage.stream().map(user -> {
                UserPageDto userPageDto = new UserPageDto();
                BeanUtils.copyProperties(user, userPageDto, "roles", "groups", "apps");
                return userPageDto;
            }).collect(Collectors.toList());
            return result;
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public Boolean authorizeRole(String appId, Long userId, Long roleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("无法通过userId:" + userId + "找到对应的用户");
        }
        User user = userOptional.get();
        //判断此用户是否具有此角色，如果有直接返回成功
        Optional<RoleRef> roleRefOptional = user.getRoles().stream().filter(roleRef -> roleRef.getRoleId().equals(roleId)).findFirst();
        if (roleRefOptional.isPresent()) {
            return true;
        }

        //判断userId 是否是这个app下的
        Optional<AppRef> appRefOptional = user.getApps().stream().filter(appRef -> appRef.getAppId().equals(appId)).findFirst();
        if (!appRefOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("此用户id:" + userId + "不属于appId:" + appId);
        }

        //判断roleId 是否在这个app下的
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("无法通过roleId:" + roleId + "找到对应的角色");
        }

        Role role = roleOptional.get();
        if (!appId.equals(role.getAppId())) {
            throw Exceptions.throwIllegalStateException("此角色id:" + roleId + "不属于appId:" + appId);
        }

        //新增对应的角色
        Set<RoleRef> roleRefs = user.getRoles();
        RoleRef roleRef = new RoleRef();
        roleRef.setRoleId(roleId);
        roleRefs.add(roleRef);

        user.setRoles(roleRefs);
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean revokeRole(String appId, Long userId, Long roleId) {
        //先根据userId查出用户
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("无法根据userId:%d，查询出对应的用户信息", userId));
        }
        User user = userOptional.get();

        //判断 userId是appId下的
        Optional<AppRef> appRefOptional = user.getApps().stream().filter(appRef -> appRef.getAppId().equals(appId)).findFirst();
        if (!appRefOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("此用户userId:%d,不属于appId为:%s的应用", userId, appId));
        }


        //查看roleId在系统中是否存在
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("无法通过roleId:" + roleId + "找到对应的角色");
        }

        //判断roleId是appId下的
        Role role = roleOptional.get();
        if (!appId.equals(role.getAppId())) {
            throw Exceptions.throwIllegalStateException("此角色id:" + roleId + "不属于appId:" + appId);
        }

        //移除对应的角色
        Set<RoleRef> roleRefs = user.getRoles();
        Set<RoleRef> roleRefs1 = roleRefs.stream().filter(roleRef -> !roleRef.getRoleId().equals(roleId)).collect(Collectors.toSet());
        user.setRoles(roleRefs1);
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean joinGroup(String appId, Long userId, Long groupId) {
        return null;
    }

    @Override
    public Boolean leaveGroup(String appId, Long userId, Long groupId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (null != user.getRoles() && user.getRoles().size() > 0) {
                List<Long> roleIds = user
                        .getRoles()
                        .stream()
                        .map(RoleRef::getRoleId)
                        .collect(Collectors.toList());
                List<GrantedAuthority> grantedAuthorities = Lists.newArrayList(roleRepository.findAllById(roleIds));
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);

            } else {
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Lists.newArrayList());
            }
        } else {
            return org.springframework.security.core.userdetails.User.builder().disabled(true).build();
        }
    }
}
