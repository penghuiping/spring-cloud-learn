package com.php25.usermicroservice.web.model;

import com.php25.common.db.cnd.annotation.Column;
import com.php25.common.db.cnd.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/8/11 23:25
 * @description:
 */
@Getter
@Setter
@Table("t_group")
public class Group implements Persistable<Long> {

    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column("create_date")
    private LocalDateTime createDate;

    @Column("create_user_id")
    private String createUserId;

    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column("last_modified_user_id")
    private String lastModifiedUserId;

    @Column("app_id")
    private String appId;

    @Column
    private Integer enable;

    @Transient
    private boolean isNew=true;

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
