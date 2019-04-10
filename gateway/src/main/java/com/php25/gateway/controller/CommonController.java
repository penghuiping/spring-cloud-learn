package com.php25.gateway.controller;

import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: penghuiping
 * @date: 2019/4/8 18:25
 * @description:
 */
@RestController
public class CommonController {

    @GetMapping("/fallback")
    public ResponseEntity<?> proxy(ProxyExchange<byte[]> proxy) throws Exception {
        return ResponseEntity.badRequest().body("请稍后再试~");
    }
}
