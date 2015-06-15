package com.flow.shellx.proxy;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;


public class LogServiceProxy implements LogService{

	@Override
	public void log(int level, String message) {
	}

	@Override
	public void log(int level, String message, Throwable exception) {
	}

	@Override
	public void log(ServiceReference sr, int level, String message) {
	}

	@Override
	public void log(ServiceReference sr, int level, String message,
			Throwable exception) {
	}

}
