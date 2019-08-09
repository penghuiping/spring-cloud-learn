package com.php25.authserver.controller;

import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/30 14:55
 * @description:
 */
@Slf4j
@RestController("/oauth")
public class Oauth2Controller extends JSONController {

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping("/callback")
    public ResponseEntity callback(String code) throws InterruptedException {
        return ResponseEntity.ok(code);
    }

    @GetMapping(value = "/SSOLogout.do")
    public ResponseEntity<JSONResponse> logout(@NotBlank @RequestHeader(name = "token") String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return ResponseEntity.ok(succeed(true));
    }

}
