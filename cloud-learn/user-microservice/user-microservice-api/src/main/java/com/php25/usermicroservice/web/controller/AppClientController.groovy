package com.php25.usermicroservice.web.controller

import com.php25.common.flux.web.JSONController
import com.php25.usermicroservice.web.service.AppClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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


}
