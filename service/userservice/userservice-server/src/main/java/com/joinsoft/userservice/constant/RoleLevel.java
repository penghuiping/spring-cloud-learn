package com.joinsoft.userservice.constant;

/**
 * Created by penghuiping on 16/4/20.
 */
public enum RoleLevel {
    级别0(0), 级别1(1), 级别2(2);

    public int value;

    RoleLevel(int value) {
        this.value = value;
    }

    public String getName() {
        return this.name();
    }
}
