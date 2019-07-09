package com.work.shop.oms.dao.define;

import com.work.shop.oms.api.bean.GoodsReturnChangeDetailBean;
import com.work.shop.oms.bean.GoodsReturnChangeDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface GoodsReturnChangeStMapper {

	//批量新增退换单申请明细
	 void batchInsertReturnChangeDetail(List<GoodsReturnChangeDetail> list);

    /**
     * 获取售后申请单详情
     * @param paramMap
     * @return
     */
	 public List<GoodsReturnChangeDetailBean> getGoodsReturnChangDetail(Map<String, Object> paramMap);
}
