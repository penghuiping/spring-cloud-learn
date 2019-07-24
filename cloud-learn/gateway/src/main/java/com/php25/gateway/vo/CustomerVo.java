package com.php25.gateway.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author penghuiping
 * @date 2016-09-02
 */
@Getter
@Setter
public class CustomerVo implements Serializable {

    private String id;

    private String username;

    private String email;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime createTime;

    private Integer sex;

    private String nickname;

    private String token;

    private String image;
}
