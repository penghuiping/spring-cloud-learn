package com.php25.usermicroservice.web.constant;

/**
 * @author: penghuiping
 * @date: 2019/8/23 14:28
 * @description:
 */
public class Constants {

    public final static class SuperAdmin {
        public static final String username = "super_admin";
        public static final String nickname = "super_admin";
        public static final String password = "123456";
        public static final String newPassword = "654321";
        public static final String mobile = "18888888888";
        public static final String email = "111@qq.com";
        public static final String appId = "##super_admin";
        public static final String appSecret = "123456";
        public static final String appName = "superAdmin";
        public static final String appRedirectUrl = "http://www.superadmin.com/callback";
    }

    public static class Role {
        public static final String ADMIN = "admin";

        public static final String CUSTOMER = "customer";

        public static final String SUPER_ADMIN = "super_admin";
    }
}
