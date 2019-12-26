package com.work.shop.oms.controller.service.impl;

import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.controller.service.RewardPointChangeLogService;
import com.work.shop.oms.dao.RewardPointChangeLogMapper;
import com.work.shop.oms.param.bean.ParamRewardPointChangeLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/20 8:32 上午
 * @Version V1.0
 **/

@Service("rewardPointChangeLogService")
public class RewardPointChangeLogServiceImpl implements RewardPointChangeLogService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RewardPointChangeLogMapper rewardPointChangeLogMapper;

    @Override
    public void addRewardPointChangeLog(RewardPointChangeLogBean changeLogBean) {
        rewardPointChangeLogMapper.addRewardPointChangeLog(changeLogBean);
    }

    @Override
    public List<RewardPointChangeLogBean> getRewardPointChangeLog(ParamRewardPointChangeLog param) {
        return rewardPointChangeLogMapper.getRewardPointChangeLog(param);
    }

    @Override
    public int getCountOfRewardPointChangeLog(ParamRewardPointChangeLog param) {
        return rewardPointChangeLogMapper.getCountOfRewardPointChangeLog(param);
    }
}
