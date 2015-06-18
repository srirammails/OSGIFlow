package com.flow.shellx.api;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RoutesDefinition;

public interface IShellBusRouteFactoy {

	String getRunnerScope();
	RouteBuilder getRouteBuilder();
	RoutesDefinition getRouteDefinition(final CamelContext context);
	
}
