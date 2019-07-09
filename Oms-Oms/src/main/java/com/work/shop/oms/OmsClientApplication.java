package com.work.shop.oms;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务启动类
 * @author QuYachu
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.work.shop.stockcenter.client.feign", "com.work.shop.oms.payment.feign",
    "com.work.shop.pca.feign", "com.work.shop.stock.center.feign", "com.work.shop.oms.api.express.feign", "com.work.shop.cloud.feign.*"})
public class OmsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmsClientApplication.class, args);
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {

        //2.添加fastJson的配置信息，比如是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        //序列化格式
        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);

        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

        //1.需要定义一个Convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        //3.在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return new HttpMessageConverters(fastConverter);
    }
}
