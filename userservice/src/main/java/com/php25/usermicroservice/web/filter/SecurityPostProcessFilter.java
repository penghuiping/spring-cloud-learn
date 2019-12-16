package com.php25.usermicroservice.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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
@Slf4j
public class SecurityPostProcessFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("进入SecurityPostProcessFilter1....");
        Principal principal = request.getUserPrincipal();
        if (principal instanceof JwtAuthenticationToken) {
            log.info("进入SecurityPostProcessFilter2....");
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            String username = (String) jwtAuthenticationToken.getToken().getClaims().get("username");
            String appId = (String) jwtAuthenticationToken.getToken().getClaims().get("appId");
            request.setAttribute("appId", appId);
            request.setAttribute("username", username);
        }
        chain.doFilter(request, response);
    }

}
