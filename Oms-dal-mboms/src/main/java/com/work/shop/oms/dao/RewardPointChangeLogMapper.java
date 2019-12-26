package com.work.shop.oms.dao;

import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.param.bean.ParamRewardPointChangeLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("rewardPointChangeLogMapper")
public interface RewardPointChangeLogMapper {

    //查询积分变更记录数量
    int getCountOfRewardPointChangeLog(ParamRewardPointChangeLog param);

    //查询积分变更记录
    List<RewardPointChangeLogBean> getRewardPointChangeLog(ParamRewardPointChangeLog param);

    //新增积分变更记录
    void addRewardPointChangeLog(RewardPointChangeLogBean changeLogBean);

}