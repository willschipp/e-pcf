package io.pivotal.poc.cf.web;

import io.pivotal.poc.cf.Application;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ConfigDispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{Application.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

}
