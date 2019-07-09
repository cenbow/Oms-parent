package com.work.shop.oms.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.work.shop.pca.feign.BgProductService;
import com.work.shop.pca.model.BgProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.ChannelInfo;
import com.work.shop.oms.bean.HandOrderBatch;
import com.work.shop.oms.bean.HandOrderBatchExample;
import com.work.shop.oms.bean.HandOrderGoods;
import com.work.shop.oms.bean.HandOrderGoodsExample;
import com.work.shop.oms.bean.HandOrderInfo;
import com.work.shop.oms.bean.HandOrderInfoExample;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.AddressModel;
import com.work.shop.oms.common.bean.DepotModel;
import com.work.shop.oms.common.bean.HandOrderBatchVo;
import com.work.shop.oms.common.bean.HandOrderInfoVo;
import com.work.shop.oms.common.bean.MasterGoods;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.common.bean.OrderCreateReturnInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ShopUserInfo;
import com.work.shop.oms.common.bean.TimeUtil;
import com.work.shop.oms.common.bean.UserModel;
import com.work.shop.oms.dao.HandOrderBatchMapper;
import com.work.shop.oms.dao.HandOrderGoodsMapper;
import com.work.shop.oms.dao.HandOrderInfoMapper;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.orderop.service.HandOrderService;
import com.work.shop.oms.orderop.service.ShopUserService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.pca.common.ResultData;
import com.work.shop.pca.model.BGGoodsInfo;

/**
 * 手工打单服务
 * @author QuYachu
 */
