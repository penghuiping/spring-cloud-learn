package com.php25.userservice.server.constant;

/**
 * Created by penghuiping on 2017/9/7.
 */
public enum CustomerUuidType {

    weixin(0), qq(1), weibo(2);

    public int value;

    CustomerUuidType(int value) {
        this.value = value;
    }

    public String getName() {
        return this.name();
    }
}
