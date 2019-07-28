package com.php25.usermicroservice.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 后台管理菜单实体类
 * Created by penghuiping on 1/20/15.
 */
@Setter
@Getter
@Table("userservice_menu")
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
     * 父节点
     */
    @Column("parent")
    private Long parentId;

    /**
     * 是否是叶子节点
     */
    @Column("is_leaf")
    private Boolean isLeaf;

    /**
     * 用于排序
     */
    @Column("sort")
    private Integer sort;

    /**
     * 判断是否是菜单，不是就是按钮
     */
    @Column("is_menu")
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
