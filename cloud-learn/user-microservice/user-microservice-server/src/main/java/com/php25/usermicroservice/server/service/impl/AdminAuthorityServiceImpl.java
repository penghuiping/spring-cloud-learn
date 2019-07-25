package com.php25.usermicroservice.server.service.impl;

import com.php25.common.db.service.BaseServiceImpl;
import com.php25.usermicroservice.server.dto.AdminAuthorityDto;
import com.php25.usermicroservice.server.model.AdminAuthority;
import com.php25.usermicroservice.server.repository.AdminAuthorityRepository;
import com.php25.usermicroservice.server.service.AdminAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2018/10/17 17:24
 * @description:
 */
@Slf4j
@Service
@Primary
@Transactional(rollbackFor = Exception.class)
public class AdminAuthorityServiceImpl implements AdminAuthorityService {

    private AdminAuthorityRepository adminAuthorityRepository;

    private BaseServiceImpl<AdminAuthorityDto, AdminAuthority, Long> baseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.baseService = new BaseServiceImpl<>(AdminAuthorityDto.class, AdminAuthority.class, adminAuthorityRepository);
    }

    @Autowired
    public void setAdminAuthorityRepository(AdminAuthorityRepository adminAuthorityRepository) {
        this.adminAuthorityRepository = adminAuthorityRepository;
    }

    @Override
    public Optional<List<AdminAuthorityDto>> findAllByAdminMenuButtonIds(List<Long> ids) {
        var optionalAdminAuthorities = adminAuthorityRepository.findAllByAdminMenuButtonIds(ids);
        if (optionalAdminAuthorities.isPresent() && optionalAdminAuthorities.get().size() > 0) {
            var adminAuthorityDtos = optionalAdminAuthorities.get().stream().map(adminAuthority -> {
                var adminAuthorityDto = new AdminAuthorityDto();
                BeanUtils.copyProperties(adminAuthority, adminAuthorityDto);
                return adminAuthorityDto;
            }).collect(Collectors.toList());
            return Optional.of(adminAuthorityDtos);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Set<AdminAuthorityDto>> findAllDistinctByAdminMenuButtonIds(List<Long> ids) {
        var optionalAdminAuthorities = adminAuthorityRepository.findAllByAdminMenuButtonIds(ids);
        if (optionalAdminAuthorities.isPresent() && optionalAdminAuthorities.get().size() > 0) {
            var adminAuthorityDtos = optionalAdminAuthorities.get().stream().map(adminAuthority -> {
                var adminAuthorityDto = new AdminAuthorityDto();
                BeanUtils.copyProperties(adminAuthority, adminAuthorityDto);
                return adminAuthorityDto;
            }).collect(Collectors.toSet());
            return Optional.of(adminAuthorityDtos);
        } else {
            return Optional.empty();
        }
    }
}
