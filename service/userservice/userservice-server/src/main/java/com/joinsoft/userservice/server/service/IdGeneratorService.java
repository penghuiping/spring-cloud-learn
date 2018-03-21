package com.joinsoft.userservice.server.service;

/**
 * 随机数生产器
 * @author penghuiping
 * @time 2016/12/17.
 */
public interface IdGeneratorService {

    /**
     * 产生随机的登入token
     *
     * @return
     * @author penghuiping
     * @time 2016/12/17.
     */
    public String generateLoginToken();
}
