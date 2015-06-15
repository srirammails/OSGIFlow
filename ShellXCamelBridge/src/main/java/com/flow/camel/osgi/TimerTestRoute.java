package com.flow.camel.osgi;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;


public class TimerTestRoute extends  RouteBuilder {


	@Override
	public void configure() throws Exception {
		 
		 from("timer://foo?fixedRate=true&period=1s")
		.routeId("timer")
//		.setBody(body().prepend("Hello Timer! Camel route."))
//		.to("stream:out")
		.process(new Processor() {
			
			@Override
			public void process(Exchange arg0) throws Exception {
				System.out.println("Hello! Camel route.");
			}
		})
		.end();
		 
		 
		
	}
	
	

	public static class PrintOutBean {
		
		public static void consolePrint(){
			System.out.println("Hello! Camel route.");
		}
		
	}
}
