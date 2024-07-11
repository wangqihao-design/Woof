package org.lls.woof.woofauth.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lls.woof.woofauth.dao.UserDao;
import org.lls.woof.woofauth.entity.dto.User;
import org.lls.woof.woofauth.service.impl.RedisTokenService;
import org.lls.woof.woofauth.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Service
public class RedisBlackListFilter extends OncePerRequestFilter {

    private final RedisTokenService redisTokenService;
    private final JwtUtils jwtUtils;
    private final UserDao userdao;

    public RedisBlackListFilter(RedisTokenService redisTokenService, JwtUtils jwtUtils, UserDao userdao) {
        this.redisTokenService = redisTokenService;
        this.jwtUtils = jwtUtils;

        this.userdao = userdao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);

                User user = userdao.getByUsername(username);
                Set<String> tokens = redisTokenService.getUserTokens("usertoken:" + user.getId());
                System.out.println(tokens);
                System.out.println(jwt);
                if (tokens.contains(jwt)) {
                    throw new Exception("Token in BlackList, Auth Failed");
                }
            }
        }
        catch (Exception e) {
            logger.error("Auth failed from Redis BlackList", e);
            return;
        }

        filterChain.doFilter(request, response);

    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }


}
