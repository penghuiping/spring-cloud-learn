package com.php25.userservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 菜单或者按钮名
     */
    @Column
    private String name;

    /**
     * 菜单或者按钮对应的接口url
     */
    @Column
    private String url;

    private List<AdminMenuButton> children;

    /**
     * 父节点
     */
    @Column(name = "parent")
    private AdminMenuButton parent;

    /**
     * 是否是叶子节点
     */
    @Column(name = "is_leaf")
    private Boolean isLeaf;

    /**
     * 用于排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 图标
     */
    @Column
    private String icon;

    /**
     * 判断是否是菜单，不是就是按钮
     */
    @Column(name = "is_menu")
    private Boolean isMenu;

    /**
     * 描述
     */
    @Column
    private String description;

    /**
     * 是否有效 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;
}
