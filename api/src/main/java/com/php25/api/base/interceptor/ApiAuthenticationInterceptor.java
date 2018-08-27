package com.php25.api.base.interceptor;


import com.alibaba.dubbo.config.annotation.Reference;
import com.php25.api.base.constant.AccessRequired;
import com.php25.userservice.client.rpc.TokenRpc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Oauth2 token认证
 * Created by penghuiping on 16/8/30.
 */
@Component
public class ApiAuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Reference(check = false)
    TokenRpc<String> tokenRest;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        AccessRequired annotation = method.getAnnotation(AccessRequired.class);
        if (null != annotation) {
            //如果此此方法上有AccessRequired注解，需要token认证
            //先获取loginToken
            String token = request.getParameter("token");
            String value = request.getHeader("Content-Type");
            if ("application/json".equals(value)) {
                response.addHeader("Content-Type", "application/json;charset=UTF-8");
            }

            //如果为空直接返回未登入提示
            if (StringUtils.isEmpty(token)) {
                //返回403状态码 访问被禁止
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            //直接判断redis里的token值是否有效
            if (tokenRest.checkTokenValidation(token, String.class)) return true;

            //返回403状态码 访问被禁止
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
            //直接通过
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
