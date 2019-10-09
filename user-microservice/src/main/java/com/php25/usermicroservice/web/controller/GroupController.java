package com.php25.usermicroservice.web.controller;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.Operator;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.dto.GroupCreateDto;
import com.php25.usermicroservice.web.dto.GroupDetailDto;
import com.php25.usermicroservice.web.dto.GroupPageDto;
import com.php25.usermicroservice.web.service.GroupService;
import com.php25.usermicroservice.web.vo.req.ReqCreateGroupVo;
import com.php25.usermicroservice.web.vo.req.ReqGroupChangeInfoVo;
import com.php25.usermicroservice.web.vo.req.SearchParamVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResGroupDetailVo;
import com.php25.usermicroservice.web.vo.res.ResGroupPageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:27
 * @description:
 */
@RestController
@RequestMapping("/group")
public class GroupController extends JSONController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/queryPage")
    public JSONResponse queryPage(@RequestAttribute String appId, @Valid @RequestBody SearchVo searchVo) {
        List<SearchParamVo> searchParamVoList = searchVo.getSearchParamVoList();
        List<SearchParam> searchParams;
        if (null == searchParamVoList || searchParamVoList.isEmpty()) {
            searchParams = new ArrayList<>();
        } else {
            searchParams = searchParamVoList.stream().map(searchParamVo ->
                    SearchParam.of(searchParamVo.getFieldName(),
                            searchParamVo.getOperator(),
                            searchParamVo.getValue())).collect(Collectors.toList());

            searchParams = searchParams.stream().filter(searchParam -> !"appId".equals(searchParam.getFieldName())).collect(Collectors.toList());
            searchParams.add(SearchParam.of("appId", Operator.EQ, appId));

        }
        List<GroupPageDto> groupPageDtos = groupService.queryPage(searchVo.getPageNum(), searchVo.getPageSize(), searchParams, "id", Sort.Direction.ASC);

        if (groupPageDtos == null || groupPageDtos.isEmpty()) {
            return succeed(Lists.newArrayList());
        } else {
            List<ResGroupPageVo> result = groupPageDtos.stream().map(groupPageDto -> {
                ResGroupPageVo resGroupPageVo = new ResGroupPageVo();
                BeanUtils.copyProperties(groupPageDto, resGroupPageVo);
                return resGroupPageVo;
            }).collect(Collectors.toList());
            return succeed(result);
        }
    }

    @PostMapping("/unableGroup")
    public JSONResponse unableGroup(@NotBlank @RequestAttribute String appId,
                                    @NotBlank @RequestAttribute String username,
                                    @Min(0) Long groupId) {
        Boolean result = groupService.unableGroup(appId, username, groupId);
        return succeed(result);
    }

    @PostMapping("/create")
    public JSONResponse createRole(@NotBlank @RequestAttribute String appId,
                                   @NotBlank @RequestAttribute String username,
                                   @Valid @RequestBody ReqCreateGroupVo reqCreateGroupVo) {
        GroupCreateDto groupCreateDto = new GroupCreateDto();
        groupCreateDto.setName(reqCreateGroupVo.getName());
        groupCreateDto.setDescription(reqCreateGroupVo.getDescription());
        Boolean result = groupService.create(appId, username, groupCreateDto);
        return succeed(result);
    }

    @PostMapping("/changeInfo")
    public JSONResponse changeInfo(@NotBlank @RequestAttribute String appId,
                                   @NotBlank @RequestAttribute String username,
                                   @Valid @RequestBody ReqGroupChangeInfoVo reqGroupChangeInfoVo) {
        Boolean result = groupService.changeInfo(appId, username, reqGroupChangeInfoVo.getGroupId(), reqGroupChangeInfoVo.getDescription());
        return succeed(result);
    }

    @PostMapping("/detailInfo")
    public JSONResponse detailInfo(@RequestAttribute String appId, @Min(0) Long groupId) {
        GroupDetailDto groupDetailDto = groupService.detailInfo(groupId);
        if (!groupDetailDto.getAppId().equals(appId)) {
            throw Exceptions.throwIllegalStateException("此用户无法查询此组的详细信息");
        }
        ResGroupDetailVo resGroupDetailVo = new ResGroupDetailVo();
        BeanUtils.copyProperties(groupDetailDto, resGroupDetailVo);
        return succeed(resGroupDetailVo);
    }

}
