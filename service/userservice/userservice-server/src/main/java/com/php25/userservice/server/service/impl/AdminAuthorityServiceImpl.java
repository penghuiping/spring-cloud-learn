package com.php25.userservice.server.service.impl;

import com.php25.common.jdbc.service.BaseServiceImpl;
import com.php25.userservice.client.dto.AdminAuthorityDto;
import com.php25.userservice.server.model.AdminAuthority;
import com.php25.userservice.server.repository.AdminAuthorityRepository;
import com.php25.userservice.server.service.AdminAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2018/10/17 17:24
 * @description:
 */
@Slf4j
@Transactional
@Service
@Primary
public class AdminAuthorityServiceImpl extends BaseServiceImpl<AdminAuthorityDto, AdminAuthority, Long> implements AdminAuthorityService {

    private AdminAuthorityRepository adminAuthorityRepository;

    @Autowired
    public void setAdminAuthorityRepository(AdminAuthorityRepository adminAuthorityRepository) {
        this.adminAuthorityRepository = adminAuthorityRepository;
    }

    @Override
    public Optional<List<AdminAuthorityDto>> findAllByAdminMenuButtonIds(List<Long> ids) {
        Optional<List<AdminAuthority>> optionalAdminAuthorities = adminAuthorityRepository.findAllByAdminMenuButtonIds(ids);
        if (optionalAdminAuthorities.isPresent() && optionalAdminAuthorities.get().size() > 0) {
            List<AdminAuthorityDto> adminAuthorityDtos = optionalAdminAuthorities.get().stream().map(adminAuthority -> {
                AdminAuthorityDto adminAuthorityDto = new AdminAuthorityDto();
                BeanUtils.copyProperties(adminAuthority, adminAuthorityDto);
                return adminAuthorityDto;
            }).collect(Collectors.toList());
            return Optional.of(adminAuthorityDtos);
        } else {
            return Optional.empty();
        }
    }
}
