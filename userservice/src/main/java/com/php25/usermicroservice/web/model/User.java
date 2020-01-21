package com.php25.usermicroservice.web.model;

import com.php25.common.db.cnd.annotation.Column;
import com.php25.common.db.cnd.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 后台管理用户实体类
 * Created by penghuiping on 11/30/14.
 */
@Getter
@Setter
@Table("t_user")
public class User implements Persistable<Long> {
    /**
     * 主键id
     */
    @Id
    @Column
    private Long id;

    /**
     * 用户名
     */
    @Column("username")
    private String username;

    /**
     * 昵称
     */
    @Column("nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @Column("email")
    private String email;

    /**
     * 手机
     */
    @Column("mobile")
    private String mobile;

    /**
     * 密码
     */
    @Column
    private String password;

    /**
     * 创建时间
     */
    @Column("create_date")
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;


    /**
     * 是否有效 0:无效,1:有效,2:软删除
     */
    @Column
    private Integer enable;

    @Column(value = "user_id")
    private Set<RoleRef> roles;

    @Column(value = "user_id")
    private Set<GroupRef> groups;

    @Column(value = "user_id")
    private Set<AppRef> apps;

    /**
     * 头像
     */
    @Column(value = "head_image_id")
    private String headImageId;

    @Transient
    private boolean isNew = true;


    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

}