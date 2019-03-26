package com.php25.userservice.server.constant;

/**
 * Created by penghuiping on 2017/1/22.
 */
public interface Constant {
    String SESSION_USER = "USER";
    String SUCCESS = "成功啦!";
    String FAILURE = "出错啦!";
    Integer PAGE_SIZE = 5;
    Integer PAGE_FIRST = 1;

    Long SMS_EXPIRE_TIME = 15 * 60l;

    interface RANDOM {
        String ACCESS_TOKEN_PREFIX = "01";
        String IMAGE_ID_PREFIX = "02";
    }
}
