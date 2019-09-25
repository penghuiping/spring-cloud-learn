package com.php25.h5api.constant;

import com.php25.common.core.exception.BusinessErrorStatus;

/**
 * @author: penghuiping
 * @date: 2019/1/3 18:34
 * @description:
 */
public enum BusinessError implements BusinessErrorStatus {
    COMMON_ERROR("10000", "出错啦,请重试");


    public String code;

    public String desc;

    BusinessError(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
