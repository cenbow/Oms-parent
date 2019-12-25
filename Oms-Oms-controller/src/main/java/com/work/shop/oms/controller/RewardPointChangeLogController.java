package com.work.shop.oms.controller;

import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.common.bean.CommonResultData;
import com.work.shop.oms.controller.service.RewardPointChangeLogService;
import com.work.shop.oms.param.bean.ParamRewardPointChangeLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/20 8:45 上午
 * @Version V1.0
 **/

@RestController
public class RewardPointChangeLogController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RewardPointChangeLogService rewardPointChangeLogService;

    //查询积分变更记录
    @PostMapping("/getRewardPointChangeLog")
    public CommonResultData<List<RewardPointChangeLogBean>> getRewardPointChangeLog(@RequestBody ParamRewardPointChangeLog param) {
        CommonResultData<List<RewardPointChangeLogBean>> result = new CommonResultData<>();
        result.setIsOk("0");

        int count = rewardPointChangeLogService.getCountOfRewardPointChangeLog(param);

        if (count == 0) {
            result.setIsOk("1");
            result.setTotal(0);
            return result;
        }

        if (param.getPageSize() > 50 || param.getPageSize() == 0) {
            param.setPageSize(16);
        }

        int pageCount = (int) Math.ceil(count * 1.0 / param.getPageSize());
        if (param.getCurrentPage() > 1) {
            //查询最后一页的逻辑
            if (param.getCurrentPage() > pageCount) {
                result.setIsOk("1");
                result.setTotal(0);
                return result;
            } else if (param.getCurrentPage() == pageCount) {
                param.setStart(param.getPageSize() * (pageCount - 1));
                param.setPageSize(count - param.getPageSize() * (pageCount - 1));
            } else {
                param.setStart(param.getCurrentPage() * (param.getPageSize() - 1));
            }
        } else {
            param.setStart(0);
        }

        List<RewardPointChangeLogBean> resultList = rewardPointChangeLogService.getRewardPointChangeLog(param);
        result.setIsOk("1");
        result.setTotal(resultList.size());
        result.setResult(resultList);
        return result;
    }
}
