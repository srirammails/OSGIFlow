package com.flow.camel.osgi;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class BaseServletRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
//		from("netty-http:http://localhost:80/")
//		.process(new Processor() {
//			@Override
//			public void process(Exchange exch) throws Exception {
//				exch.getOut().setBody("test port 80 ");
//			}
//		})
//		.end();
//		
//		from("netty-http:http://127.0.0.1:8080/test")
//		.process(new Processor() {
//			@Override
//			public void process(Exchange exch) throws Exception {
//				exch.getOut().setBody("test port also 80");
//			}
//		})
//		.end();
	
		

		from("direct:vaadintest")
		.process(new Processor() {
			
			@Override
			public void process(Exchange arg0) throws Exception {
				// TODO Auto-generated method stub
				
			}
		}).end();
	}	
	
	
	

}