package com.php25.usermicroservice.web.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.RandomUtil;
import com.php25.usermicroservice.web.constant.Constants;
import com.php25.usermicroservice.web.dto.AccountDto;
import com.php25.usermicroservice.web.dto.AppDetailDto;
import com.php25.usermicroservice.web.dto.AppPageDto;
import com.php25.usermicroservice.web.dto.AppRegisterDto;
import com.php25.usermicroservice.web.model.App;
import com.php25.usermicroservice.web.model.AppRef;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.model.RoleRef;
import com.php25.usermicroservice.web.model.User;
import com.php25.usermicroservice.web.repository.AppRepository;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.repository.UserRepository;
import com.php25.usermicroservice.web.service.AppClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:46
 * @description:
 */
@Slf4j
@Service
public class AppClientServiceImpl extends RandomValueAuthorizationCodeServices implements AppClientService, InitializingBean {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String OAUTH_CODE_REDIS_PREFIX = "oauth2_code:";

    private final RedisTemplate<String, byte[]> redisTemplate1 = new RedisTemplate<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate1.setConnectionFactory(redisConnectionFactory);
        redisTemplate1.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate1.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate1.afterPropertiesSet();
    }

    @Override
    public AppDetailDto detailInfo(String appId) {
        Optional<App> appOptional = appRepository.findById(appId);
        if (!appOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("无法找到对应oauth2客户端,appId:" + appId);
        } else {
            App app = appOptional.get();
            AppDetailDto appDetailDto = new AppDetailDto();
            BeanUtils.copyProperties(app, appDetailDto);
            return appDetailDto;
        }
    }

    @Transactional
    @Override
    public AccountDto register(AppRegisterDto appRegisterDto) {
        App app = new App();
        BeanUtils.copyProperties(appRegisterDto, app);
        app.setRegisterDate(LocalDateTime.now());
        app.setAppName(appRegisterDto.getAppName());
        app.setAppSecret(passwordEncoder.encode(app.getAppSecret()));
        app.setEnable(1);
        appRepository.insert(app);


        String password = RandomUtil.getRandomNumbersAndLetters(8);

        User user = new User();
        user.setUsername(app.getAppId() + RandomUtil.getRandomNumbersAndLetters(6));
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());
        user.setEnable(1);
        user.setMobile("11111111111");
        user.setEmail("123@qq.com");
        user.setNickname(user.getUsername());
        AppRef appRef = new AppRef();
        appRef.setAppId(app.getAppId());
        user.setApps(Sets.newHashSet(appRef));
        user = userRepository.save(user);

        //初始化管理员权限
        Optional<Role> adminRoleOptional = roleRepository.findByNameAndAppId(Constants.Role.ADMIN, app.getAppId());
        Role adminRole;
        if (!adminRoleOptional.isPresent()) {
            Role role1 = new Role();
            role1.setName(Constants.Role.ADMIN);
            role1.setAppId(app.getAppId());
            role1.setCreateUserId(user.getUsername());
            role1.setCreateDate(LocalDateTime.now());
            role1.setDescription("管理员权限");
            role1.setEnable(1);
            adminRole = roleRepository.save(role1);
        } else {
            adminRole = adminRoleOptional.get();
        }

        //初始化普通用户权限
        Optional<Role> customerRoleOptional = roleRepository.findByNameAndAppId(Constants.Role.CUSTOMER, app.getAppId());
        if (!customerRoleOptional.isPresent()) {
            Role role2 = new Role();
            role2.setName(Constants.Role.CUSTOMER);
            role2.setAppId(app.getAppId());
            role2.setCreateUserId(user.getUsername());
            role2.setCreateDate(LocalDateTime.now());
            role2.setDescription("普通用户权限");
            role2.setEnable(1);
            roleRepository.save(role2);
        }

        RoleRef roleRef = new RoleRef();
        roleRef.setRoleId(adminRole.getId());
        user.setRoles(Sets.newHashSet(roleRef));
        userRepository.save(user);

        AccountDto accountDto = new AccountDto();
        accountDto.setUsername(user.getUsername());
        accountDto.setPassword(password);
        return accountDto;
    }

    @Override
    public Boolean unregister(String appId) {
        Optional<App> appOptional = appRepository.findById(appId);
        if (appOptional.isPresent()) {
            App app = appOptional.get();
            app.setEnable(2);
            appRepository.save(app);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<AppPageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction) {
        SearchParamBuilder searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
        Sort sort = Sort.by(direction, property);
        PageRequest page = PageRequest.of(pageNum, pageSize, sort);
        Page<App> appPage = appRepository.findAll(searchParamBuilder, page);
        if (null != appPage && appPage.getTotalElements() > 0) {
            List<AppPageDto> result = appPage.stream().map(app -> {
                AppPageDto appPageDto = new AppPageDto();
                BeanUtils.copyProperties(app, appPageDto);
                return appPageDto;
            }).collect(Collectors.toList());
            return result;
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<App> appOptional = appRepository.findById(clientId);
        if (!appOptional.isPresent()) {
            return null;
        } else {
            App app = appOptional.get();
            BaseClientDetails clientDetails = new BaseClientDetails();
            clientDetails.setClientId(app.getAppId());
            clientDetails.setClientSecret(app.getAppSecret());
            clientDetails.setRegisteredRedirectUri(Sets.newHashSet(app.getRegisteredRedirectUri()));
            clientDetails.setAuthorizedGrantTypes(Lists.newArrayList("authorization_code"));
            clientDetails.setScope(Lists.newArrayList("authentication"));
            clientDetails.setAutoApproveScopes(Lists.newArrayList("authentication"));
            return clientDetails;
        }
    }


    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        String key = OAUTH_CODE_REDIS_PREFIX + code;
        redisTemplate1.opsForValue().set(key, serialize(authentication), 30, TimeUnit.MINUTES);
    }

    @Override
    protected OAuth2Authentication remove(String code) {
        String key = OAUTH_CODE_REDIS_PREFIX + code;
        byte[] value = redisTemplate1.opsForValue().get(key);
        if (null != value) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) deserialize(value);
            redisTemplate1.delete(key);
            return auth2Authentication;
        } else {
            return null;
        }
    }

    private byte[] serialize(Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            throw Exceptions.throwIllegalStateException("序列化对象失败", e);
        }
    }

    private Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) {
            throw Exceptions.throwIllegalStateException("反序列化对象失败", e);
        }
    }
}
