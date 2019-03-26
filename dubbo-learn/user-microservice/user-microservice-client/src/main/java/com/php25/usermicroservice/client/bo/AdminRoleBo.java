package com.php25.usermicroservice.client.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by penghuiping on 2016/2/20.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminRoleBo implements Serializable {

    private Long id;

    private String name;

    private String code;//角色代码

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private Integer enable;

    private List<AdminMenuButtonBo> menus;
}
