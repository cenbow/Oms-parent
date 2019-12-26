package com.work.shop.oms.controller.service;

import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.param.bean.ParamRewardPointChangeLog;

import java.util.List;

public interface RewardPointChangeLogService {

    void addRewardPointChangeLog(RewardPointChangeLogBean changeLogBean);

    List<RewardPointChangeLogBean> getRewardPointChangeLog(ParamRewardPointChangeLog param);

    //查询积分变更记录数量
    int getCountOfRewardPointChangeLog(ParamRewardPointChangeLog param);
}
