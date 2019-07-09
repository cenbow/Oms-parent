package com.work.shop.oms.express.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class WebApplicationContextUtil {

	
	public static WebApplicationContext webApplicationContext = null;
	
	static{
		webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
	}
}
