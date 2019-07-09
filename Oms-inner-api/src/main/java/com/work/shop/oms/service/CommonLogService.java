package com.work.shop.oms.service;


import java.util.Map;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.vo.AdminUser;


public interface CommonLogService {
	public Paging getMasterOrderAction(AdminUser adminUser, String masterOrderSn, Integer isHistory);
	
	public Paging getDistributeOrderAction(AdminUser adminUser, String orderSn, Integer isHistory);
	
	public Map<String,String> decodeLinkMoble(AdminUser adminUser,
			String mobile, String tel, String masterOrderSn,String returnSn);
	
	public Paging getGoodsReturnChangeAction(AdminUser adminUser, String orderSn, Integer isHistory);

}
