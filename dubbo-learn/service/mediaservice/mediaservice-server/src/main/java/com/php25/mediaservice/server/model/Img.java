package com.php25.mediaservice.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 图片表,系统图片留痕
 *
 * @author penghuiping
 * @date 2019-01-04
 */
@Data
@Entity
@Table(name = "mediaservice_img")
public class Img {

    @Id
    private String id;

    /**
     * 图片地址 http访问地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 图片名
     */
    @Column
    private String name;

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
     * 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;
}
