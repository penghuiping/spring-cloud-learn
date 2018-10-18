package com.php25.userservice.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @author: penghuiping
 * @date: 2018/10/17 17:22
 * @description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminAuthorityDto {

    private Long id;

    /**
     * 权限地址
     */
    private String url;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 对应关联的菜单与按钮表id
     */
    private AdminMenuButtonDto adminMenuButton;
}
