package com.joinsoft.api;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by penghuiping on 2018/3/29.
 */
public class LogstashTest extends BaseTest {

    public static final Logger logger = Logger.getLogger(LogstashTest.class);

    @Test
    public void test() {
        logger.debug("this is a debug message");
        logger.info("this is a info message");
        logger.warn("this is a warn message");
        logger.error("this is a error message");
    }
}
