package com.work.shop.oms.controller;

import com.work.shop.oms.api.express.service.Express100Service;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 快递100服务
 * @author QuYachu
 */
@RestController
@RequestMapping("/express")
public class Express100Controller {

    private static final Logger logger = Logger.getLogger(Express100Controller.class);

    @Resource
    private Express100Service express100Service;

    /**
     * 快递100服务抓取
     * @return ReturnInfo<String>
     */
    @RequestMapping(value = "/express")
    @ResponseBody
    public ReturnInfo<String> express() {
        ReturnInfo<String> info = new ReturnInfo<>();
        info.setMessage("查询失败");

        try {
            express100Service.express();
            info.setIsOk(Constant.OS_YES);
            info.setMessage("成功");
        } catch (Exception e) {
            logger.error("快递100物流抓取处理异常", e);
        }
        return info;
    }
}
