package com.php25.usermicroservice.web.repository;

import com.php25.common.core.util.JsonUtil;
import com.php25.usermicroservice.web.model.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2019/8/17 01:13
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;



    @Before
    public void setup() {
        
    }


    @Test
    @Sql(statements = {"INSERT INTO `t_user` VALUES ('1', 'jack', 'jack', '123@qq.com', '188123456', '2019-08-17 01:39:59', '2019-08-17 01:40:02', '123456', '1', '1')"})
    public void findByUsername() {
        this.userRepository.findByUsername("jack").ifPresent(user -> {
            Assertions.assertThat(user.getUsername()).isEqualTo("jack");
            log.info("user:{}", JsonUtil.toJson(user));
        });
    }

    @Test
    @Sql(statements = {"INSERT INTO `t_user` VALUES ('1', 'jack', 'jack', '123@qq.com', '188123456', '2019-08-17 01:39:59', '2019-08-17 01:40:02', '123456', '1', '1')"})
    public void findUsernameAndPassword() {
        Optional<User> userOptional = userRepository.findByUsernameAndPassword("jack", "123456");
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Assertions.assertThat(user.getUsername()).isEqualTo("jack");
            log.info("user:{}", JsonUtil.toJson(user));
        } else {
            Assertions.assertThatIllegalStateException();
        }
    }


}
