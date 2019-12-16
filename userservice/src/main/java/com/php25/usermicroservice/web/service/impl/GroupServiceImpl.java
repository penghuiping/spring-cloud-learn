package com.php25.usermicroservice.web.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.usermicroservice.web.constant.UserBusinessError;
import com.php25.usermicroservice.web.dto.GroupCreateDto;
import com.php25.usermicroservice.web.dto.GroupDetailDto;
import com.php25.usermicroservice.web.dto.GroupPageDto;
import com.php25.usermicroservice.web.model.Group;
import com.php25.usermicroservice.web.repository.GroupRepository;
import com.php25.usermicroservice.web.service.GroupService;
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
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<GroupPageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction) {
        SearchParamBuilder searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
        Pageable pageable = PageRequest.of(pageNum, pageSize, direction, property);
        Page<Group> groupPage = groupRepository.findAll(searchParamBuilder, pageable);
        if (null != groupPage && groupPage.getTotalElements() > 0) {
            List<Group> groups = groupPage.getContent();
            if (groups.size() > 0) {
                List<GroupPageDto> result = groups.stream().map(group -> {
                    GroupPageDto groupPageDto = new GroupPageDto();
                    BeanUtils.copyProperties(group, groupPageDto);
                    return groupPageDto;
                }).collect(Collectors.toList());
                return result;
            }
        }
        return Lists.newArrayList();
    }


    @Override
    public Boolean unableGroup(String appId,String username, Long groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            throw Exceptions.throwBusinessException(UserBusinessError.GROUP_ID_NOT_VALID);
        }
        Group group = groupOptional.get();
        //判断group是否是appId下的组
        if (!group.getAppId().equals(appId)) {
            throw Exceptions.throwBusinessException(UserBusinessError.GROUP_ID_NOT_VALID);
        }

        group.setLastModifiedUserId(username);
        group.setLastModifiedDate(LocalDateTime.now());
        group.setEnable(2);

        groupRepository.save(group);
        return true;
    }

    @Override
    public Boolean create(String appId,String username, GroupCreateDto groupCreateDto) {
        Group group = new Group();
        BeanUtils.copyProperties(groupCreateDto, group);
        group.setAppId(appId);
        group.setCreateDate(LocalDateTime.now());
        group.setCreateUserId(username);
        group.setEnable(1);
        groupRepository.save(group);
        return true;
    }

    @Override
    public Boolean changeInfo(String appId, String username,Long groupId, String groupDescription) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            throw Exceptions.throwBusinessException(UserBusinessError.GROUP_ID_NOT_VALID);
        }
        Group group = groupOptional.get();
        //判断group是否是appId下的角色
        if (!group.getAppId().equals(appId)) {
            throw Exceptions.throwBusinessException(UserBusinessError.GROUP_ID_NOT_VALID);
        }

        group.setDescription(groupDescription);
        group.setLastModifiedUserId(username);
        group.setLastModifiedDate(LocalDateTime.now());
        groupRepository.save(group);
        return true;
    }

    @Override
    public GroupDetailDto detailInfo(Long groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            throw Exceptions.throwBusinessException(UserBusinessError.GROUP_ID_NOT_VALID);
        } else {
            Group group = groupOptional.get();
            GroupDetailDto groupDetailDto = new GroupDetailDto();
            BeanUtils.copyProperties(group, groupDetailDto);
            return groupDetailDto;
        }
    }

}
