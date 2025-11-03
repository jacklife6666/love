package com.love.datingapp.security.filter;

import com.love.datingapp.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            Long userId = jwtUtils.validateTokenAndGetUserId(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String role = jwtUtils.validateTokenAndGetRole(token);

                // ==================== 【终极调试代码】 ====================
                System.out.println("\n\n==================== DEBUG INFO ====================");
                System.out.println("请求路径: " + request.getRequestURI());
                System.out.println("从Token中解析出的Role是: " + role);

                List<GrantedAuthority> authorities = Collections.emptyList();
                if (role != null && !role.trim().isEmpty()) {
                    String processedRole = role.trim().toUpperCase();
                    authorities = AuthorityUtils.createAuthorityList("ROLE_" + processedRole);
                }

                System.out.println("创建的权限列表是: " + authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("设置到Security中的用户ID(Principal)是: " + authentication.getPrincipal());
                System.out.println("设置到Security中的权限(Authorities)是: " + authentication.getAuthorities());
                System.out.println("==================================================\n\n");
                // ==========================================================
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}