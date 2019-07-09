package com.work.shop.oms.dao.define;
		
import java.util.List;
import java.util.Map;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.oms.api.bean.OrderReturnForSellers;
import com.work.shop.oms.api.bean.ReturnInfoPage;
import com.work.shop.oms.api.param.bean.ReturnSearchParams;
import com.work.shop.oms.vo.OrderReturnListVO;
import com.work.shop.oms.vo.OrderReturnSearchExample;

public interface OrderReturnSearchMapper {
	
	 @ReadOnly
	 List<OrderReturnListVO> selectPageListByExample(OrderReturnSearchExample example);
	 
	 @ReadOnly
	 int countPageListByExample(OrderReturnSearchExample example);

	 @ReadOnly
	 List<OrderReturnListVO> selectOrderReturnPageListByExample(OrderReturnSearchExample example);

	 @ReadOnly
	 int countOrderReturnPageListByExample(OrderReturnSearchExample example);
	  
	 /**
		 * 退单列表(分页) 导出csv
		 * 
		 * @param model
		 * @param helper
		 * @return Paging
		 */
	 List<OrderReturnListVO> selectOrderReturnPageListExportCsvByExample(OrderReturnSearchExample example);
	
	 /**
		 * 退单列表 总数      导出csv
		 * 
		 * @param model
		 * @param helper
		 * @return Paging
	 */
	 int countPageListExportCsvByExample(OrderReturnSearchExample example);

	 
	 @ReadOnly
	 List<OrderReturnListVO> selectOrderReturnListByExample(OrderReturnSearchExample example);

	 int countOrderReturnListByExample(OrderReturnSearchExample example);
	 
	 /**
	  * 退单商品列表总数
	  * @param example
	  * @return
	  */
	 @ReadOnly
	 int countOrderReturnGoodsListByExample(OrderReturnSearchExample example);
	 
	 /**
	  * 退单商品列表明细
	  * @param example
	  * @return
	  */
	 @ReadOnly
	 List<OrderReturnListVO> selectOrderReturnGoodsListByExample(OrderReturnSearchExample example);
	 
	 /***
	  *财务模版 
	  ****/
	 @ReadOnly
	 List<OrderReturnListVO> selectOrderReturnListByFinancialExample(OrderReturnSearchExample example);

	 /***
	  *财务模版数量 
	  ****/
	 int countOrderReturnListByFinancialExample(OrderReturnSearchExample example);
	
	 /***
	  *物流模版 
	  ****/
	 @ReadOnly
	 List<OrderReturnListVO> selectOrderReturnListByLogisticsExample(OrderReturnSearchExample example);
 
	 /***
	  *物流模版数量 
	  ****/
	 int countOrderReturnListByLogisticsExample(OrderReturnSearchExample example);
	
	 /**
	  * 第三方查询退单接口
	  * @param params
	  * @return
	  */
	 @ReadOnly
	 List<ReturnInfoPage> getReturnInfoPageList(ReturnSearchParams params);
	 @ReadOnly
	 int getReturnInfoPageCount(ReturnSearchParams params);
	 @ReadOnly
	 List<OrderReturnForSellers> getOrderReturnForSeller(Map<String,Object> params);
	 
	 /**
	  * 获取退单信息
	  * @param params
	  * @return
	  */
	 ReturnInfoPage getOrderReturnInfo(ReturnSearchParams params);
}
