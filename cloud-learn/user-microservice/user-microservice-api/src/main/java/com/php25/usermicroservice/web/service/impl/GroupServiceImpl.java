package com.php25.usermicroservice.web.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
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
    public Boolean unableGroup(String appId, Long groupId) {
        return groupRepository.softDelete(Lists.newArrayList(groupId), appId);
    }

    @Override
    public Boolean create(String appId, GroupCreateDto groupCreateDto) {
        Group group = new Group();
        BeanUtils.copyProperties(groupCreateDto, group);
        group.setAppId(appId);
        groupRepository.save(group);
        return true;
    }

    @Override
    public Boolean changeInfo(String appId, Long roleId, String roleDescription) {
        return groupRepository.changeInfo(roleDescription, roleId, appId);
    }

    @Override
    public GroupDetailDto detailInfo(Long groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("无法通过id:%d找到对应的组记录", groupId));
        } else {
            Group group = groupOptional.get();
            GroupDetailDto groupDetailDto = new GroupDetailDto();
            BeanUtils.copyProperties(group, groupDetailDto);
            return groupDetailDto;
        }
    }

}
