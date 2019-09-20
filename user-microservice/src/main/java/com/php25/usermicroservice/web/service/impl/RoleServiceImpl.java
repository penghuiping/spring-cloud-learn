package com.php25.usermicroservice.web.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.usermicroservice.web.constant.Constants;
import com.php25.usermicroservice.web.dto.RoleCreateDto;
import com.php25.usermicroservice.web.dto.RoleDetailDto;
import com.php25.usermicroservice.web.dto.RolePageDto;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.repository.UserRepository;
import com.php25.usermicroservice.web.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/3 10:23
 * @description:
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RolePageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction) {
        SearchParamBuilder searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
        Pageable pageable = PageRequest.of(pageNum, pageSize, direction, property);
        Page<Role> rolePage = roleRepository.findAll(searchParamBuilder, pageable);
        if (null != rolePage && rolePage.getTotalElements() > 0) {
            List<Role> roles = rolePage.getContent();
            if (roles.size() > 0) {
                List<RolePageDto> result = roles.stream().map(role -> {
                    RolePageDto rolePageDto = new RolePageDto();
                    BeanUtils.copyProperties(role, rolePageDto);
                    return rolePageDto;
                }).collect(Collectors.toList());
                return result;
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public Boolean unableRole(String appId, String username, Long roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("无法通过roleId:" + roleId + "找到对应的数据");
        }
        Role role = roleOptional.get();
        //判断role是否是appId下的角色
        if (!role.getAppId().equals(appId)) {
            throw Exceptions.throwIllegalStateException("此角色id:" + roleId + "不属于appId:" + appId + "的应用");
        }

        //customer与admin角色是系统自动创建的角色，不能删除
        if(Constants.Role.ADMIN.equals(role.getName())) {
            throw Exceptions.throwIllegalStateException(Constants.Role.ADMIN+"角色是授权服务内置角色，无法删除，请删除其他自定义角色");
        }

        if(Constants.Role.CUSTOMER.equals(role.getName())) {
            throw Exceptions.throwIllegalStateException(Constants.Role.CUSTOMER+"角色是授权服务内置角色，无法删除，请删除其他自定义角色");
        }

        role.setLastModifiedUserId(username);
        role.setLastModifiedDate(LocalDateTime.now());
        role.setEnable(2);

        roleRepository.save(role);
        return true;
    }

    @Override
    public Boolean create(String appId, String username, RoleCreateDto roleCreateDto) {
        Role role = new Role();
        BeanUtils.copyProperties(roleCreateDto, role);
        role.setAppId(appId);
        role.setCreateDate(LocalDateTime.now());
        role.setCreateUserId(username);
        role.setEnable(1);
        roleRepository.save(role);
        return true;
    }

    @Override
    public Boolean changeInfo(String appId, String username, Long roleId, String roleDescription) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException("无法通过roleId:" + roleId + "找到对应的数据");
        }
        Role role = roleOptional.get();
        //判断role是否是appId下的角色
        if (!role.getAppId().equals(appId)) {
            throw Exceptions.throwIllegalStateException("此角色id:" + roleId + "不属于appId:" + appId + "的应用");
        }

        role.setDescription(roleDescription);
        role.setLastModifiedUserId(username);
        role.setLastModifiedDate(LocalDateTime.now());
        roleRepository.save(role);
        return true;
    }

    @Override
    public RoleDetailDto detailInfo(Long roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("无法通过id:%d找到对应的角色记录", roleId));
        } else {
            Role role = roleOptional.get();
            RoleDetailDto roleDetailDto = new RoleDetailDto();
            BeanUtils.copyProperties(role, roleDetailDto);
            return roleDetailDto;
        }
    }

}
