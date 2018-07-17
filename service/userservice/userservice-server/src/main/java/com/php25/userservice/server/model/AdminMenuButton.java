package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 后台管理菜单实体类
 * Created by penghuiping on 1/20/15.
 */
@Data
@Entity
@Table(name = "userservice_menu")
public class AdminMenuButton {
    @Id
    private Long id;//主键id

    @Column(length = 45)
    private String name;//菜单或者按钮名

    @Column
    private String url;//菜单或者按钮对应的接口url

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<AdminMenuButton> children;

    @ManyToOne
    @JoinColumn(name = "parent")
    private AdminMenuButton parent;//父节点

    @Column(name = "is_leaf")
    private Boolean isLeaf;//是否是叶子节点

    @Column(name = "sort")
    private Integer sort;//用于排序

    @Column(name = "create_time")
    private Date createTime;//创建时间

    @Column(name = "update_time")
    private Date updateTime;//更新时间

    @Column
    private String icon; //图标

    @Column(name = "is_menu")
    private Boolean isMenu;//判断是否是菜单，不是就是按钮

    @Column
    private String description;//描述

    @Column
    private Integer enable;//是否有效 0:无效 1:有效 2:软删除
}
