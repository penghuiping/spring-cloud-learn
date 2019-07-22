package com.php25.usermicroservice.client.bo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: penghuiping
 * @date: 2018/10/17 17:22
 * @description:
 */
@Setter
@Getter
public class AdminAuthorityBo {

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 对应关联的菜单与按钮表id
     */
    private AdminMenuButtonBo adminMenuButton;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminAuthorityBo that = (AdminAuthorityBo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
