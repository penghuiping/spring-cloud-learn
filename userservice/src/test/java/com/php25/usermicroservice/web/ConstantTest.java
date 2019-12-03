package com.php25.usermicroservice.web;

/**
 * @author: penghuiping
 * @date: 2019/8/22 16:17
 * @description:
 */
public class ConstantTest {


    public final static class Admin {
        public static final String username = "admin";
        public static final String nickname = "admin";
        public static final String password = "123456";
        public static final String newPassword = "654321";
        public static final String mobile = "18877777777";
        public static final String email = "222@qq.com";
        public static final String appId = "##admin";
        public static final String appSecret = "123456";
        public static final String appName = "admin";
        public static final String appRedirectUrl = "http://www.admin.com/callback";
        public static final String roleName = "admin";
    }

    public final static class Customer {
        public static final String username = "jack";
        public static final String nickname = "jack";
        public static final String password = "123456";
        public static final String newPassword = "654321";
        public static final String mobile = "18812345678";
        public static final String email = "123@qq.com";
        public static final String appId = "#ajduund";
        public static final String appSecret = "123456";
        public static final String appName = "customer";
        public static final String appRedirectUrl = "http://www.customer.com/callback";
        public static final String roleName = "selfDefineRole";
        public static final String groupName = "selfDefineGroup";
    }


    public final static String AUTHORIZATION_DESC = "内容为:\"Bearer ${access_token}\",access_token可以通过\"获取oauth2的token\"接口获取";

}
