package com.work.shop.oms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 启动配置文件
 * @author QuYachu
 */
@Configuration
@ImportResource(locations = {"classpath*:spring/**/spring-*.xml"})
public class Config {

}
