package com.php25.usermicroservice.web.controller;

import com.php25.common.core.exception.Exceptions;
import com.php25.common.flux.trace.TracedWrapper;
import com.php25.usermicroservice.web.dto.AppRefDto;
import com.php25.usermicroservice.web.dto.UserDetailDto;
import com.php25.usermicroservice.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2019/8/21 18:22
 * @description:
 */
@Controller
@RequestMapping("/oauth2")
public class OauthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private TracedWrapper tracedWrapper;

    @PostMapping(value = "/authorize")
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters,
                                  SessionStatus sessionStatus, Principal principal) {
        UserDetailDto userDetailDto = tracedWrapper.wrap("userService.detailInfo",() -> {
            return userService.detailInfo(principal.getName());
        });

        if (null != userDetailDto.getApps() && !userDetailDto.getApps().isEmpty()) {
            try {
                ModelAndView modelAndView = authorizationEndpoint.authorize(model, parameters, sessionStatus, principal);
                String appId = parameters.getOrDefault("client_id", "");
                Optional<AppRefDto> appRefDtoOptional = userDetailDto.getApps().stream().filter(appRefDto -> appRefDto.getAppId().equals(appId)).findAny();
                if (!appRefDtoOptional.isPresent()) {
                    throw Exceptions.throwIllegalStateException("无法再此appId下找到对应用户");
                }
                return modelAndView;
            } catch (Exception e) {
                throw Exceptions.throwIllegalStateException(e.getMessage());
            }
        } else {
            throw Exceptions.throwIllegalStateException("无法再此appId下找到对应用户");
        }
    }


    @PostMapping(value = "/token")
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) {
        try {
            return tokenEndpoint.postAccessToken(principal, parameters);
        } catch (Exception e) {
            throw Exceptions.throwIllegalStateException(e.getMessage());
        }
    }

}
