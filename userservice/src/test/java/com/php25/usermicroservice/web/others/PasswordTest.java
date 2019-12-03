package com.php25.usermicroservice.web.others;

import com.php25.usermicroservice.web.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author penghuiping
 * @date 2019/10/23 14:44
 */
@Slf4j
public class PasswordTest {

    @Test
    public void BCryptPasswordEncoderTest() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(Constants.SuperAdmin.password);
        log.info("加密后的密码:{}",encodedPassword);
    }
}
