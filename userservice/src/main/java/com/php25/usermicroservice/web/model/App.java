package com.php25.usermicroservice.web.model;

import com.php25.common.db.cnd.GenerationType;
import com.php25.common.db.cnd.annotation.Column;
import com.php25.common.db.cnd.annotation.GeneratedValue;
import com.php25.common.db.cnd.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/4/1 16:50
 * @description:
 */
@Getter
@Setter
@Table("t_app")
public class App implements Persistable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column("app_id")
    private String appId;

    @Column("app_name")
    private String appName;

    @Column("app_secret")
    private String appSecret;

    @Column("registered_redirect_uri")
    private String registeredRedirectUri;

    @CreatedDate
    @Column("register_date")
    private LocalDateTime registerDate;

    private Integer enable;

    @Override
    public String getId() {
        return this.appId;
    }

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
