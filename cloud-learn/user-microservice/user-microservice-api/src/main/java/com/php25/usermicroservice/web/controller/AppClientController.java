package com.php25.usermicroservice.web.controller;

import com.google.common.collect.Lists;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.dto.AppDetailDto;
import com.php25.usermicroservice.web.dto.AppPageDto;
import com.php25.usermicroservice.web.dto.AppRegisterDto;
import com.php25.usermicroservice.web.service.AppClientService;
import com.php25.usermicroservice.web.vo.req.ReqRegisterAppVo;
import com.php25.usermicroservice.web.vo.req.SearchParamVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResAppDetailVo;
import com.php25.usermicroservice.web.vo.res.ResAppPageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/8/15 18:12
 * @description:
 */
@RestController
@RequestMapping("/appClient")
public class AppClientController extends JSONController {


    @Autowired
    private AppClientService appClientService;


    @GetMapping
    @RequestMapping("/callback")
    public ResponseEntity callback(String code) {
        return ResponseEntity.ok(code);
    }


    @PostMapping
    @RequestMapping("/detailInfo")
    public JSONResponse detailInfo(@NotBlank String appId) {
        AppDetailDto appDetailDto = appClientService.detailInfo(appId);
        ResAppDetailVo resAppDetailVo = new ResAppDetailVo();
        BeanUtils.copyProperties(appDetailDto, resAppDetailVo);
        return succeed(resAppDetailVo);
    }

    @PostMapping
    @RequestMapping("/register")
    public JSONResponse register(@Valid @RequestBody ReqRegisterAppVo reqRegisterAppVo) {
        AppRegisterDto appRegisterDto = new AppRegisterDto();
        appRegisterDto.setAppId(reqRegisterAppVo.getAppId());
        appRegisterDto.setAppSecret(reqRegisterAppVo.getAppSecret());
        appRegisterDto.setRegisteredRedirectUri(reqRegisterAppVo.getRegisteredRedirectUri());
        Boolean result = appClientService.register(appRegisterDto);
        return succeed(result);
    }

    @PostMapping
    @RequestMapping("/unregister")
    public JSONResponse unregister(@NotBlank String appId) {
        Boolean result = appClientService.unregister(appId);
        return succeed(result);
    }

    @PostMapping
    @RequestMapping("/queryPage")
    public JSONResponse queryPage(@RequestBody SearchVo searchVo) {
        List<SearchParamVo> searchParamVoList = searchVo.getSearchParamVoList();
        List<SearchParam> searchParams;
        if (null == searchParamVoList || searchParamVoList.isEmpty()) {
            searchParams = new ArrayList<>();
        } else {
            searchParams = searchParamVoList.stream().map(searchParamVo ->
                    SearchParam.of(searchParamVo.getFieldName(),
                            searchParamVo.getOperator(),
                            searchParamVo.getValue())).collect(Collectors.toList());
        }
        List<AppPageDto> appPageDtos = appClientService.queryPage(searchVo.getPageNum(),
                searchVo.getPageSize(), searchParams,
                "id", Sort.Direction.ASC);

        if (null == appPageDtos || appPageDtos.isEmpty()) {
            return succeed(Lists.newArrayList());
        } else {
            List<ResAppPageVo> resAppPageVos = appPageDtos.stream().map(appPageDto -> {
                ResAppPageVo resAppDetailVo = new ResAppPageVo();
                BeanUtils.copyProperties(appPageDto, resAppDetailVo);
                return resAppDetailVo;
            }).collect(Collectors.toList());
            return succeed(resAppPageVos);
        }
    }


}
