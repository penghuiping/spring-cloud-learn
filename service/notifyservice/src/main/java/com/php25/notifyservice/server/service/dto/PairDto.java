package com.php25.notifyservice.server.service.dto;

import lombok.Data;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 10:42
 * @Description:
 */
@Data
public class PairDto<K, V> {

    private K key;

    private V value;
}
