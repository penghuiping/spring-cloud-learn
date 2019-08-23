package com.php25.usermicroservice.web.config;

import com.google.common.collect.Sets;
import com.php25.common.core.exception.Exceptions;
import com.php25.usermicroservice.web.constant.Constants;
import com.php25.usermicroservice.web.model.App;
import com.php25.usermicroservice.web.model.AppRef;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.model.RoleRef;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.AppRepository;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2019/8/23 14:25
 * @description:
 */
@Configuration
public class DataInitConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @PostConstruct
    private void initData() {

        Boolean result = transactionTemplate.execute(status -> {
            //初始化 最高权限账号
            Optional<App> appOptional = appRepository.findById(Constants.SuperAdmin.appId);

            //初始化super_admin应用
            App app;
            if (!appOptional.isPresent()) {
                app = new App();
                app.setAppSecret(Constants.SuperAdmin.appSecret);
                app.setAppName(Constants.SuperAdmin.appName);
                app.setRegisterDate(LocalDateTime.now());
                app.setRegisteredRedirectUri(Constants.SuperAdmin.appRedirectUrl);
                app.setEnable(1);
                app.setAppId(Constants.SuperAdmin.appId);
                appRepository.insert(app);
            } else {
                app = appOptional.get();
            }

            //初始化super_admin账号
            Optional<User> userOptional = userRepository.findByUsername(Constants.SuperAdmin.username);
            User user;
            if (!userOptional.isPresent()) {
                user = new User();
                user.setUsername(Constants.SuperAdmin.username);
                user.setPassword(Constants.SuperAdmin.password);
                user.setEmail(Constants.SuperAdmin.email);
                user.setNickname(Constants.SuperAdmin.nickname);
                user.setMobile(Constants.SuperAdmin.mobile);
                user.setEnable(1);
                user.setCreateDate(LocalDateTime.now());
                user.setLastModifiedDate(LocalDateTime.now());
                AppRef appRef = new AppRef();
                appRef.setAppId(app.getAppId());
                user.setApps(Sets.newHashSet(appRef));
                userRepository.save(user);
            } else {
                user = userOptional.get();
            }

            //初始化 超级管理员、管理员与普通用户权限
            Optional<Role> roleOptional = roleRepository.findByNameAndAppId(Constants.Role.SUPER_ADMIN, app.getAppId());
            Role superAdminRole;
            if (!roleOptional.isPresent()) {
                Role role = new Role();
                role.setName(Constants.Role.SUPER_ADMIN);
                role.setAppId(app.getAppId());
                role.setCreateUserId(user.getUsername());
                role.setCreateDate(LocalDateTime.now());
                role.setDescription("超级管理员权限");
                role.setLastModifiedUserId(user.getUsername());
                role.setLastModifiedDate(LocalDateTime.now());
                role.setEnable(1);
                superAdminRole = roleRepository.save(role);
            } else {
                superAdminRole = roleOptional.get();
            }

            //给super_admin账号赋予超级管理员权限
            if (!userOptional.isPresent()) {
                RoleRef roleRef = new RoleRef();
                roleRef.setRoleId(superAdminRole.getId());
                user.setRoles(Sets.newHashSet(roleRef));
                userRepository.save(user);
            }
            return true;
        });

        if (null != result && result) {

        } else {
            throw Exceptions.throwIllegalStateException("初始化数据失败");
        }

    }
}
