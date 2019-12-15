package com.php25.usermicroservice.web.controller;

import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.dto.AppDetailDto;
import com.php25.usermicroservice.web.dto.Oauth2TokenDto;
import com.php25.usermicroservice.web.service.AppClientService;
import com.php25.usermicroservice.web.service.UserService;
import com.php25.usermicroservice.web.vo.req.ReqAuthorizeVo;
import com.php25.usermicroservice.web.vo.req.ReqTokenVo;
import com.php25.usermicroservice.web.vo.res.ResTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author: penghuiping
 * @date: 2019/8/21 18:22
 * @description:
 */
@Slf4j
@Controller
@RequestMapping("/oauth2")
public class OauthController extends JSONController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppClientService appClientService;


    @PostMapping(value = "/authorize")
    public ModelAndView authorize(@RequestBody ReqAuthorizeVo reqAuthorizeVo) {
        String code = userService.authorizeCode(reqAuthorizeVo.getUsername(), reqAuthorizeVo.getPassword(), reqAuthorizeVo.getAppId());
        AppDetailDto appDetailDto = appClientService.detailInfo(reqAuthorizeVo.getAppId());
        ModelAndView modelAndView = new ModelAndView();
        RedirectView redirectView = new RedirectView();
        //静默授权
        redirectView.setUrl(appDetailDto.getRegisteredRedirectUri() + "?code=" + code);
        modelAndView.setView(redirectView);
        return modelAndView;
    }


    @PostMapping(value = "/token")
    @ResponseBody
    public JSONResponse getAccessToken(@RequestBody ReqTokenVo reqTokenVo) {
        Oauth2TokenDto oauth2TokenDto = userService.getAccessToken(reqTokenVo.getCode(), reqTokenVo.getAppId(), reqTokenVo.getAppSecret());
        ResTokenVo resTokenVo = new ResTokenVo();
        BeanUtils.copyProperties(oauth2TokenDto, resTokenVo);
        return succeed(resTokenVo);
    }

}
