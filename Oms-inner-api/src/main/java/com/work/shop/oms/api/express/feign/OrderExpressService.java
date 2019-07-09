package com.work.shop.oms.api.express.feign;

import java.util.List;

import com.work.shop.oms.express.bean.ExpressRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.express.bean.ExpressInfo;

/**
 * feign订单物流服务接口
 * @author
 */
@FeignClient("EXPRESS-SERVICE")
public interface OrderExpressService {

	/**
	 * 订单物流写入
	 * @param orderSn
	 * @return ReturnInfo<String>
	 */
	@PostMapping("/express/orderExpress")
	public ReturnInfo<String> orderExpress(@RequestParam(name="orderSn") String orderSn);

	/**
	 * 退单物流写入
	 * @param returnSn
	 * @return ReturnInfo<String>
	 */
	@PostMapping("/express/orderReturnExpress")
	public ReturnInfo<String> orderReturnExpress(@RequestParam(name="returnSn") String returnSn);

	/**
	 * 订单物流查询（查询条件：订单号）
	 * @param expressRequest
	 * @return ReturnInfo<List<ExpressInfo>>
	 */
	@PostMapping("/express/orderExpressQuery")
	ReturnInfo<List<ExpressInfo>> orderExpressQuery(ExpressRequest expressRequest);

	/**
	 * oms订单物流查询（查询条件：订单号、仓编码、快递单号）
	 * @param expressRequest
	 * @return ReturnInfo<List<ExpressInfo>>
	 */
	@PostMapping("/express/orderExpressOmsQuery")
	ReturnInfo<List<ExpressInfo>> orderExpressOmsQuery(ExpressRequest expressRequest);
}
