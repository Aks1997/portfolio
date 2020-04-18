package com.portfolio.base.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.base.common.BaseException;
import com.portfolio.base.common.CommonConstants;
import com.portfolio.base.security.JwtManager;

@Component
public class RequestInterceptor implements HandlerInterceptor  {

	@Autowired
	JwtManager jwtManager;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("after completion");
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("post handle");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("pre handle");
		String token= request.getHeader(CommonConstants.ACCESS_TOKEN);
		if(token!=null && !token.equals("")) {
			String userName=jwtManager.verifyJwt(token);
			if(userName==null || userName.equals("")) {
				throw new BaseException(HttpStatus.UNAUTHORIZED, CommonConstants.INVALID_TOKEN);
			}
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
