package com.sp.spring.jwt.auth;


import com.sun.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TokenManager _tokenManager;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
/**
 *  "Bearer [Token]"
 */
        final String authHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String userName = null;
        if(authHeader!=null&&authHeader.contains("Bearer")){
            token = authHeader.substring(7);
            try{
                userName = _tokenManager.getUserFromToken(token);
                System.out.println(userName);
            }catch (Exception e){
                System.out.println(e);
            }
        }
        if(userName!=null &&token!=null
                && SecurityContextHolder.getContext().getAuthentication()==null){
            if(_tokenManager.tokenValidate(token)){
                UsernamePasswordAuthenticationToken upassToken = new UsernamePasswordAuthenticationToken(userName,null,new ArrayList<>());
                upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upassToken);
            }

        }
        //Isleme Devam
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
