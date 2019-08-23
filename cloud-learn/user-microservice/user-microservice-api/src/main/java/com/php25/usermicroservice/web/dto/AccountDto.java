package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: penghuiping
 * @date: 2019/8/23 13:39
 * @description:
 */
@Setter
@Getter
public class AccountDto {

    private String username;

    private String password;
}
