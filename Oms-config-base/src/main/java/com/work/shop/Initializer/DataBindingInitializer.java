package com.work.shop.Initializer;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class DataBindingInitializer implements WebBindingInitializer {

	public void initBinder(WebDataBinder binder) {
		binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}

	/*@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		// TODO Auto-generated method stub
		binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
	}*/

}
