package com.work.shop.oms.orderop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.cloud.account.UserAccountRequestBean;
import com.work.shop.cloud.api.bean.ResultMsgBean;
import com.work.shop.cloud.bean.usercompany.UserAccountInfo;
import com.work.shop.cloud.feign.account.UserAccountInfoFeign;
import com.work.shop.oms.bean.UserAccountBean;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.user.account.UserAccountService;
import com.work.shop.oms.utils.Constant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户账号服务
 * @author QuYachu
 */
@Service("userAccountService")
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger logger = Logger.getLogger(UserAccountServiceImpl.class);

    @Resource
    private UserAccountInfoFeign userAccountInfoFeign;

    /**
     * 处理用户信用额度
     * @param userAccountBean
     * @return ReturnInfo<Boolean>
     */
    @Override
    public ReturnInfo<Boolean> doReduceUserAccount(UserAccountBean userAccountBean) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("处理失败");

        try {
            UserAccountRequestBean userAccountRequestBean = new UserAccountRequestBean();
            // 在线支付
            userAccountRequestBean.setType(userAccountBean.getType());
            userAccountRequestBean.setUserId(userAccountBean.getUserId());
            userAccountRequestBean.setMoney(userAccountBean.getMoney());
            userAccountRequestBean.setOrderNo(userAccountBean.getOrderNo());
            userAccountRequestBean.setActionUser(userAccountBean.getActionUser());
            ResultMsgBean<UserAccountInfo> resultMsgBean = userAccountInfoFeign.doUserAccountInfo(userAccountRequestBean);
            if (resultMsgBean != null && Constant.OS_YES == resultMsgBean.getIsOk()) {
                returnInfo.setMessage("处理成功");
                returnInfo.setIsOk(Constant.OS_YES);
                returnInfo.setData(true);
                return returnInfo;
            } else {
                logger.error("处理用户账户信息失败:" + JSONObject.toJSONString(userAccountRequestBean) + ",resultMsgBean:" + JSONObject.toJSONString(resultMsgBean));
                returnInfo.setMessage(resultMsgBean.getMsg());
            }
        } catch (Exception e) {
            logger.error("处理用户账户信息异常:", e);
        }
        return returnInfo;
    }
}
