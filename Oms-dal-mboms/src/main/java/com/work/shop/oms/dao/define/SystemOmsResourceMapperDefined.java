package com.work.shop.oms.dao.define;

import java.util.List;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.common.bean.SystemOmsResourceVo;
import com.work.shop.oms.common.paraBean.SystemOmsResourceSearchExample;

public interface SystemOmsResourceMapperDefined {
	@ReadOnly
	List<SystemOmsResource> selectSystemResourceByRoleCodes(List<String> roleCodes);
	
	@ReadOnly
	List<SystemOmsResourceVo> selectSystemOmsResourceByExample(SystemOmsResourceSearchExample systemOmsResourceVo);

}
