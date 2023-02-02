package com.testsecurity.secrets.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class NewSecurityFilterWithToken extends OncePerRequestFilter {


    private  JwtService jwtService;
    private  CustomeUserDetailsServices customeUserDetailsServices;



    public NewSecurityFilterWithToken(JwtService jwtService, CustomeUserDetailsServices customeUserDetailsServices) {
        this.jwtService = jwtService;
        this.customeUserDetailsServices = customeUserDetailsServices;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtService.getTokenFromRequest(request);
        if(!token.isEmpty() && jwtService.isTokenExpired(token)){
            String username = jwtService.getUsernameFromToken(token);
            UserDetails userDetails = customeUserDetailsServices.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/api/auth/**", request.getServletPath());
    }
}