@Service
public class HandOrderServiceImpl implements HandOrderService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private HandOrderBatchMapper handOrderBatchMapper;
	@Resource
	private HandOrderInfoMapper handOrderInfoMapper;
	@Resource
	private HandOrderGoodsMapper handOrderGoodsMapper;
	@Resource(name = "handOrderProviderJmsTemplate")
	private JmsTemplate jmsTemplate;
	@Resource
	private MasterOrderInfoService masterOrderInfoService;

	@Resource
	private BgProductService bgProductService;

	@Resource
	private ChannelInfoService channelInfoService;
	@Resource
	private ShopUserService shopUserService;
	
	
	
	@Override
	public Paging getHardOrderPage(HandOrderBatchVo model, PageHelper helper) {
		logger.info("获取手工打单批次列表 model：" + JSON.toJSONString(model) + ";helper:" + JSON.toJSONString(helper));
		HandOrderBatchExample example = new HandOrderBatchExample();
		example.setOrderByClause("create_time desc");
		HandOrderBatchExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		if (model != null) {
			if (StringUtil.isNotEmpty(model.getBatchNo())) {
				criteria.andBatchNoEqualTo(model.getBatchNo());
			}
			if (StringUtil.isNotEmpty(model.getCreateUser())) {
				criteria.andCreateUserEqualTo(model.getCreateUser());
			}
			if (model.getProcessStatus() != null && model.getProcessStatus() != -1) {
				criteria.andProcessStatusEqualTo(model.getProcessStatus());
			}
			if (model.getCreateOrderStatus() != null && model.getCreateOrderStatus() != -1) {
				criteria.andCreateOrderStatusEqualTo(model.getCreateOrderStatus());
			}
			if (StringUtil.isNotEmpty(model.getStartTime())) {
				criteria.andCreateTimeGreaterThanOrEqualTo(TimeUtil.parseString2Date(model.getStartTime()));
			}
			if (StringUtil.isNotEmpty(model.getEndTime())) {
				criteria.andCreateTimeLessThan(TimeUtil.parseString2Date(model.getEndTime()));
			}
		}
		List<HandOrderBatch> list = handOrderBatchMapper.selectByExample(example);
		int count = handOrderBatchMapper.countByExample(example);
		Paging paging = new Paging(count, list);
		logger.info("获取手工打单批次列表 count：" + count);
		return paging;
	}

	@Override
	public ReturnInfo<String> createHandOrderBatch(HandOrderBatch orderBatch) {
		logger.info("创建手工打单批次 model：" + JSON.toJSONString(orderBatch));
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		try {
			handOrderBatchMapper.insert(orderBatch);
		} catch (Exception e) {
		}
		return info;
	}

	@Override
	public ReturnInfo<String> changeProcessStatus(String batchNo, int processStatus, String userName) {
		logger.info("变更手工打单批次状态 batchNo：" + batchNo + ";processStatus:" + processStatus + ";userName:" + userName);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		try {
			HandOrderBatchExample example = new HandOrderBatchExample();
			HandOrderBatch record = new HandOrderBatch();
			record.setUpdateTime(new Date());
			record.setUpdateUser(userName);
			record.setBatchNo(batchNo);
			record.setProcessStatus(processStatus);
			handOrderBatchMapper.updateByExampleSelective(record, example);
			
		} catch (Exception e) {
			logger.error("", e);
		}
		return info;
	}

	@Override
	public Paging getThirdPartyOrderDetailList(String batchNo, PageHelper helper) {
		logger.info("获取手工打单批次列表 batchNo：" + batchNo + ";helper:" + JSON.toJSONString(helper));
		HandOrderInfoExample example = new HandOrderInfoExample();
		example.setOrderByClause("create_time desc");
		HandOrderInfoExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		criteria.andBatchNoEqualTo(batchNo);
		List<HandOrderInfo> list = handOrderInfoMapper.selectByExample(example);
		int count = handOrderInfoMapper.countByExample(example);
		Paging paging = new Paging(count, list);
		logger.info("获取手工打单批次列表 count：" + count);
		return paging;
	}

	@Override
	public ReturnInfo<List<HandOrderInfoVo>> doImport(MultipartFile myfile, String note,
			String channelCode, String channelName, Integer sourceType) {
		ReturnInfo<List<HandOrderInfoVo>> info = new ReturnInfo<List<HandOrderInfoVo>>(Constant.OS_NO);
		String msg = "批量导入读取数据失败！";
		try{
			//读取文件
			InputStream is = myfile.getInputStream();
			InputStreamReader fis = new InputStreamReader(is, "GBK");
			BufferedReader reader = new BufferedReader(fis);
			String inString = "";
			try {
				int i = 0;
				Map<String, HandOrderInfoVo> map = new HashMap<String, HandOrderInfoVo>();
				while ((inString = reader.readLine()) != null) {
					String flag = "0";//标志项，0表示没有待修改数据，1表示有待修改数据
					StringBuffer errorMsg = new StringBuffer("");//拼装待修改数据提示语句
					if (i != 0) {
						logger.info("inString:" + inString);
						String[] arr = inString.split(",");
						if (arr == null || arr.length == 0) {
							continue;
						}
						if (arr.length < 3) {
							flag = "1";
							errorMsg.append("第" + (i + 1) + "行数据不完整;");
							throw new Exception("第" + (i + 1) + "行数据不完整;");
						} else {
							String userId = arr[0];
							String customCode = arr[1];
							String goodsNumber = arr[2];
							String shippingAddress = null;
							try {
								shippingAddress = arr[3];
							} catch (Exception e) {
								logger.error("收货地址为空 " + e.getMessage(), e);
							}
							// "" 内容行排除
							if (StringUtil.isTrimEmpty(userId) && StringUtil.isTrimEmpty(customCode)
									&& StringUtil.isTrimEmpty(goodsNumber)) {
								continue ;
							}
							if (StringUtil.isTrimEmpty(userId)){
								flag = "1";
								errorMsg.append("第" + (i + 1) + "行用户名不允许为空;");
							}
							if (StringUtil.isTrimEmpty(customCode)){
								flag = "1";
								errorMsg.append("第" + (i + 1) + "行商品货号不允许为空;");
							}
							if (StringUtil.isTrimEmpty(goodsNumber)){
								flag = "1";
								errorMsg.append("第" + (i + 1) + "行商品数量不允许为空;");
							}
							HandOrderInfoVo infoVo = map.get(userId);
							String goodsInfo = customCode + "|" + goodsNumber;
							if (infoVo == null) {
								infoVo = new HandOrderInfoVo();
								String orderId = StringUtil.getSysCode();
								logger.info("inString: orderId:" + orderId);
								infoVo.setOrderId(orderId);
								infoVo.setGoodsInfo(goodsInfo);
								infoVo.setUserId(userId);
								infoVo.setCustomCode(customCode);
								infoVo.setGoodsNumber(Integer.valueOf(goodsNumber.trim()));
								infoVo.setFlag(StringUtil.Null2Str(flag).trim());
								infoVo.setErrorMsg(StringUtil.Null2Str(errorMsg.toString()).trim());
								infoVo.setChannelCode(channelCode);
								infoVo.setChannelName(channelName);
								infoVo.setSourceType(sourceType);
								infoVo.setShippingAddress(shippingAddress);
							} else {
								goodsInfo = goodsInfo + "," + infoVo.getGoodsInfo();
								infoVo.setGoodsInfo(goodsInfo);
								if (flag.equals("1")) {
									infoVo.setFlag(StringUtil.Null2Str(flag).trim());
								} else {
									infoVo.setFlag(infoVo.getFlag());
								}
								infoVo.setErrorMsg(StringUtil.Null2Str(errorMsg.toString()).trim() + infoVo.getErrorMsg());
							}
							map.put(userId, infoVo);
						}
					}
					i++;
				}
				List<HandOrderInfoVo> orderInfoVos = new ArrayList<HandOrderInfoVo>();
				for (String userId : map.keySet()) {
					orderInfoVos.add(map.get(userId));
				}
				msg = "批量导入读取数据成功！";
				info.setMessage(msg);
				info.setIsOk(Constant.OS_YES);
				info.setData(orderInfoVos);
			} catch (Exception e) {
				msg = "数据格式错误！请严格按照模板格式填充数据！(可使用格式刷将模板格式刷到相应的填充数据)" + e.getMessage();
				logger.error(msg, e);
				info.setMessage(msg);
			} finally {
				if (reader != null) {
					reader.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (is != null) {
					is.close();
				}
			}
		}catch(Exception e){
			msg = "数据格式错误！请严格按照模板格式填充数据！(可使用格式刷将模板格式刷到相应的填充数据)" + e.getMessage();
			logger.error(msg, e);
			info.setMessage(msg);
		}
		return info;
	}

	@Override
	public ReturnInfo<String> submitImport(List<HandOrderInfoVo> infoVos, String actionUser) {
		logger.info("保存导入手工打单数据 infoVos:" + JSON.toJSONString(infoVos));
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		Date date = new Date();
		String batchNo = TimeUtil.format3Date(date);
		try {
			String channelCode = infoVos.get(0).getChannelCode();
			String channelName = infoVos.get(0).getChannelName();
			Integer sourceType = infoVos.get(0).getSourceType();
			HandOrderBatch batch = new HandOrderBatch();
			batch.setBatchNo(batchNo);
			batch.setChannelCode(channelCode);
			batch.setChannelName(channelName);
			batch.setCreateTime(date);
			batch.setCreateUser(actionUser);
			batch.setProcessStatus(1);
			batch.setSourceType(sourceType);
			handOrderBatchMapper.insertSelective(batch);
			for (HandOrderInfoVo infoVo : infoVos) {
				HandOrderInfo orderInfo = new HandOrderInfo();
				orderInfo.setBatchNo(batchNo);
				orderInfo.setChannelCode(channelCode);
				orderInfo.setChannelName(channelName);
				orderInfo.setCreateUser(actionUser);
				orderInfo.setOrderId(infoVo.getOrderId());
				orderInfo.setUserId(infoVo.getUserId());
				orderInfo.setSourceType(sourceType);
				orderInfo.setShippingAddress(infoVo.getShippingAddress());
				handOrderInfoMapper.insertSelective(orderInfo);
				String goodsInfo = infoVo.getGoodsInfo();
				String[] skus = goodsInfo.split(",");
				for (String skuinfo : skus) {
					String[] arr = skuinfo.split("\\|");
					HandOrderGoods goods = new HandOrderGoods();
					goods.setBatchNo(batchNo);
					goods.setOrderId(infoVo.getOrderId());
					goods.setCustomCode(arr[0]);
					goods.setGoodsNumber(Integer.valueOf(arr[1]));
					handOrderGoodsMapper.insertSelective(goods);
				}
			}
			doHandOrder(batchNo, actionUser);
			info.setIsOk(Constant.OS_YES);
			info.setMessage("success");
			info.setData(batchNo);
		} catch (Exception e) {
			logger.error(batchNo + "保存导入手工打单数据异常" + e.getMessage(), e);
			info.setMessage(batchNo + "保存导入手工打单数据异常" + e.getMessage());
		}
		return info;
	}
	@Override
	public ReturnInfo<String> doHandOrder(String batchNo, String userName) {
		logger.info("执行手工打单 batchNo：" + batchNo + ";userName:" + userName);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		try {
			HandOrderInfoExample infoExample = new HandOrderInfoExample();
			infoExample.or().andBatchNoEqualTo(batchNo);
			List<HandOrderInfo> infos = handOrderInfoMapper.selectByExample(infoExample);
			if(StringUtil.isListNull(infos)) {
				logger.error(batchNo + "下打单信息为空，终止执行！");
				info.setMessage(batchNo + "下打单信息为空，终止执行！");
				return info;
			}
			for (HandOrderInfo orderInfo : infos) {
				jmsTemplate.send(new TextMessageCreator(orderInfo.getOrderId()));
			}
			HandOrderBatchExample example = new HandOrderBatchExample();
			example.or().andBatchNoEqualTo(batchNo);
			HandOrderBatch record = new HandOrderBatch();
			record.setUpdateTime(new Date());
			record.setUpdateUser(userName);
			record.setBatchNo(batchNo);
			record.setProcessStatus(2);
			handOrderBatchMapper.updateByExampleSelective(record, example);
			info.setIsOk(Constant.OS_YES);
			info.setMessage("success");
		} catch (Exception e) {
			logger.error("执行手工打单 batchNo：" + batchNo + ";userName:" + userName + ";" + e.getMessage(), e);
			info.setMessage("执行手工打单 batchNo：" + batchNo + ";userName:" + userName + ";" + e.getMessage());
		}
		return info;
	}

	@Override
	public ReturnInfo<String> doCreateOrder(String orderId) {
		logger.info("执行手工转单 orderId：" + orderId);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		boolean result = true;
		HandOrderInfoExample example = new HandOrderInfoExample();
		example.or().andOrderIdEqualTo(orderId);
		List<HandOrderInfo> infos = handOrderInfoMapper.selectByExample(example);
		if (StringUtil.isListNull(infos)) {
			logger.error(orderId + "打单信息为空，终止执行！");
			info.setMessage(orderId + "打单信息为空，终止执行！");
			return info;
		}
		HandOrderInfo orderInfo = infos.get(0);
		String batchNo = orderInfo.getBatchNo();
		String userId = orderInfo.getUserId();
		try {
			String channelCode = orderInfo.getChannelCode();
			HandOrderGoodsExample goodsExample = new HandOrderGoodsExample();
			goodsExample.or().andOrderIdEqualTo(orderId);
			List<HandOrderGoods> goods = this.handOrderGoodsMapper.selectByExample(goodsExample);
			if (StringUtil.isListNull(goods)) {
				result = false;
				logger.error(orderId + "打单商品信息为空，终止执行！");
				info.setMessage(orderId + "打单商品信息为空，终止执行！");
				return info;
			}
			List<String> values = new ArrayList<String>();
			Map<String, Integer> map = new HashMap<String, Integer>();
			for (HandOrderGoods obj : goods) {
				values.add(obj.getCustomCode());
				map.put(obj.getCustomCode(), obj.getGoodsNumber());
			}
			BgProductRequest skuRequest = new BgProductRequest();
			skuRequest.setChannelCode(channelCode);
			ReturnInfo<ChannelInfo> siteInfo = null;
			// ReturnInfo<ChannelInfo> siteInfo = channelInfoService.findShopInfoByShopCode(channelCode);
			if (siteInfo == null || siteInfo.getIsOk() == Constant.OS_NO || siteInfo.getData() == null) {
				result = false;
				logger.error(orderId +"," + channelCode + "打单渠道信息为空，终止执行！");
				info.setMessage(orderId +"," + channelCode + "打单渠道信息为空，终止执行！");
				return info;
			}
			ChannelInfo channelInfo = siteInfo.getData();
			skuRequest.setSiteCode(channelInfo.getSiteCode());
			skuRequest.setProductSysCodeList(values);
			ResultData<ArrayList<BGGoodsInfo>> cartRes = bgProductService.getProductAllInfoForOrder(skuRequest);
			logger.info("cartRes:" + JSON.toJSONString(cartRes));
			if (cartRes.getIsOk() != Constant.OS_YES) {
				result = false;
				logger.error(orderId +"获取商品系统数据异常 ，终止执行！" + cartRes.getMsg());
				info.setMessage(orderId +"获取商品系统数据异常 ，终止执行！" + cartRes.getMsg());
				return info;
			}
			
			ReturnInfo<ShopUserInfo> shopUserri = shopUserService.getUserCreateOrderInfo(userId);
			if (shopUserri == null) {
				result = false;
				logger.error(orderId + "," + userId +"获取用户数据异常：返回为空，终止执行！");
				info.setMessage(orderId + "," + userId +"获取用户数据异常：返回为空，终止执行！");
				return info;
			}
			if (shopUserri.getIsOk() == Constant.OS_NO) {
				result = false;
				logger.error(orderId + "," + userId +"获取用户数据异常，终止执行！" + shopUserri.getMessage());
				info.setMessage(orderId + "," + userId +"获取用户数据异常，终止执行！" + shopUserri.getMessage());
				return info;
			}
			ShopUserInfo shopUserInfo = shopUserri.getData();
			if (shopUserInfo == null) {
				result = false;
				logger.error(orderId + "," + userId +"获取用户数据返回为空，终止执行！");
				info.setMessage(orderId + "," + userId +"获取用户数据返回为空，终止执行！");
				return info;
			}
			
			List<MasterGoods> goodsList = createMasterGoods(cartRes.getData(), map);
			List<MasterShip> shipList = createMasterShip(goodsList, shopUserInfo, orderInfo.getShippingAddress());
			MasterPay masterPay = createMasterPay(1, "alipay", (int)Constant.OI_PAY_STATUS_PAYED, 0D);
			List<MasterPay> payList = new ArrayList<MasterPay>();
			payList.add(masterPay);
			
			MasterOrder masterOrder = buildOrder(channelCode, shopUserInfo.getUserModel(), null, orderInfo.getCreateUser());
			masterOrder.setPayList(payList);
			masterOrder.setShipList(shipList);
			masterOrder.setSource(orderInfo.getSourceType() == null ? 5 : orderInfo.getSourceType());
			logger.info("打单调用数据 masterOrder :" + JSON.toJSONString(masterOrder));
			OrderCreateReturnInfo createReturnInfo = masterOrderInfoService.createOrder(masterOrder);
			logger.info("打单返回数据 createReturnInfo :" + JSON.toJSONString(createReturnInfo));
			// 打单成功 记录执行结果与Oms单号
			if (createReturnInfo.getIsOk() == Constant.OS_YES) {
				String masterOrderSn = createReturnInfo.getMasterOrderSn();
				HandOrderInfo updateInfo = new HandOrderInfo();
				updateInfo.setId(orderInfo.getId());
				updateInfo.setMasterOrderSn(masterOrderSn);
				updateInfo.setProcessMessage("打单成功,单号：" + masterOrderSn);
				updateInfo.setCreateTime(new Date());
				updateInfo.setIsOk(Constant.OS_YES);
				handOrderInfoMapper.updateByPrimaryKeySelective(updateInfo);
			} else {
				result = false;
				logger.error(orderId +"打单失败 errorMsg" + createReturnInfo.getMessage());
				info.setMessage(orderId +"打单失败 errorMsg" + createReturnInfo.getMessage());
				return info;
			}
		} catch (Exception e) {
			logger.error("执行手工转单  orderId：" + orderId + e.getMessage(), e);
			info.setMessage("执行手工转单  orderId：" + orderId + e.getMessage());
			result = false;
		} finally {
			if (!result) {
				HandOrderInfo updateInfo = new HandOrderInfo();
				updateInfo.setId(orderInfo.getId());
				updateInfo.setProcessMessage("打单失败,原因：" + info.getMessage());
				updateInfo.setCreateTime(new Date());
				updateInfo.setIsOk(2);
				handOrderInfoMapper.updateByPrimaryKeySelective(updateInfo);
			}
			// 判断该批次打单执行结果
			HandOrderInfoExample infoExample = new HandOrderInfoExample();
			infoExample.or().andBatchNoEqualTo(batchNo);
			// 总条数
			int count = handOrderInfoMapper.countByExample(infoExample);
			HandOrderInfoExample successExample = new HandOrderInfoExample();
			successExample.or().andBatchNoEqualTo(batchNo).andIsOkEqualTo(1);
			// 成功条数
			int successCount = handOrderInfoMapper.countByExample(successExample);
			
			HandOrderInfoExample failureExample = new HandOrderInfoExample();
			failureExample.or().andBatchNoEqualTo(batchNo).andIsOkEqualTo(2);
			// 失败条数
			int failureCount = handOrderInfoMapper.countByExample(failureExample);
			if (count == (successCount + failureCount)) {
				HandOrderBatch updateBatch = new HandOrderBatch();
				updateBatch.setBatchNo(batchNo);
				updateBatch.setUpdateTime(new Date());
				int createOrderStatus = 0;
				if (successCount == count) {
					// 全部成功
					createOrderStatus = 1;
				} else if (failureCount == count) {
					createOrderStatus = 3;
				} else {
					createOrderStatus = 2;
				}
				updateBatch.setCreateOrderStatus(createOrderStatus);
				HandOrderBatchExample updateBatchExample = new HandOrderBatchExample();
				updateBatchExample.or().andBatchNoEqualTo(batchNo);
				handOrderBatchMapper.updateByExampleSelective(updateBatch, updateBatchExample);
			}
		}
		return info;
	}
	
	private MasterOrder buildOrder(String channelCode, UserModel userModel, String expectedShipDate, String actionUser) {
		double moneyPaid = 0D;
		double totalPayable = 0D;
		double shippingTotalFee = 0D;
		double paySettlementPrice = 0D;
		MasterOrder masterOrder = new MasterOrder();
		masterOrder.setActionUser(actionUser);
		masterOrder.setAddTime(new Date());
		masterOrder.setOrderFrom(channelCode);
		masterOrder.setReferer("手工打单");
		masterOrder.setUserId(userModel.getShopCode());
		masterOrder.setTransType(1);
		masterOrder.setSource(3);
		masterOrder.setIsNow(0);
		masterOrder.setRegisterMobile(userModel.getMobile());
		masterOrder.setBonusId(null);
		masterOrder.setExpectedShipDate(null);
		masterOrder.setIsAdvance((short)0);

		double goodsAmount = 0;
		int goodsCount = 0;
		double discount = 0D;
		double goodsSettlementPrice = 0D;
		masterOrder.setGoodsSettlementPrice(goodsSettlementPrice + shippingTotalFee);
		masterOrder.setPaySettlementPrice(paySettlementPrice);
		masterOrder.setShippingTotalFee(shippingTotalFee);
		Double totalFee = goodsAmount - discount + shippingTotalFee;
		masterOrder.setTotalFee(totalFee);
//		Double totalPayable = totalFee - moneyPaid - surplus - bonus;
		masterOrder.setTotalPayable(totalPayable);
		masterOrder.setSurplus(0D);
		masterOrder.setMoneyPaid(moneyPaid);
		masterOrder.setBonus(0D);
		masterOrder.setPrName("");
		masterOrder.setGoodsAmount(goodsAmount);
		masterOrder.setGoodsCount(goodsCount);
		masterOrder.setDiscount(discount);
		masterOrder.setInvPayee("个人");
		masterOrder.setInvType("普通发票");
		masterOrder.setOrderType((byte)0);
		masterOrder.setOrderStatus(0);
		masterOrder.setPayStatus((short)2);
		masterOrder.setMobile(userModel.getMobile());
		masterOrder.setSurplus(0D);
		masterOrder.setOrderSettlementPrice(0D);
		masterOrder.setPoints(0D);
		masterOrder.setBvValue(0);
		masterOrder.setBaseBvValue(0);
		masterOrder.setIsCac(0);
		return masterOrder;
	}
	
	private List<MasterShip> createMasterShip(List<MasterGoods> goodsList, ShopUserInfo userInfo, String shippingAddress) {
		AddressModel addressModel = userInfo.getModel();
		DepotModel depotModel = userInfo.getDepotModel();
		UserModel userModel = userInfo.getUserModel();
		MasterShip masterShip = new MasterShip();
		masterShip.setShippingCode(null);
		masterShip.setCountry("1");
		masterShip.setProvince("310000");
		masterShip.setCity("310100");
		masterShip.setDistrict("310115");
		masterShip.setAddress(addressModel.getAddressName());
		masterShip.setMobile(userModel.getMobile());
		masterShip.setTel(null);
		masterShip.setConsignee(userModel.getUserName());
		masterShip.setEmail(null);
		masterShip.setBestTime(null);
		masterShip.setSignBuilding("");
		masterShip.setZipcode("");
		masterShip.setChargeType((byte)0);
		masterShip.setGoodsList(goodsList);
		masterShip.setDepotCode(depotModel.getDepotCode());
//		masterShip.setCardInfos(cardInfos);
		masterShip.setAreaCode(addressModel.getAddressCode());
		masterShip.setShippingAddress(shippingAddress);
		List<MasterShip> masterShips = new ArrayList<MasterShip>();
		masterShips.add(masterShip);
		return masterShips;
	}
	
	private List<MasterGoods> createMasterGoods(ArrayList<BGGoodsInfo> goodsInfos, Map<String, Integer> map) {
		List<MasterGoods> list = new ArrayList<MasterGoods>();
		for (BGGoodsInfo goodsInfo: goodsInfos) {
			String goodsSn = goodsInfo.getGoodsSn();
			Integer goodsNumber = map.get(goodsSn);
			MasterGoods masterGoods = new MasterGoods();
			masterGoods.setCustomCode(goodsInfo.getCustomCode());
			masterGoods.setGoodsName(goodsInfo.getGoodsName());
			masterGoods.setGoodsThumb(goodsInfo.getGoodsThumb());
			masterGoods.setGoodsPrice(0D);
			masterGoods.setMarketPrice(0D);
			masterGoods.setTransactionPrice(0D);
			masterGoods.setShareBonus(0D);
			masterGoods.setGoodsNumber(goodsNumber);
			masterGoods.setUseCard("");
			masterGoods.setExtensionCode("gift");
			masterGoods.setPromotionDesc("");
			masterGoods.setIntegralMoney(0D);
			masterGoods.setSellerUser("");
			masterGoods.setSalesMode(goodsInfo.getSalesMode());
			masterGoods.setSupplierCode(goodsInfo.getSupplierCode());
			masterGoods.setSizeName(goodsInfo.getSizeName());
			masterGoods.setColorName(goodsInfo.getColorName());
			masterGoods.setBarCode(goodsInfo.getIntlCode());
			masterGoods.setGoodsSn(goodsInfo.getGoodsSn());
			masterGoods.setBvValue(0);
			masterGoods.setBaseBvValue(0);
//			masterGoods.setExpectedShipDate(com.work.shop.oms.utils.TimeUtil.format2Date(goodsInfo.getExpectedShipDate()));
			masterGoods.setDepotCode(goodsInfo.getDepotCode());
			list.add(masterGoods);
		}
		return list;
	}

	private MasterPay createMasterPay(Integer payId, String payCode, Integer payStatus, Double payTotalFee) {
		MasterPay masterPay = new MasterPay();
		masterPay.setPayId(payId);
		masterPay.setPayCode(payCode);
		masterPay.setPayStatus(payStatus);
		masterPay.setPayTotalFee(payTotalFee);
		masterPay.setPayNote("");
		return masterPay;
	}
}
