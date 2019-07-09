package com.work.shop.oms.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.work.shop.oms.utils.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.ApplyItem;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.bean.GoodsReturnChangeActionExample;
import com.work.shop.oms.bean.GoodsReturnChangeExample;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.GoodsReturnChangeActionMapper;
import com.work.shop.oms.dao.GoodsReturnChangeMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.define.GoodsReturnChangePageListMapper;
import com.work.shop.oms.dao.define.OrderMapper;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.order.service.ApplyManagementService;
import com.work.shop.oms.order.service.GoodsReturnChangeService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单售后申请服务
 * @author lemon
 */
@Service
public class ApplyManagementServiceImpl implements ApplyManagementService {
	
	private static final Logger logger = Logger.getLogger(ApplyManagementServiceImpl.class);

	@Resource
	private GoodsReturnChangePageListMapper goodsReturnChangePageListMapper;

	@Resource
	private GoodsReturnChangeActionMapper goodsReturnChangeActionMapper;

	@Resource
	private GoodsReturnChangeMapper goodsReturnChangeMapper;

	@Resource(name="goodsReturnChangeService")
	private GoodsReturnChangeService goodsReturnChangeService;

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	/**
	 * 根据退换货申请单号, 获取申请日志
	 * @param returnChangeSn 申请单号
	 * @return OmsBaseResponse<GoodsReturnChangeAction>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChangeAction> findActionByReturnChangeSn(String returnChangeSn) {
		logger.info("查询退单申请单日志returnChangeSn:" + returnChangeSn);
		OmsBaseResponse<GoodsReturnChangeAction> response = new OmsBaseResponse<GoodsReturnChangeAction>();
		response.setSuccess(false);
		response.setMessage("查询失败");
		GoodsReturnChangeActionExample example = new GoodsReturnChangeActionExample();
		GoodsReturnChangeActionExample.Criteria criteria = example.or();
		criteria.andReturnchangeSnEqualTo(returnChangeSn);
		List<GoodsReturnChangeAction> actions = goodsReturnChangeActionMapper.selectByExampleWithBLOBs(example);
		response.setList(actions);
		response.setSuccess(true);
		response.setMessage("查询成功");
		return response;
	}

	/**
	 * 根据申请单id,获取申请单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChange>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeById(OmsBaseRequest<ApplyItem> request) {
		logger.info("查询退单申请单日志request:" + JSON.toJSONString(request));
		OmsBaseResponse<GoodsReturnChange> response = new OmsBaseResponse<GoodsReturnChange>();
		response.setSuccess(false);
		response.setMessage("查询退单申请单日志失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null || item.getApplyId() == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		try {
			GoodsReturnChange change = goodsReturnChangeMapper.selectByPrimaryKey(item.getApplyId());
			response.setData(change);
			response.setSuccess(true);
			response.setMessage("查询退单申请单日志成功");
		} catch (Exception e) {
			logger.error("查询退单申请单日志失败" + e.getMessage(), e);
			response.setMessage("查询退单申请单日志失败" + e.getMessage());
		}
		return response;
	}

	/**
	 * 修改申请单状态
	 * @param request 请求参数
	 * @return OmsBaseResponse<String>
	 */
	@Override
	public OmsBaseResponse<String> updateStatusBatch(OmsBaseRequest<ApplyItem> request) {
		logger.info("退单申请单变更状态:request" + JSON.toJSONString(request));
		OmsBaseResponse<String> response = new OmsBaseResponse<String>();
		response.setSuccess(false);
		response.setMessage("退单申请单变更状态失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		List<Integer> applyIds = item.getApplyIds();
		if (StringUtil.isListNull(applyIds)) {
			response.setMessage("申请单ID列表不能为空");
			return response;
		}
		Integer applyStatus = item.getApplyStatus();
		if (applyStatus == null) {
			response.setMessage("申请单状态不能为空");
			return response;
		}
		try {
			for (Integer id : applyIds) {
				GoodsReturnChange goodsReturnChange = goodsReturnChangeMapper.selectByPrimaryKey(id);
				ReturnInfo<String> returnInfo = goodsReturnChangeService.updateStatus(applyStatus, id, goodsReturnChange.getOrderSn(), request.getActionUser(), "更新");
				if (returnInfo.getIsOk() == Constant.OS_NO) {
					response.setMessage(id + "退单申请单变更状态失败" + returnInfo.getMessage());
					return response;
				}
			}
			response.setSuccess(true);
			response.setMessage("退单申请单变更状态成功");
		} catch (Exception e) {
			logger.error("退单申请单变更状态失败" + e.getMessage(), e);
			response.setMessage("退单申请单变更状态失败" + e.getMessage());
		}
		return response;
	}

	/**
	 * 根据订单号，获取退换货申请单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChangeInfoVO>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChangeInfoVO> findGoodsReturnChangeBySn(OmsBaseRequest<ApplyItem> request) {
		logger.info("退单申请单查询:request" + JSON.toJSONString(request));
		OmsBaseResponse<GoodsReturnChangeInfoVO> response = new OmsBaseResponse<GoodsReturnChangeInfoVO>();
		response.setSuccess(false);
		response.setMessage("查询失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		String orderSn = item.getOrderSn();
		if (StringUtil.isTrimEmpty(orderSn)) {
			response.setMessage("查询单号为空");
			return response;
		}
		try {
			List<GoodsReturnChangeInfoVO> list = orderMapper.selectGoodsReturnChangeBySn(orderSn);
			for (GoodsReturnChangeInfoVO goodsReturnChangeInfoVO : list) {
				MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
				goodsExample.or().andMasterOrderSnEqualTo(orderSn).andCustomCodeEqualTo(goodsReturnChangeInfoVO.getCustumCode());
				List<MasterOrderGoods> orderGoods = masterOrderGoodsMapper.selectByExample(goodsExample);
				if(CollectionUtils.isEmpty(orderGoods)){
					throw new RuntimeException("无法获取有效的商品基础数据");
				}
				MasterOrderGoods masterOrderGoods = orderGoods.get(0);
				//加载商品基础数据
				goodsReturnChangeInfoVO.setGoodsName(masterOrderGoods.getGoodsName());
				goodsReturnChangeInfoVO.setGoodsSn(masterOrderGoods.getGoodsSn());
				goodsReturnChangeInfoVO.setColorName(masterOrderGoods.getGoodsColorName());
				goodsReturnChangeInfoVO.setSizeName(masterOrderGoods.getGoodsSizeName());
				goodsReturnChangeInfoVO.setGoodsNumber(orderGoods.size());
			}
			response.setSuccess(true);
			response.setMessage("查询成功");
			response.setList(list);
		} catch (Exception e) {
			logger.error("退单申请单变更状态失败" + e.getMessage(), e);
			response.setMessage("退单申请单变更状态失败" + e.getMessage());
		}
		return response;
	}

	/**
	 * 根据订单号，查询退换货申请单
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChange>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeByOrderSn(OmsBaseRequest<ApplyItem> request) {
		OmsBaseResponse<GoodsReturnChange> response = new OmsBaseResponse<GoodsReturnChange>();
		response.setSuccess(false);
		response.setMessage("查询失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		String orderSn = item.getOrderSn();
		if (StringUtil.isTrimEmpty(orderSn)) {
			response.setMessage("查询单号为空");
			return response;
		}
		try {
			GoodsReturnChangeExample example = new GoodsReturnChangeExample();
			example.or().andOrderSnEqualTo(orderSn);
			List<GoodsReturnChange> changes = goodsReturnChangeMapper.selectByExample(example);
			response.setSuccess(true);
			response.setMessage("查询成功");
			response.setList(changes);
		} catch (Exception e) {
			logger.error("查询失败" + e.getMessage(), e);
			response.setMessage("查询失败" + e.getMessage());
		}
		return response;
	}
}
