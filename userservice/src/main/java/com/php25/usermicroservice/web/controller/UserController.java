package com.php25.usermicroservice.web.controller;

import com.google.common.collect.Lists;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.dto.UserChangeDto;
import com.php25.usermicroservice.web.dto.UserDetailDto;
import com.php25.usermicroservice.web.dto.UserPageDto;
import com.php25.usermicroservice.web.dto.UserRegisterDto;
import com.php25.usermicroservice.web.service.UserService;
import com.php25.usermicroservice.web.vo.req.ReqAuthorizeRoleVo;
import com.php25.usermicroservice.web.vo.req.ReqChangePasswordVo;
import com.php25.usermicroservice.web.vo.req.ReqChangeUserInfo;
import com.php25.usermicroservice.web.vo.req.ReqJoinGroupVo;
import com.php25.usermicroservice.web.vo.req.ReqLeaveGroupVo;
import com.php25.usermicroservice.web.vo.req.ReqRegisterUserVo;
import com.php25.usermicroservice.web.vo.req.ReqRevokeRoleVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResUserDetailVo;
import com.php25.usermicroservice.web.vo.res.ResUserPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/8/15 10:09
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends JSONController {

    @Autowired
    private UserService userService;

    @Autowired
    private Processor processor;


    @PostMapping("/register")
    public JSONResponse register(@Valid @RequestBody ReqRegisterUserVo reqRegisterUserVo) {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        BeanUtils.copyProperties(reqRegisterUserVo, userRegisterDto);
        return succeed(userService.register(userRegisterDto));
    }


    @PostMapping("/changePassword")
    public JSONResponse changePasswordByUsername(@NotBlank @RequestAttribute String username, @Valid @RequestBody ReqChangePasswordVo reqChangePasswordVo) {
        return succeed(userService.changePasswordByUsername(username,
                reqChangePasswordVo.getOriginPassword(),
                reqChangePasswordVo.getNewPassword()));
    }

    @PostMapping("/detailInfo")
    public JSONResponse detailInfo(@NotBlank @RequestAttribute String username) {
        UserDetailDto userDetailDto = userService.detailInfo(username);
        Map<String,String> headers = new HashMap<>();
        headers.put("type","auditlogservice");
        GenericMessage<String> genericMessage = new GenericMessage(String.format("用户名为:%s，访问了/detailInfo", username),headers);
        processor.output().send(genericMessage);
        ResUserDetailVo resUserDetailVo = new ResUserDetailVo();
        BeanUtils.copyProperties(userDetailDto, resUserDetailVo);
        return succeed(resUserDetailVo);
    }


    @PostMapping("/changeUserInfo")
    public JSONResponse changeUserInfo(@NotBlank @RequestAttribute String username, @Valid @RequestBody ReqChangeUserInfo reqChangeUserInfo) {
        UserChangeDto userChangeDto = new UserChangeDto();
        BeanUtils.copyProperties(reqChangeUserInfo, userChangeDto);
        Boolean result = userService.changeUserInfo(username, userChangeDto);
        return succeed(result);
    }

    @PostMapping("/admin/queryPage")
    public JSONResponse queryPage(@Valid @RequestBody SearchVo searchVo) {
        List<SearchParam> searchParamList = null;
        String property = "id";
        Sort.Direction direction = Sort.Direction.ASC;
        if (null == searchVo.getSearchParamVoList()) {
            searchParamList = Lists.newArrayList();
        } else {
            searchParamList = searchVo.getSearchParamVoList().stream()
                    .map(searchParamVo -> SearchParam
                            .of(searchParamVo.getFieldName(),
                                    searchParamVo.getOperator(),
                                    searchParamVo.getValue()))
                    .collect(Collectors.toList());
        }
        List<UserPageDto> userPageDtos = userService.queryPage(searchVo.getPageNum(), searchVo.getPageSize(), searchParamList, property, direction);

        if (userPageDtos == null || userPageDtos.isEmpty()) {
            return succeed(Lists.newArrayList());
        } else {
            List<ResUserPageVo> result = userPageDtos.stream().map(userPageDto -> {
                ResUserPageVo resUserPageVo = new ResUserPageVo();
                BeanUtils.copyProperties(userPageDto, resUserPageVo);
                return resUserPageVo;
            }).collect(Collectors.toList());
            return succeed(result);
        }
    }

    @PostMapping("/admin/authorizeRole")
    public JSONResponse authorizeRole(@NotBlank @RequestAttribute String appId, @Valid @RequestBody ReqAuthorizeRoleVo reqAuthorizeRoleVo) {
        Boolean result = userService.authorizeRole(appId, reqAuthorizeRoleVo.getUserId(), reqAuthorizeRoleVo.getRoleId());
        return succeed(result);
    }

    @PostMapping("/admin/revokeRole")
    public JSONResponse revokeRole(@NotBlank @RequestAttribute String appId, @Valid @RequestBody ReqRevokeRoleVo reqRevokeRoleVo) {
        Boolean result = userService.revokeRole(appId, reqRevokeRoleVo.getUserId(), reqRevokeRoleVo.getRoleId());
        return succeed(result);
    }


    @PostMapping("/admin/joinGroup")
    public JSONResponse joinGroup(@NotBlank @RequestAttribute String appId, @Valid @RequestBody ReqJoinGroupVo reqJoinGroupVo) {
        log.info("进入/admin/joinGroup");
        Boolean result = userService.joinGroup(appId, reqJoinGroupVo.getUserId(), reqJoinGroupVo.getGroupId());
        return succeed(result);
    }

    @PostMapping("/admin/leaveGroup")
    public JSONResponse leaveGroup(@NotBlank @RequestAttribute String appId, @Valid @RequestBody ReqLeaveGroupVo reqLeaveGroupVo) {
        Boolean result = userService.leaveGroup(appId, reqLeaveGroupVo.getUserId(), reqLeaveGroupVo.getGroupId());
        return succeed(result);
    }

}
