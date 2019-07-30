package com.php25.authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: penghuiping
 * @date: 2019/7/30 14:55
 * @description:
 */
@RestController
public class Oauth2Controller {

    @RequestMapping("/callback")
    public ResponseEntity callback(String code) {
        return ResponseEntity.ok(code);
    }
}
