package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggerInterceptor extends HandlerInterceptorAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//preHandle : 컨트롤러의 메서드에 매핑된 특정 URI를 호출했을 때 컨트롤러에 접근하기 전에 실행되는 메서드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		logger.debug("===============================================");
		logger.debug("==================== BEGIN ====================");
		logger.debug("Request URI ===> " + request.getRequestURI());
		return super.preHandle(request, response, handler);
	}

	//postHandle : 컨트롤러를 경유한 뒤에 뷰(화면)로 결과를 전달하기 전에 실행되는 메서드
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		logger.debug("==================== END ======================");
		logger.debug("===============================================");
	}

}
