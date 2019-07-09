package com.work.shop.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单支付服务启动类
 * @author QuYachu
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.work.shop.oms.order.feign"})
public class OmsPayClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmsPayClientApplication.class, args);
	}

}
