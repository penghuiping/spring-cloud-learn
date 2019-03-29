package com.php25.authserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: penghuiping
 * @date: 2019/3/27 22:47
 * @description:
 */
@Slf4j
//@Controller
//// 必须配置
//@SessionAttributes("authorizationRequest")
public class AuthController {

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        log.info("123123123123123");
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("base-grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        return view;
    }
}
