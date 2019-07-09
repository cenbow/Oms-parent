package com.work.shop.oms.controller.task;

import com.work.shop.oms.core.task.ITaskServiceCall;
import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 订单任务
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class OrderTaskController {

    private static final Logger logger = Logger.getLogger(OrderTaskController.class);

    @Resource
    private ITaskServiceCall taskServiceCall;

    /**
     * 任务处理
     * @param param
     * @return String
     */
    @RequestMapping("/doTask")
    public String doTask(@RequestBody Map<String, String> param) {
        taskServiceCall.processTask(param);
        return Constant.OS_STR_YES;
    }

}
