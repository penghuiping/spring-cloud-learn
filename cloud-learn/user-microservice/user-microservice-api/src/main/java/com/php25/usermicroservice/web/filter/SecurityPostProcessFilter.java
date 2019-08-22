package com.php25.usermicroservice.web.filter;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * @author: penghuiping
 * @date: 2019/8/22 14:05
 * @description:
 */
public class SecurityPostProcessFilter extends HttpFilter  {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Principal principal = request.getUserPrincipal();
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            String appId = oAuth2Authentication.getOAuth2Request().getClientId();
            String username = request.getRemoteUser();
            request.setAttribute("appId", appId);
            request.setAttribute("username", username);
        }
        chain.doFilter(request, response);
    }

}
