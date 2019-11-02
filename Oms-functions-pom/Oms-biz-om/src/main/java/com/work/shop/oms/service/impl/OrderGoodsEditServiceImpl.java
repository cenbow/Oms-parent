package com.work.shop.oms.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.work.shop.pca.feign.BgProductService;
import com.work.shop.pca.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.cardAPI.api.CardCartSearchServiceApi;
import com.work.shop.cardAPI.bean.CardAPIBean;
import com.work.shop.cart.api.OrderCartApi;
import com.work.shop.cart.api.bean.GoodsForOrderServiceParaBean;
import com.work.shop.cart.api.bean.ResultOrderServiceData;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExample;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.ProductBarcodeList;
import com.work.shop.oms.bean.ProductBarcodeListExample;
import com.work.shop.oms.bean.ProductGoodsExample;
import com.work.shop.oms.bean.SystemConfig;
import com.work.shop.oms.bean.SystemConfigExample;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.CardPackageUpdateInfo;
import com.work.shop.oms.common.bean.ChannelGoodsVo;
import com.work.shop.oms.common.bean.OrderGoodsUpdateBean;
import com.work.shop.oms.common.bean.OrderInfoUpdateInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ShopUserInfo;
import com.work.shop.oms.dao.MasterOrderGoodsDetailMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.SystemConfigMapper;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.orderop.service.ShopUserService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.service.OrderGoodsEditService;
import com.work.shop.oms.stock.service.UniteStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.pca.common.ResultData;
import com.work.shop.stockcenter.client.dto.SkuStock;

/**
 * 商品编辑服务
 * @author QuYachu
 */
@Service
public class OrderGoodsEditServiceImpl implements OrderGoodsEditService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
//	@Resource
//	private ProductBarcodeListMapper productBarcodeListMapper;
	@Resource
	private MasterOrderGoodsDetailMapper masterOrderGoodsDetailMapper;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	//@Resource(name="cardCartSearchServiceApi")
	private CardCartSearchServiceApi cardCartSearchServiceApi;

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	@Resource(name="orderCommonService")
	private OrderCommonService orderCommonService;
	@Resource
	private RedisClient redisClient;
//	@Resource
//	private ProductBarcodeListDefinedMapper productBarcodeListDefinedMapper;
	@Resource
	private UniteStockService uniteStockService;
