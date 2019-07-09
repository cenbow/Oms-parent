package com.work.shop.oms.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 数据库健康检查
 * @author QuYachu
 */
@Component
public class DbHealthIndicator implements HealthIndicator {

    /**
     * 健康检查
     * @return Health
     */
    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode) .build();
        }
        return Health.up().build();
    }

    /**
     * 检查
     * @return int
     */
    private int check(){
        //可以实现自定义的数据库检测逻辑
        return 0;
    }
}
