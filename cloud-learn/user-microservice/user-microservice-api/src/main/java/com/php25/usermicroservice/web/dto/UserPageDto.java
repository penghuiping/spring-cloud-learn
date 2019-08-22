package com.php25.usermicroservice.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/8/14 13:28
 * @description:
 */
@Setter
@Getter
public class UserPageDto {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    private LocalDateTime createDate;

    private LocalDateTime lastModifiedDate;

    private Integer enable;
}