//	@Resource
//	private ProductGoodsMapper productGoodsMapper;
	@Resource(name="platformCartAPI")
	private OrderCartApi platformCartAPI;
	@Resource
	private SystemConfigMapper systemConfigMapper;

	@Resource
	private BgProductService bgProductService;

	@Resource
	private ChannelInfoService channelInfoService;
	@Resource
	private ShopUserService shopUserService;
	
	
	private static final String SHOP_CODE_PREFIX = "OS_SHOP_CODE_";
	
	
	public List<Map<String,String>> getOrderSnCombStore(String masterOrderSn){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try{
			if(!"".equals(masterOrderSn)&&masterOrderSn!=null){
				List<OrderDistribute> distributeList = new ArrayList<OrderDistribute>();
				OrderDistributeExample example = new OrderDistributeExample();
				OrderDistributeExample.Criteria criteria = example.or();
				criteria.andMasterOrderSnEqualTo(masterOrderSn);
				criteria.andOrderStatusNotEqualTo(new Byte("2"));
				distributeList = orderDistributeMapper.selectByExample(example);
				if(distributeList!=null){
					for(OrderDistribute bean : distributeList){
						Map<String,String> map = new HashMap<String,String>();
						map.put("n", bean.getOrderSn());
						map.put("v", bean.getOrderSn());
						list.add(map);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<Map<String, String>> getSupplierCodeCombStore(
			String masterOrderSn) {
		// TODO Auto-generated method stub
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try{
			if(!"".equals(masterOrderSn)&&masterOrderSn!=null){
				List<MasterOrderGoods> orderGoodsList = new ArrayList<MasterOrderGoods>();
				MasterOrderGoodsExample example = new MasterOrderGoodsExample();
				MasterOrderGoodsExample.Criteria criteria = example.createCriteria();
				criteria.andMasterOrderSnEqualTo(masterOrderSn);
				criteria.andIsDelEqualTo(0);
				orderGoodsList = masterOrderGoodsMapper.selectByExample(example);
				if(orderGoodsList!=null){
					Set<Map<String, String>> set = new HashSet<Map<String, String>>();
					//遍历去掉重复的供应商
					for(MasterOrderGoods bean : orderGoodsList){
						Map<String,String> map = new HashMap<String,String>();
						map.put("n", bean.getSupplierCode());
						map.put("v", bean.getSupplierCode());
						set.add(map);
					}
					list = new ArrayList<Map<String,String>>(set);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public List<ProductBarcodeList> getProductBarcodeListByGoodsSn(
			String goodsSn, String type) {
		List<ProductBarcodeList> list = new ArrayList<ProductBarcodeList>();
		try{
			if(!"".equals(goodsSn)&&goodsSn!=null){
				String siteCode = "NEWFORCE";
				String channelCode = "A0000001";
				BgProductRequest bgReq = new BgProductRequest();
				bgReq.setChannelCode(channelCode);
				bgReq.setProductSysCode(goodsSn);
				bgReq.setSiteCode(siteCode);
				ResultData<BgProductInfo> resultData = bgProductService.getProductDetailInfo(bgReq);
				if (resultData != null && resultData.getIsOk() == Constant.OS_YES && resultData.getData() != null) {
					// 1 size 2 color
					if ("2".equals(type)) {
						if (StringUtil.isListNotNull(resultData.getData().getSaleAttrList().getSaleAttr1List())) {
							for (BGProductSaleAttr1 saleAttr1 : resultData.getData().getSaleAttrList().getSaleAttr1List()) {
								ProductBarcodeList barcodeList = new ProductBarcodeList();
								barcodeList.setColorCode(saleAttr1.getSaleAttr1ValueCode());
								barcodeList.setColorName(saleAttr1.getSaleAttr1Value());
								list.add(barcodeList);
							}
						}
					} else {
						if (StringUtil.isListNotNull(resultData.getData().getSaleAttrList().getSaleAttr2List())) {
							for (BGProductSaleAttr2 saleAttr2 : resultData.getData().getSaleAttrList().getSaleAttr2List()) {
								ProductBarcodeList barcodeList = new ProductBarcodeList();
								barcodeList.setSizeCode(saleAttr2.getSaleAttr2ValueCode());
								barcodeList.setSizeName(saleAttr2.getSaleAttr2Value());
								list.add(barcodeList);
							}
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("根据商品编码" + goodsSn + "查询颜色尺码列表异常" + e.getMessage(), e);
		}
		return list;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map getOrderGoodsEdit(String masterOrderSn, String orderSn) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0:无数据  1：有数据
		String msg = "获取订单数商品数据失败！";
		List<MasterOrderGoodsDetail> masterOrderGoodsDetailList = new ArrayList<MasterOrderGoodsDetail>();//商品列表
		List<CardPackageUpdateInfo> bonusList = new ArrayList<CardPackageUpdateInfo>();//红包列表
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else{
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("masterOrderSn", masterOrderSn);
				if(StringUtil.isNotBlank(orderSn)){//交货单号
					paramMap.put("orderSn", orderSn);
				}
				//查询主单是否拆单  如果未拆单查询未删除的商品  如果拆单  查询未删除和未取消交货单的商品
				MasterOrderInfo info = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
				if(info.getSplitStatus()==2){
					paramMap.put("flag", "flag");
				}
				//获取主单商品信息
				masterOrderGoodsDetailList =  masterOrderGoodsDetailMapper.getMasterOrderGoodsDetail(paramMap);
				for(MasterOrderGoodsDetail bean : masterOrderGoodsDetailList){
					//给页面要用到的字段赋值
					bean.setInitGoodsNumber(bean.getGoodsNumber());
					bean.setInitIntegralMoney(bean.getIntegralMoney());
					bean.setCurrColorName(bean.getColorName());
					bean.setCurrSizeName(bean.getSizeName());
					bean.setShippingStatusName(getDepotShipStatusName(bean.getShippingStatus()));
					bean.setInitLackNum(Integer.parseInt(StringUtil.isNull(bean.getLackNum())?"0":bean.getLackNum()));
					//11位码
					String custumCode = bean.getCustomCode();
					String tempGoodsSn = bean.getGoodsSn();
					// BarcodeSysCode
					String siteCode = info.getChannelCode();
					BgProductRequest bgReq = new BgProductRequest();
					bgReq.setChannelCode(info.getOrderFrom());
//					bgReq.setBarcodeSysCode(custumCode);
					bgReq.setSiteCode(siteCode);
					bgReq.setProductSysCode(bean.getGoodsSn());
					List<String> values = new ArrayList<String>();
					values.add(custumCode);
//					bgReq.setBarcodeSysCodeList(values);
					bgReq.setProductSysCodeList(values);
					ResultData<ArrayList<BgProductChannelBarcode>> data = bgProductService.getSaleAttrByBarcodeSysCode(bgReq);
					if (data != null && data.getIsOk() == Constant.OS_YES && data.getData() != null) {
						BgProductChannelBarcode barcode = data.getData().get(0);
						bean.setSizeCode(barcode.getSaleAttr1ValueCode());//尺码
						bean.setSizeName(barcode.getSaleAttr1Value());//尺码名
						bean.setCurrSizeCode(barcode.getSaleAttr1ValueCode());
						bean.setCurrSizeName(barcode.getSaleAttr1Value());
						bean.setColorCode(barcode.getSaleAttr2ValueCode());//颜色码
						bean.setColorName(barcode.getSaleAttr2Value());//颜色名
						bean.setCurrColorCode(barcode.getSaleAttr2ValueCode());
						bean.setCurrColorName(barcode.getSaleAttr2Value());
					}
					//给货号下的颜色列表、尺码列表
					if(!"".equals(tempGoodsSn)){
						bgReq = new BgProductRequest();
						bgReq.setChannelCode(info.getOrderFrom());
						bgReq.setProductSysCode(tempGoodsSn);
						bgReq.setSiteCode(siteCode);
						ResultData<BgProductInfo> resultData = bgProductService.getProductDetailInfo(bgReq);
						List<ProductBarcodeList> colorChild = new ArrayList<ProductBarcodeList>();
						List<ProductBarcodeList> sizeChild = new ArrayList<ProductBarcodeList>();
						if (resultData != null && resultData.getIsOk() == Constant.OS_YES && resultData.getData() != null) {
							// 1 size 2 color
							if (StringUtil.isListNotNull(resultData.getData().getSaleAttrList().getSaleAttr1List())) {
								for (BGProductSaleAttr1 saleAttr1 : resultData.getData().getSaleAttrList().getSaleAttr1List()) {
									ProductBarcodeList barcodeList = new ProductBarcodeList();
									barcodeList.setColorCode(saleAttr1.getSaleAttr1ValueCode());
									barcodeList.setColorName(saleAttr1.getSaleAttr1Value());
									colorChild.add(barcodeList);
								}
							}
							if (StringUtil.isListNotNull(resultData.getData().getSaleAttrList().getSaleAttr2List())) {
								for (BGProductSaleAttr2 saleAttr2 : resultData.getData().getSaleAttrList().getSaleAttr2List()) {
									ProductBarcodeList barcodeList = new ProductBarcodeList();
									barcodeList.setSizeCode(saleAttr2.getSaleAttr2ValueCode());
									barcodeList.setSizeName(saleAttr2.getSaleAttr2Value());
									sizeChild.add(barcodeList);
								}
							}
						}
						bean.setColorChild(colorChild);
						bean.setSizeChild(sizeChild);
					}
				}
				//根据主单号获取主单红包列表
				MasterOrderInfoExample example = new MasterOrderInfoExample();
				example.or().andMasterOrderSnEqualTo(masterOrderSn);
				List<MasterOrderInfo> masterOrderInfoList = masterOrderInfoMapper.selectByExample(example);
				MasterOrderInfo masterOrderInfo = new MasterOrderInfo();
				if(masterOrderInfoList!=null){
					masterOrderInfo = masterOrderInfoList.get(0);
					String bonusId = masterOrderInfo.getBonusId();
					if(StringUtil.isNotBlank(bonusId)){
						String[] bonusIds = bonusId.split(",");
						for(String cardNo : bonusIds){
							try{
								CardAPIBean cardBean = cardCartSearchServiceApi.getCardBeanByNo(cardNo, 20);
								if(cardBean!=null){
									CardPackageUpdateInfo bean = new CardPackageUpdateInfo();
									bean.setCardLnName(cardBean.getCardLnName());
									bean.setCardNo(cardBean.getCardNo());
									bean.setCardMoney(cardBean.getCardMoney());
									bean.setEffectDate(cardBean.getEffectDateStr());
									bean.setExpireTime(cardBean.getExpireTimeStr());
									bean.setCardLimitMoney(cardBean.getCardLimitMoney());
									bean.setRangeCode(cardBean.getRangeCode());
									bean.setRangeName("0".equals(cardBean.getRangeCode())?"全场":"1".equals(cardBean.getRangeCode())?"品牌":"2".equals(cardBean.getRangeCode())?"单品":"");
									Date date = new Date();
									SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
									String nowdate = df.format(date);
									if (nowdate.compareTo(cardBean.getEffectDateStr()) >= 0 &&  nowdate.compareTo(cardBean.getExpireTimeStr()) <= 0) {
										bean.setSelected(true);
										bean.setInitSelected(true);
									} else {
										bean.setSelected(false);
										bean.setInitSelected(false);
									}
									bonusList.add(bean);
								}
							}catch(Exception e){
								logger.error("订单[" + masterOrderSn + "]编辑，获取订单红包数据异常" + e.getMessage(), e);
							}
						}
					}
				}
				String doERP = "";
				if(masterOrderInfo.getSplitStatus()==0){
					doERP = "0";// 未拆单
				}else{
					doERP = "1";// 已拆单
				}
				map.put("doERP", doERP);
				code = "1";
				msg = "获取订单数商品数据成功！";
			}
		}catch(Exception e){
			logger.error("订单[" + masterOrderSn + "]编辑，获取订单商品数据异常" + e.getMessage(), e);
		}
		map.put("bonusList",bonusList);
		map.put("masterOrderGoodsDetailList",masterOrderGoodsDetailList);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	private String getDepotShipStatusName(Byte shippingStatus){
		String returnValue = "";
		if(shippingStatus!=null){
			if(shippingStatus==0){
				returnValue = "未发货";
			}else if(shippingStatus==1){
				returnValue = "已发货";
			}else if(shippingStatus==2){
				returnValue = "已收货";
			}else if(shippingStatus==3){
				returnValue = "备货中";
			}else if(shippingStatus==6){
				returnValue = "门店收货";
			}else if(shippingStatus==10){
				returnValue = "快递取件";
			}else if(shippingStatus==11){
				returnValue = "运输中";
			}else if(shippingStatus==12){
				returnValue = "派件中";
			}else if(shippingStatus==13){
				returnValue = "客户签收";
			}else if(shippingStatus==14){
				returnValue = "客户拒签";
			}else if(shippingStatus==15){
				returnValue = "货物遗失";
			}else if(shippingStatus==16){
				returnValue = "货物损毁";
			}
		}
		return returnValue;
	}
	
	/**
	 * 判断是否已经将订单下发过ERP
	 * 
	 * @param orderInfo
	 * @param isrelive  复活标示 1为复活
	 * @return true 已经下发过erp false 没有下发过erp
	 */
	private static boolean doERP(MasterOrderInfo orderInfo, MasterOrderInfoExtend extend) {
		
		boolean isErp=false;
		if(orderInfo==null)
			 isErp=false;
		switch(orderInfo.getOrderType()){
			case 3 :
				isErp=false;
			case 4 :
				isErp=false;
			default :
				if(orderInfo.getReferer().trim().indexOf("淘宝品牌特卖")!=-1 
					&& orderInfo.getOrderType() == Constant.OI_ORDER_TYPE_NORMAL_ORDER){
					isErp=false;
				}
				isErp=true;
		}
		if(isErp){
			int isrelive = 0;
			if (extend != null && extend.getReviveStt() != null) {
				isrelive = extend.getReviveStt();
			}
			// 全流通订单 同步ERP
			if (!noERPWithO2O(orderInfo.getReferer()) && isrelive == 0) {
				isErp = true;
			} else if(orderInfo.getUpdateTime()!= null && isrelive == 0){ //已经下发过ERP且没有复活
				isErp = true;
			}else{
				isErp = false;
			}	
		}
		return isErp;
	}
	
	private static boolean noERPWithO2O(String referer) {
		if(org.apache.commons.lang.StringUtils.equalsIgnoreCase(referer, "仓库发货")
				|| org.apache.commons.lang.StringUtils.equalsIgnoreCase(referer, "门店发货")
				|| org.apache.commons.lang.StringUtils.equalsIgnoreCase(referer, "全流通"))
			return false;
		return true;
	}


	@Override
	public Map doSaveOrderGoodsEdit(AdminUser adminUser, String masterOrderSn,String orderSn,
			OrderInfoUpdateInfo orderInfoUpdateInfo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1失败
		String msg = "编辑订单商品信息保存失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else if(adminUser==null){
				msg = "登录失效！请重新登录！";
			}else{
				List<OrderGoodsUpdateBean> orderGoodsUpdateInfos = orderInfoUpdateInfo.getOrderGoodsUpdateInfos();
				if(orderGoodsUpdateInfos==null){
					msg = "编辑订单商品列表为空！";
				}else{
					//拼装接口需要参数
					for(OrderGoodsUpdateBean bean : orderGoodsUpdateInfos){
						bean.setDoubleGoodsPrice(bean.getGoodsPrice()==null?0.00D:bean.getGoodsPrice().doubleValue());
						bean.setDoubleSettlementPrice(bean.getSettlementPrice()==null?0.00D:bean.getSettlementPrice().doubleValue());
						bean.setDoubleTransactionPrice(bean.getTransactionPrice()==null?0.00D:bean.getTransactionPrice().doubleValue());
						bean.setDoubleShareBonus(bean.getShareBonus()==null?0.00D:bean.getShareBonus().doubleValue());
					}
					ReturnInfo res = new ReturnInfo();
					if(!"".equals(orderSn)&&orderSn!=null){//保存交货单商品
						res = orderCommonService.editGoodsByOrderSn(orderSn, orderInfoUpdateInfo, adminUser.getUserName());
					}else{//保存主单商品
						res = orderCommonService.editGoodsByMasterSn(masterOrderSn, orderInfoUpdateInfo, adminUser.getUserName());
					}
					if(res!=null){
						if(res.getIsOk()==1){
							code = "1";
							msg = "编辑订单商品信息保存成功！";
						}else{
							msg = res.getMessage();
						}
					}	
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}


	@Override
	public Map searchGoods(String goodsSn, String channelCode, String userId) {
		logger.info("查询商品信息 goodsSn:"+goodsSn+",channelCode:"+channelCode);
		Map map = new HashMap();
		String code = "0";//0失败，1失败
		String msg = "查询商品失败！";
		List<ChannelGoodsVo> list = new ArrayList<ChannelGoodsVo>();
		
		BGGoodsInfo bgGoodsInfo = new BGGoodsInfo();
		try{
			if(StringUtil.isTrimEmpty(goodsSn)){
				msg = "商品编码不能为空！";
			}else{
				String siteCode = channelInfoService.getChannelCode(channelCode);
				if (StringUtil.isNotEmpty(siteCode)) {
					BgProductRequest skuRequest = new BgProductRequest();
					skuRequest.setChannelCode(channelCode);
					skuRequest.setSiteCode(siteCode);
					
					List<String> values = new ArrayList<String>();
					values.add(goodsSn);
					skuRequest.setProductSysCodeList(values);
					ResultData<ArrayList<BGGoodsInfo>> cartRes = bgProductService.getProductAllInfoForOrder(skuRequest);
					logger.info("cartRes:" + JSON.toJSONString(cartRes.getData()));
					if (cartRes == null || cartRes.getIsOk() != Constant.OS_YES || StringUtil.isListNull(cartRes.getData())) {
						msg = "商品编码"+goodsSn+"商品表中商品不存在！";
					} else {
						code = "1";
						msg = "查询商品成功！";
						bgGoodsInfo = cartRes.getData().get(0);
						if (StringUtil.isTrimEmpty(bgGoodsInfo.getDepotCode())) {
							ReturnInfo<ShopUserInfo> tInfo  = shopUserService.getUserCreateOrderInfo(userId);
							if (tInfo==null || tInfo.getIsOk() == Constant.OS_NO) {
								logger.error("userId:" + userId + "查询用户接口：" + (tInfo==null ? "返回结果为空" : JSON.toJSONString(tInfo)));
							} else {
								ShopUserInfo userInfo = tInfo.getData();
								if (userInfo != null && userInfo.getDepotModel() != null) {
									bgGoodsInfo.setDepotCode(userInfo.getDepotModel().getDepotCode());
								} else {
									logger.error("userId:" + userId + "查询用户接口：用户信息不存在！");
								}
							}
						}
					}
					/*BGProductRequest bgReq = new BGProductRequest();
					bgReq.setChannelCode(channelCode);
					bgReq.setProductSysCode(queryGoodsSn);
					bgReq.setSiteCode(siteCode);
					ResultData<BGProductInfo> resultData = bgProductService.getProductDetailInfo(bgReq);
					if (resultData != null && resultData.getIsOk() == Constant.OS_YES) {
						BGProductInfo bgProductInfo = resultData.getData();
						if (bgProductInfo != null) {
							ChannelGoodsVo goodsVo = new ChannelGoodsVo();
							goodsVo.setChannelCode(channelCode);
							goodsVo.setGoodsSn(queryGoodsSn);
							goodsVo.setGoodsTitle(bgProductInfo.getProductName());
							goodsVo.setGoodsName(bgProductInfo.getProductName());
							goodsVo.setMarketPrice(bgProductInfo.getMarketPrice());
							goodsVo.setChannelPrice(bgProductInfo.getMarketPrice());
							goodsVo.setBrandCode(bgProductInfo.getBrandCode());
							goodsVo.setPlatformPrice(bgProductInfo.getMarketPrice());
							if (StringUtil.isNotEmpty(goodsSn) && goodsSn.length() > 6) {
								BGProductRequest skuRequest = new BGProductRequest();
								skuRequest.setChannelCode(channelCode);
								List<String> values = new ArrayList<String>();
								values.add(queryGoodsSn);
								skuRequest.setProductSysCodeList(values);
								skuRequest.setSiteCode(siteCode);
								ResultData<BGCartResponse> cartRes = bgProductService.getProductAllInfoForCart(skuRequest);
								if (cartRes != null && cartRes.getIsOk() == Constant.OS_YES && cartRes.getData() != null) {
									if (StringUtil.isListNotNull(cartRes.getData().getProductChannelBarcodeList())) {
										for (BGProductChannelBarcode channelBarcode : cartRes.getData().getProductChannelBarcodeList()) {
											if (channelBarcode.getBarcodeSysCode().equals(goodsSn)) {
												goodsVo.setCurrColorCode(channelBarcode.getSaleAttr1ValueCode());
												goodsVo.setCurrSizeCode(channelBarcode.getSaleAttr2ValueCode());
												break;
											}
										}
									}
								}
							}
							list.add(goodsVo);
						}
						code = "1";
						msg = "查询商品成功！";
					} else {
						msg = "商品编码"+goodsSn+"商品表中商品不存在！";
					}*/
				} else {
					msg = "商品编码"+goodsSn+"所属站点为空！";
				}
			}
		} catch (Exception e) {
			logger.error("查询商品 goodsSn:"+goodsSn+",channelCode:"+channelCode+"异常", e);
			msg = "查询商品 goodsSn:"+goodsSn+",channelCode:"+channelCode+"异常" + e.getMessage();
		}
//		map.put("list", list);
		map.put("bgGoodsInfo", bgGoodsInfo);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}


	@Override
	public Map getSizeListByColorCode(String goodSn, String colorCode) {
		// TODO Auto-generated method stub
		Map returnMap = new HashMap();
		String code = "0";//0无数据 1有数据
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try{
			if(!"".equals(goodSn)&&goodSn!=null){
				ProductBarcodeListExample example = new ProductBarcodeListExample();
				ProductBarcodeListExample.Criteria criteria = example.createCriteria();
				criteria.andGoodsSnEqualTo(goodSn);
				if(StringUtil.isNotNull(colorCode)){
					criteria.andColorCodeEqualTo(colorCode);
				}
				/*List<ProductBarcodeList> productBarcodeList = productBarcodeListMapper.selectByExample(example);
				for(ProductBarcodeList data:productBarcodeList){
					Map<String,String> map = new HashMap<String,String>();
					map.put("sizeCode", data.getSizeCode());
					map.put("sizeName", data.getSizeName());
					list.add(map);
				}*/
				
				String siteCode = "NEWFORCE";
				String channelCode = "A0000001";
				BgProductRequest bgReq = new BgProductRequest();
				bgReq.setChannelCode(channelCode);
				bgReq.setProductSysCode(goodSn);
				bgReq.setSiteCode(siteCode);
				ResultData<BgProductInfo> resultData = bgProductService.getProductDetailInfo(bgReq);
				code="1";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		returnMap.put("code", code);
		returnMap.put("list", list);
		return returnMap;
	}


	@Override
	public Map getColorListBySizeCode(String goodSn, String sizeCode) {
		// TODO Auto-generated method stub
		Map returnMap = new HashMap();
		String code = "0";//0无数据 1有数据
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try{
			if(!"".equals(goodSn)&&goodSn!=null){
				ProductBarcodeListExample example = new ProductBarcodeListExample();
				ProductBarcodeListExample.Criteria criteria = example.createCriteria();
				criteria.andGoodsSnEqualTo(goodSn);
				if(StringUtil.isNotNull(sizeCode)){
					criteria.andSizeCodeEqualTo(sizeCode);
				}
				/*List<ProductBarcodeList> productBarcodeList = productBarcodeListMapper.selectByExample(example);
				for(ProductBarcodeList data:productBarcodeList){
					Map<String,String> map = new HashMap<String,String>();
					map.put("colorCode", data.getColorCode());
					map.put("colorName", data.getColorName());
					list.add(map);
				}*/
				code="1";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		returnMap.put("code", code);
		returnMap.put("list", list);
		return returnMap;
	}


	@Override
	public Map getCustomStock(ProductBarcodeList barcodeList, String shopCode,
			int type) {
		Map map = new HashMap();
		String code = "0";//0：失败 ， 1： 成功
		String msg = "查询库存接口失败！";
		List<SkuStock> list = new ArrayList<SkuStock>();
		String siteCode = "NEWFORCE";
		try{
			if(barcodeList!=null||StringUtil.isNull(shopCode)){
				if(StringUtil.isNotEmpty(barcodeList.getCustumCode())){
					// 查询11位码库存
					SkuStock skuStock = uniteStockService.queryStockBySku(barcodeList.getCustumCode(), shopCode, null);
					list.add(skuStock);
					code = "1";
					msg = "查询库存接口成功！";
				}else if(StringUtil.isNotEmpty(barcodeList.getColorCode())
						&& StringUtil.isNotEmpty(barcodeList.getSizeCode())
						&& type == 1){
					//先查对应的customerCode
					String goodsSn = barcodeList.getGoodsSn();
					BgProductRequest bgReq = new BgProductRequest();
					bgReq.setSaleAttr1ValueCode(barcodeList.getColorCode());
					bgReq.setSaleAttr2ValueCode(barcodeList.getSizeCode());
					bgReq.setChannelCode(shopCode);
					bgReq.setProductSysCode(goodsSn);
					bgReq.setSiteCode(siteCode);
					ResultData<String>  resultData = bgProductService.getBarcodeSysCodeByProductSysCodeAndSaleAttrCode(bgReq);
					if (resultData != null && resultData.getIsOk() == Constant.OS_YES) {
						SkuStock skuStock = null;
						if(StringUtil.isNotBlank(resultData.getData())){
							skuStock = uniteStockService.queryStockBySku(resultData.getData(), shopCode, null);
						}
						if (skuStock == null) {
							skuStock = new SkuStock();
							skuStock.setSku(resultData.getData());
							skuStock.setAllStock(0);
						}
						list.add(skuStock);
						code = "1";
						msg = "查询库存接口成功！";
					}
				}
			}else{
				msg = "参数缺失！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("list", list);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}


	@Override
	public Map addOrderGoods(String masterOrderSn,
			String channelCode,MasterOrderGoodsDetail masterOrderGoodsDetail) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0：失败，1：成功
		String msg = masterOrderSn+"添加订单商品失败";
		List<MasterOrderGoodsDetail> list = new ArrayList<MasterOrderGoodsDetail>();
		try{
			//各种验证验证
			if (StringUtil.isEmpty(masterOrderSn)) {
				msg = masterOrderSn + "添加订单商品订单号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(masterOrderInfo==null){
				msg = masterOrderSn + "未找到相关订单信息！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			if(masterOrderGoodsDetail == null){
				msg = masterOrderSn+"添加订单商品为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			String customCode = masterOrderGoodsDetail.getCustomCode();
			if(StringUtil.isEmpty(customCode)){
				msg = masterOrderSn+"添加订单商品CustomCode为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			
			if(StringUtil.isEmpty(channelCode)){
				msg = masterOrderSn + "添加订单商品ChannelCode为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			
			int goodsNumber = masterOrderGoodsDetail.getGoodsNumber();
			if(goodsNumber<1){
				msg = masterOrderSn + "至少添加一件商品！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//这里extensionId只记录添加商品前最大的extensionId值
			String extensionId = masterOrderGoodsDetail.getExtensionId();
			if(extensionId==null||"".equals(extensionId)){
				msg = masterOrderSn + "extensionId为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			int maxExtensionId = Integer.valueOf(extensionId);
			for(int i=0;i<goodsNumber;i++){
				MasterOrderGoodsDetail newBean = new MasterOrderGoodsDetail();
				//将参数中的属性值copy到新对象中
				newBean.setMarketPrice(masterOrderGoodsDetail.getMarketPrice());
				newBean.setGoodsSn(masterOrderGoodsDetail.getGoodsSn());
				newBean.setGoodsName(masterOrderGoodsDetail.getGoodsName());
				newBean.setCustomCode(masterOrderGoodsDetail.getCustomCode());
				newBean.setCurrColorCode(masterOrderGoodsDetail.getCurrColorCode());
				newBean.setCurrSizeCode(masterOrderGoodsDetail.getCurrSizeCode());
				newBean.setCurrSizeName(masterOrderGoodsDetail.getCurrSizeName());
				newBean.setCurrColorName(masterOrderGoodsDetail.getCurrColorName());
				newBean.setGoodsNumber(new Short("1"));
				newBean.setInitGoodsNumber(new Short("1"));
				newBean.setGoodsPrice(masterOrderGoodsDetail.getGoodsPrice());
				newBean.setShareBonus(masterOrderGoodsDetail.getShareBonus());
				newBean.setTransactionPrice(masterOrderGoodsDetail.getTransactionPrice());
				newBean.setSettlementPrice(masterOrderGoodsDetail.getSettlementPrice());
				newBean.setExtensionCode(masterOrderGoodsDetail.getExtensionCode());
				newBean.setDepotCode(masterOrderGoodsDetail.getDepotCode());
				newBean.setExtensionId(String.valueOf(maxExtensionId));
				newBean.setDiscount(masterOrderGoodsDetail.getMarketPrice() - masterOrderGoodsDetail.getGoodsPrice().floatValue());
				newBean.setMasterOrderSn(masterOrderSn);
				if (StringUtil.isTrimEmpty(masterOrderGoodsDetail.getDepotCode())) {
					newBean.setDepotCode(Constant.DETAILS_DEPOT_CODE);
				} else {
					newBean.setDepotCode(masterOrderGoodsDetail.getDepotCode());
				}
				newBean.setGoodsThumb(masterOrderGoodsDetail.getGoodsThumb());
				newBean.setSap(masterOrderGoodsDetail.getSap());
				newBean.setBvValue(masterOrderGoodsDetail.getBvValue());
				newBean.setBaseBvValue(masterOrderGoodsDetail.getBaseBvValue());
//				newBean.setSupplierCode(productGoods != null ? productGoods.getSellerCode() : "");
				//补充判断颜色图
//				if(StringUtil.isBlank(newBean.getGoodsThumb())){
//					newBean.setGoodsThumb(productGoods != null ? productGoods.getGoodsThumb() : "");
//				}
				// 对新增商品信息补全操作
				BigDecimal value = new BigDecimal(0.00D);
				newBean.setIntegralMoney(value);
				newBean.setInitIntegralMoney(value);
				newBean.setColorCode(newBean.getCurrColorCode());
				newBean.setSizeCode(newBean.getCurrSizeCode());
				newBean.setUseCard("");
				newBean.setPromotionDesc("");
				newBean.setSubTotal(newBean.getSubTotal());
				newBean.setC2mItem("");
				newBean.setLackNum("0");
				newBean.setInitLackNum(0);
				newBean.setShippingStatus((byte)0);
				newBean.setShippingStatusName("未发货");
				//填充颜色尺码名
				/*String siteCode = masterOrderInfo.getChannelCode();
				String goodsSn = newBean.getGoodsSn();
				BGProductRequest custombgReq = new BGProductRequest();
				custombgReq.setChannelCode(channelCode);
				custombgReq.setSiteCode(siteCode);
				custombgReq.setProductSysCode(goodsSn);
				List<String> values = new ArrayList<String>();
				values.add(masterOrderGoodsDetail.getCustomCode());
				custombgReq.setProductSysCodeList(values);
				ResultData<ArrayList<BGProductChannelBarcode>> data = bgProductService.getSaleAttrByBarcodeSysCode(custombgReq);
				if (data != null && data.getIsOk() == Constant.OS_YES && data.getData() != null) {
					BGProductChannelBarcode barcode = data.getData().get(0);
					newBean.setSizeCode(barcode.getSaleAttr2ValueCode());//尺码
					newBean.setSizeName(barcode.getSaleAttr2Value());//尺码名
					newBean.setCurrSizeCode(barcode.getSaleAttr2ValueCode());
					newBean.setCurrSizeName(barcode.getSaleAttr2Value());
					newBean.setColorCode(barcode.getSaleAttr1ValueCode());//颜色码
					newBean.setColorName(barcode.getSaleAttr1Value());//颜色名
					newBean.setCurrColorCode(barcode.getSaleAttr1ValueCode());
					newBean.setCurrColorName(barcode.getSaleAttr1Value());
				}*/
				//填充颜色、尺码列表
				/*if(!"".equals(goodsSn)){
					BGProductRequest bgReq = new BGProductRequest();
					bgReq.setChannelCode(channelCode);
					bgReq.setProductSysCode(goodsSn);
					bgReq.setSiteCode(siteCode);
					ResultData<BGProductInfo> resultData = bgProductService.getProductDetailInfo(bgReq);
					List<ProductBarcodeList> colorChild = new ArrayList<ProductBarcodeList>();
					List<ProductBarcodeList> sizeChild = new ArrayList<ProductBarcodeList>();
					if (resultData != null && resultData.getIsOk() == Constant.OS_YES && resultData.getData() != null) {
						// 1 size 2 color
						if (StringUtil.isListNotNull(resultData.getData().getSaleAttrList().getSaleAttr1List())) {
							for (BGProductSaleAttr1 saleAttr1 : resultData.getData().getSaleAttrList().getSaleAttr1List()) {
								ProductBarcodeList barcodeList = new ProductBarcodeList();
								barcodeList.setColorCode(saleAttr1.getSaleAttr1ValueCode());
								barcodeList.setColorName(saleAttr1.getSaleAttr1Value());
								colorChild.add(barcodeList);
							}
						}
						if (StringUtil.isListNotNull(resultData.getData().getSaleAttrList().getSaleAttr2List())) {
							for (BGProductSaleAttr2 saleAttr2 : resultData.getData().getSaleAttrList().getSaleAttr2List()) {
								ProductBarcodeList barcodeList = new ProductBarcodeList();
								barcodeList.setSizeCode(saleAttr2.getSaleAttr2ValueCode());
								barcodeList.setSizeName(saleAttr2.getSaleAttr2Value());
								sizeChild.add(barcodeList);
							}
						}
					}
					newBean.setColorChild(colorChild);
					newBean.setSizeChild(sizeChild);
				}*/
				list.add(newBean);
				maxExtensionId = maxExtensionId+1;
			}
			code = "1";
			msg = "添加订单商品成功！";
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("list", list);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	/**
	 * 获取该商品中所以的颜色码、尺码信息 以及当前所选择的的颜色码、尺码信息
	 * 已选颜色码、尺码使用当前选择的，没选择的情况，使用颜色码尺码列表第一项
	 * @param goodsVO
	 */


	@Override
	public Map recalculate(OrderInfoUpdateInfo infoUpdateInfo, String[] bonusIds) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败  1成功
		String msg = "红包试算失败！";
		Double goodsBonusTotal = new Double(0.0000);
		List<OrderGoodsUpdateBean> orderGoodsList = new ArrayList<OrderGoodsUpdateBean>();
		try{
			//非空校验
			if(infoUpdateInfo == null){
				msg = "参数为空!";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			
			if(!StringUtil.isListNotNull(infoUpdateInfo.getOrderGoodsUpdateInfos())){
				msg = "订单商品参数列表为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			
			if(bonusIds==null||!(bonusIds.length>0)){
				msg = "订单红包列表为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}

			orderGoodsList = infoUpdateInfo.getOrderGoodsUpdateInfos();
			//去掉删除的商品  即GoodsNumber<0
			for (int i=orderGoodsList.size()-1;i>0;i--) {
				OrderGoodsUpdateBean bean = orderGoodsList.get(i);
				if (bean.getGoodsNumber()<0||(!"common".equals(bean.getExtensionCode()))) { 
					orderGoodsList.remove(i);
				}
			}
			/*
			 * 准备接口需要的入参：红包列表+封装有customCode、brandCode、商品成交价的对象列表
			 */
			//获取参数红包列表
			List<String> cardBonusList = Arrays.asList(bonusIds);
			//根据商品列表中的goodsSn获取brandCode
			Set<String> goodsSnSet = new HashSet<String>();//为了去掉重复的goodsSn
			for(OrderGoodsUpdateBean bean : orderGoodsList){
				if(!"".equals(bean.getGoodsSn())&&bean.getGoodsSn()!=null){
					goodsSnSet.add(bean.getGoodsSn());
				}
			}
			if(goodsSnSet==null){
				msg = "商品码为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//转成查询brandCode的入参
			List<String> goodsSnList = new ArrayList<String>(goodsSnSet);
			ProductGoodsExample example = new ProductGoodsExample();
			example.or().andGoodsSnIn(goodsSnList);
//			List<ProductGoods> productGoodsList = productGoodsMapper.selectByExample(example);
			//存入<goodsSn,brandCode>形成的键值对
			Map<String,String> productGoodsMap = new HashMap<String,String>();
			/*for(ProductGoods bean : productGoodsList){
				productGoodsMap.put(bean.getGoodsSn(), bean.getBrandCode());
			}*/
			//拼装红包接口的商品入参
			List<GoodsForOrderServiceParaBean> goodsCardShareList = new ArrayList<GoodsForOrderServiceParaBean>();
			for(OrderGoodsUpdateBean bean : orderGoodsList){
				GoodsForOrderServiceParaBean card = new GoodsForOrderServiceParaBean();
				card.setGoodsSn(bean.getCustomCode());//注意：这里接口认为的goodsSn实际为我们的customCode
				String brandCode = productGoodsMap.get(bean.getGoodsSn())==null?"":productGoodsMap.get(bean.getGoodsSn());
				card.setBrandCode(brandCode);
				card.setGoodsTransactionPrice(bean.getTransactionPrice().doubleValue()*bean.getGoodsNumber());
				goodsCardShareList.add(card);
			}
			//调用红包接口
			ResultOrderServiceData orderServiceData = platformCartAPI.usePackageForOrderService(cardBonusList, goodsCardShareList);
			if(orderServiceData!=null){
				if(orderServiceData.getCode()==0){//接口返回成功
					List<GoodsForOrderServiceParaBean> trialResult = orderServiceData.getGoodsList();
					if(StringUtil.isListNotNull(trialResult)){  
						//接口返回结果拼装成<customerCode|transactionPrice,红包对象>键值对  注意：接口认为的goodsSn为我们的customCode
						Map<String, Object> resultMap = new HashMap<String, Object>();
						for(GoodsForOrderServiceParaBean bean : trialResult){
							String key = bean.getGoodsSn()+"|"+new BigDecimal(bean.getGoodsTransactionPrice()).setScale(4, BigDecimal.ROUND_HALF_UP);
							resultMap.put(key, bean);
						}
						//回写红包信息
						for(OrderGoodsUpdateBean bean : orderGoodsList){
							String customCode = bean.getCustomCode();
							double transactionPrice = bean.getTransactionPrice().doubleValue();
							String key = customCode+"|"+new BigDecimal(transactionPrice).setScale(4, BigDecimal.ROUND_HALF_UP);
							GoodsForOrderServiceParaBean serviceParaBean = (GoodsForOrderServiceParaBean) resultMap.get(key);
							if(serviceParaBean!=null){
								goodsBonusTotal = goodsBonusTotal + serviceParaBean.getSharePrice();
								Double shareBonus = serviceParaBean.getSharePrice()/bean.getGoodsNumber();
								bean.setShareBonus(new BigDecimal(shareBonus).setScale(4, BigDecimal.ROUND_HALF_UP));
								bean.setSettlementPrice(new BigDecimal(bean.getTransactionPrice().doubleValue()
										- shareBonus - bean.getIntegralMoney()).setScale(4, BigDecimal.ROUND_HALF_UP));
							}else{
								bean.setShareBonus(new BigDecimal(0.00));
							}
						}
						code = "1";
						msg = "红包试算成功！";
					}else{
						msg = "接口返回的可用红包信息为空！";
					}
				}else{//接口返回失败
					msg = orderServiceData.getMessage();
				}
			}else{//调用接口失败
				msg = "调用红包分摊接口服务失败！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("goodsBonusTotal", new BigDecimal(goodsBonusTotal).setScale(4, BigDecimal.ROUND_HALF_UP));
		map.put("orderGoodsList", orderGoodsList);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}


	@Override
	public Map getIsVerifyStock() {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "1";//0：不验证，1：验证
		try{
			SystemConfigExample example = new SystemConfigExample();
			example.or().andCodeEqualTo("is_verify_stock");
			List<SystemConfig> list = systemConfigMapper.selectByExampleWithBLOBs(example);
			if(list!=null&&list.size()>0){
				SystemConfig bean = list.get(0);
				code = bean.getValue();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		return map;
	}
	
	
	
}
