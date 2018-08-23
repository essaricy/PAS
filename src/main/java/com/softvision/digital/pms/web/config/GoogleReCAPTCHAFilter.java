package com.softvision.digital.pms.web.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//@Slf4j
//@Component
public class GoogleReCAPTCHAFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Nothing to do
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Nothing to do
	}

}
