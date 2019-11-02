package com.work.shop.oms.user.account;

import com.work.shop.oms.bean.AccountSettlementOrderBean;
import com.work.shop.oms.bean.UserAccountBean;
import com.work.shop.oms.common.bean.ReturnInfo;

/**
 * 用户账户服务
 * @author QuYachu
 */
public interface UserAccountService {

    /**
     * 处理用户信用额度
     * @param userAccountBean
     * @return ReturnInfo<Boolean>
     */
    ReturnInfo<Boolean> doReduceUserAccount(UserAccountBean userAccountBean);

    /**
     * 处理用户结算账户
     * @param accountSettlementOrderBean
     * @return
     */
    ReturnInfo<Boolean> doUserSettlementAccount(AccountSettlementOrderBean accountSettlementOrderBean);
}
