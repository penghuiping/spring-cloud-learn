package com.php25.notifyservice.server.service.dto;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 10:42
 * @Description:
 */
public class PairDto<K, V> {

    private K key;

    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
