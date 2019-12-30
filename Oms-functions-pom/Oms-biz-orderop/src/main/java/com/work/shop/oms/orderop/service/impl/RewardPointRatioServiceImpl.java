package com.work.shop.oms.orderop.service.impl;

import com.work.shop.oms.service.RewardPointRatioService;
import com.work.shop.oms.dao.RewardPointRatioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/24 2:40 下午
 * @Version V1.0
 **/

@Service("rewardPointRatioService")
public class RewardPointRatioServiceImpl implements RewardPointRatioService {

    @Autowired
    private RewardPointRatioMapper rewardPointRatioMapper;

    @Override
    public int getRewardPointRatio() {
        return rewardPointRatioMapper.getRewardPointRatio();
    }
}
