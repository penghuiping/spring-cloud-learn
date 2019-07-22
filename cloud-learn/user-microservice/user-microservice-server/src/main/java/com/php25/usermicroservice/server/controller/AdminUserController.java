package com.php25.usermicroservice.server.controller;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.common.core.util.AssertUtil;
import com.php25.usermicroservice.client.bo.AdminUserBo;
import com.php25.usermicroservice.client.bo.SearchBo;
import com.php25.usermicroservice.client.rpc.AdminUserRpc;
import com.php25.userservice.server.dto.AdminUserDto;
import com.php25.userservice.server.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/2 15:17
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/adminUser")
public class AdminUserController implements AdminUserRpc {

    @Autowired
    private AdminUserService adminUserService;

    @Override
    @PostMapping("/login")
    public Mono<AdminUserBo> login(String username, String password) {
        //参数效验
        AssertUtil.hasText(username, "username参数不能为空");
        AssertUtil.hasText(password, "password参数不能为空");

        return Mono.fromCallable(() -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findByUsernameAndPassword(username, password);
            if (!adminUserDtoOptional.isPresent()) {
                return null;
            } else {
                AdminUserBo adminUserBo = new AdminUserBo();
                BeanUtils.copyProperties(adminUserDtoOptional.get(), adminUserBo);
                return adminUserBo;
            }
        }).flatMap(adminUserBo -> {
            if (adminUserBo == null) {
                return Mono.empty();
            } else {
                return Mono.just(adminUserBo);
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/resetPassword")
    public Mono<Boolean> resetPassword(@RequestBody List<Long> adminUserIds) {
        //参数效验
        AssertUtil.notEmpty(adminUserIds, "adminUserIds至少需要一个元素");
        //初始化密码为123456
        return Mono.fromCallable(() -> {
            return adminUserService.updatePassword("123456", adminUserIds);
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/changePassword")
    public Mono<Boolean> changePassword(Long adminUserId, String originPassword, String newPassword) {
        //参数效验
        AssertUtil.notNull(adminUserId, "adminUserId参数不能为null");
        AssertUtil.hasText(originPassword, "originPassword不能为空");
        AssertUtil.hasText(newPassword, "newPassword不能为空");

        return Mono.fromCallable(() -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(adminUserId);
            if (!adminUserDtoOptional.isPresent()) {
                throw new IllegalArgumentException(String.format("无法通过adminUserId:%d找到相关的后台用户信息", adminUserId));
            }

            AdminUserDto adminUserDto = adminUserDtoOptional.get();
            if (!adminUserDto.getPassword().equals(originPassword)) {
                throw new IllegalArgumentException(String.format("originPassword:%s与数据库的密码不一样", originPassword));
            }

            adminUserDto.setPassword(newPassword);
            Optional<AdminUserDto> adminUserDtoOptional1 = adminUserService.save(adminUserDto);
            if (adminUserDtoOptional1.isPresent()) {
                return true;
            } else {
                return false;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @GetMapping(value = "/findOne", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<AdminUserBo> findOne(@NotBlank Long id) {
        AssertUtil.notNull(id, "id数不能为null");
        return Mono.fromCallable(() -> {
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.findOne(id);
            if (adminUserDtoOptional.isPresent()) {
                AdminUserDto adminUserDto = adminUserDtoOptional.get();
                AdminUserBo adminUserBo = new AdminUserBo();
                BeanUtils.copyProperties(adminUserDto, adminUserBo);
                return adminUserBo;
            } else {
                return null;
            }
        }).flatMap(adminUserBo -> {
            if (adminUserBo == null) {
                return Mono.empty();
            } else {
                return Mono.just(adminUserBo);
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/save")
    public Mono<AdminUserBo> save(@RequestBody AdminUserBo adminUserBo) {
        //参数验证
        AssertUtil.notNull(adminUserBo, "adminUserBo不能为null");
        return Mono.fromCallable(() -> {
            AdminUserDto adminUserDto = new AdminUserDto();
            BeanUtils.copyProperties(adminUserBo, adminUserDto);
            Optional<AdminUserDto> adminUserDtoOptional = adminUserService.save(adminUserDto);
            if (adminUserDtoOptional.isPresent()) {
                adminUserBo.setId(adminUserDtoOptional.get().getId());
                return adminUserBo;
            } else {
                return null;
            }
        }).flatMap(adminUserBo1 -> {
            if (adminUserBo1 == null) {
                return Mono.empty();
            } else {
                return Mono.just(adminUserBo1);
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });

    }

    @Override
    @PostMapping("/softDelete")
    public Mono<Boolean> softDelete(@RequestBody List<Long> ids) {
        //参数效验
        AssertUtil.notEmpty(ids, "ids至少需要一个元素");
        return Mono.fromCallable(() -> {
            Optional<List<AdminUserDto>> optionalAdminUserDtos = adminUserService.findAll(ids);
            if (optionalAdminUserDtos.isPresent() && !optionalAdminUserDtos.get().isEmpty()) {
                adminUserService.softDelete(optionalAdminUserDtos.get());
                return true;
            } else {
                return false;
            }
        }).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }

    @Override
    @PostMapping("/query")
    public Flux<AdminUserBo> query(@RequestBody SearchBo searchBo) {
        return Mono.fromCallable(() -> {
            var searchParams = searchBo.getSearchParams().stream()
                    .map(searchBoParam -> SearchParam.of(searchBoParam.getFieldName(), searchBoParam.getOperator(), searchBoParam.getValue())).collect(Collectors.toList());
            var searchParamBuilder = SearchParamBuilder.builder().append(searchParams);

            Optional<DataGridPageDto<AdminUserDto>> optionalAdminUserDtoDataGridPageDto =
                    adminUserService.query(searchBo.getPageNum(), searchBo.getPageSize(), searchParamBuilder, BeanUtils::copyProperties, Sort.by(searchBo.getDirection(), searchBo.getProperty()));

            if (optionalAdminUserDtoDataGridPageDto.isPresent()) {
                DataGridPageDto<AdminUserDto> dtoDataGridPageDto = optionalAdminUserDtoDataGridPageDto.get();
                return dtoDataGridPageDto.getData().stream().map(adminUserDto -> {
                    AdminUserBo adminUserBo = new AdminUserBo();
                    BeanUtils.copyProperties(adminUserDto, adminUserBo);
                    return adminUserBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<AdminUserBo>();
            }
        }).flatMapMany(Flux::fromIterable).doOnError(throwable -> {
            log.error("出错啦", throwable);
        });
    }
}
