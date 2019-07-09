package com.work.shop.oms.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.cardAPI.api.CardCartSearchServiceApi;
import com.work.shop.cardAPI.bean.CardAPIBean;
import com.work.shop.oms.api.param.bean.OrderInfoSearchExample;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.APIxml;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExample;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderPayTypeDetail;
import com.work.shop.oms.bean.MasterOrderQuestionDetail;
import com.work.shop.oms.bean.OrderDepotShipDetail;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderQuestionNewDetail;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnExample;
import com.work.shop.oms.bean.OrderReturnGoods;
import com.work.shop.oms.bean.ProductBarcodeList;
import com.work.shop.oms.bean.ProductBarcodeListExample;
import com.work.shop.oms.bean.SendMessageToolRecord;
import com.work.shop.oms.bean.ShippingOrg;
import com.work.shop.oms.bean.ShortageQuestion;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.bean.SystemShippingExample;
import com.work.shop.oms.bean.SystemShippingWithBLOBs;
import com.work.shop.oms.bean.TrPriceInfo;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.bean.bgchanneldb.CsChannelInfo;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.ButtonMenu;
import com.work.shop.oms.common.bean.Common;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.MasterOrderDetail;
import com.work.shop.oms.common.bean.OrderOtherModifyInfo;
import com.work.shop.oms.common.bean.OrderQuestionSearchResultVO;
import com.work.shop.oms.common.bean.OrderQuestionSearchVO;
import com.work.shop.oms.common.bean.PayType;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.DistributeOrderInfoVO;
import com.work.shop.oms.common.utils.DistributeOrderStatusUtils;
import com.work.shop.oms.common.utils.MasterOrderInfoVO;
import com.work.shop.oms.common.utils.OrderStatusUtils;
import com.work.shop.oms.dao.MasterOrderActionHistoryMapper;
import com.work.shop.oms.dao.MasterOrderActionMapper;
import com.work.shop.oms.dao.MasterOrderAddressDetailMapper;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsDetailMapper;
import com.work.shop.oms.dao.MasterOrderInfoDetailMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayTypeDetailMapper;
import com.work.shop.oms.dao.MasterOrderQuestionDetailMapper;
import com.work.shop.oms.dao.OrderDepotShipDetailMapper;
import com.work.shop.oms.dao.OrderDistributeDetailMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderQuestionNewDetailMapper;
import com.work.shop.oms.dao.OrderReturnGoodsDetailMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.SendMessageToolRecordMapper;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.dao.define.OrderInfoSearchMapper;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.stock.service.UniteStockService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.oms.vo.DeliveryInfoParam;
import com.work.shop.oms.vo.ErpDepotInfo;
import com.work.shop.oms.vo.ErpStatus;
import com.work.shop.oms.vo.ExpressInfo;
import com.work.shop.oms.vo.ExpressStatus;
import com.work.shop.oms.vo.SystemShippingVo;
import com.work.shop.sms.bean.Message;
import com.work.shop.sms.bean.State;
import com.work.shop.sms.bean.User;
import com.work.shop.sms.send.api.SMSService;
import com.work.shop.stockcenter.client.dto.SkuStock;
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private MasterOrderInfoDetailMapper masterOrderInfoDetailMapper;
	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;
	@Resource
	private MasterOrderAddressDetailMapper masterOrderAddressDetailMapper;
	@Resource
	private MasterOrderGoodsDetailMapper masterOrderGoodsDetailMapper;
//	@Resource
//	private ProductBarcodeListMapper productBarcodeListMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private OrderDistributeDetailMapper orderDistributeDetailMapper;
	@Resource
	private OrderDepotShipDetailMapper orderDepotShipDetailMapper;
	@Resource
	private MasterOrderPayTypeDetailMapper masterOrderPayTypeDetailMapper;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	private MasterOrderActionMapper masterOrderActionMapper;
	@Resource
	private MasterOrderActionHistoryMapper masterOrderActionHistoryMapper;
	@Resource
	private SystemShippingMapper systemShippingMapper;

	//@Resource(name="cardCartSearchServiceApi")
	private CardCartSearchServiceApi cardCartSearchServiceApi;

	@Resource(name="orderCommonService")
	private OrderCommonService orderCommonService;
	@Resource
	private UniteStockService uniteStockService;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private OrderQuestionNewDetailMapper orderQuestionNewDetailMapper;
	@Resource
	private MasterOrderQuestionDetailMapper masterOrderQuestionDetailMapper;
	@Resource
	private OrderReturnGoodsDetailMapper orderReturnGoodsDetailMapper;
	@Resource
	private OrderReturnMapper orderReturnMapper;
	//@Resource(name = "sMSService")
	private SMSService sMSService;
	@Resource
	private SendMessageToolRecordMapper sendMessageToolRecordMapper;
	@Resource
	private ChannelInfoService channelInfoService;
	
	private static final String QUERY_ORDER_EXPRESS_URL = ConfigCenter.getProperty("order_express_url");
	private static final String SHIPPING_URL = ConfigCenter.getProperty("shipping_org");
	
	@Override
	public MasterOrderInfoVO getOrderDetail(AdminUser adminUser, String masterOrderSn, Integer isHistory) {
		// TODO Auto-generated method stub
		MasterOrderInfoVO masterOrderInfoVO = new MasterOrderInfoVO();
		long startTime=System.currentTimeMillis();   //获取开始时间
		try{
			//主单下的退单总金额
			double returnSettleMoney = 0.00D;
			//封装参数
			Map paramMap = new HashMap();
			paramMap.put("masterOrderSn", masterOrderSn);
			paramMap.put("isHistory", isHistory);
			//查询主单信息（主单表、扩展表、地址信息表）
			MasterOrderDetail masterOrderInfo = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
			if(masterOrderInfo==null){
				return masterOrderInfoVO;
			}
			//主单问题单原因类型描述
			if (masterOrderInfo.getQuestionStatus() == Constant.OI_QUESTION_STATUS_QUESTION) {
				Map<String,Object> masterQuesMap = new HashMap<String, Object>();
				masterQuesMap.put("masterOrderSn", masterOrderSn);
				List<MasterOrderQuestionDetail> masterQuestions = masterOrderQuestionDetailMapper.getMasterOrderQuestionDetail(masterQuesMap);
				if(masterQuestions!=null){
					String[] arr = new String[masterQuestions.size()];
					for (int i = 0; i < masterQuestions.size() ; i++) {
						arr[i] = masterQuestions.get(i).getQuestionDesc();
					}
					masterOrderInfo.setLogqDesc(JSON.toJSONString(arr));
				}
			}
			//主单按钮权限
			OrderStatusUtils orderStatusUtils = new OrderStatusUtils(masterOrderInfo,masterOrderSn,adminUser,Constant.SESSION_SUP_KEY);
			//主单红包名称
			String bonusId = masterOrderInfo.getBonusId();
			List<String> bonusNoList = new ArrayList<String>();
			StringBuffer bounsName = new StringBuffer("");
			if(StringUtil.isNotBlank(bonusId)){
				String[] bonusIds = bonusId.split(",");
				for(String cardNo : bonusIds){
					if(StringUtil.isNotBlank(cardNo)){
						bonusNoList.add(cardNo);
					}
					
				}
				if(bonusNoList!=null&&bonusNoList.size()>0){
					long startGetBounsTime=System.currentTimeMillis();
					try{
						List<CardAPIBean> bounsList = cardCartSearchServiceApi.getCardBeanListByNo(bonusNoList,20);
						if(bounsList!=null&&bounsList.size()>0){
							for(CardAPIBean bean : bounsList){
								bounsName.append("名称:"+ bean.getCardLnName()+ ",卡号:"+ bean.getCardNo()
										+ ",面值:"+ bean.getCardMoney() + ";");
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					long endGetBounsTime=System.currentTimeMillis();
					logger.info("获取红包列表详情耗时： "+(endGetBounsTime-startGetBounsTime)+"ms");
				}
			}
			masterOrderInfo.setBonusName(bounsName.toString());
			//如果是换货单  获取换货单单号
			/*if(!"".equals(masterOrderInfo.getRelatingOriginalSn())&&masterOrderInfo.getRelatingOriginalSn()!=null){
				try{
					String relatingOriginalSn = masterOrderInfo.getRelatingOriginalSn();
					Map smallMap = new HashMap();
					smallMap.put("orderSn", relatingOriginalSn);
					smallMap.put("isHistory", isHistory);
					List<OrderDistribute> tempOrderDistributeList = orderDistributeDetailMapper.getOrderDistributeList(smallMap);
					if(tempOrderDistributeList!=null&&tempOrderDistributeList.size()>0){
						OrderDistribute temp = tempOrderDistributeList.get(0);
						String masOrdSnAndOrdSn = temp.getMasterOrderSn()+"&orderSn="+relatingOriginalSn;
						masterOrderInfo.setRelatingOriginalSn(masOrdSnAndOrdSn);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}*/
			
			//获取主单商品信息+发货仓信息+发货单信息(根据order_sn、extension_code、custom_code、transaction_price、settlement_price、depot_code字段合并)
			List<MasterOrderGoodsDetail> mergedMasOrdGoodsDetailList =  masterOrderGoodsDetailMapper.getMergedMasOrdGoodsDetail(paramMap);
			//占用库存按钮状态标志  通过遍历商品列表已发送数量和购买数量影响
			boolean occRealMenu = false;
			//填充商品列表其他数据（如打折券、货号、条形码、颜色、尺码名等），另外计算所有退货金额等
			if(mergedMasOrdGoodsDetailList!=null&&mergedMasOrdGoodsDetailList.size()>0){
				String isAllOccupy = "";//是全占用库存标志
				//待查barcode信息参数列表、待查打折券信息参数列表
				List<String> skus = new ArrayList<String>();
				List<String> cardNoList = new ArrayList<String>();
				for(MasterOrderGoodsDetail bean : mergedMasOrdGoodsDetailList){
					if(StringUtil.isNotNull(bean.getCustomCode())){
						skus.add(bean.getCustomCode());
					}
					if(StringUtil.isNotNull(bean.getUseCard())){
						String useCard = bean.getUseCard();
						String[] cards = useCard.split(":");
						for(String cardNo : cards){
							if(StringUtil.isNotNull(cardNo)){
								cardNoList.add(cardNo);
							}
						}
					}
					//是否占用库存
					if(bean.getSendNumber().intValue()==bean.getGoodsNumber().intValue()){
						isAllOccupy+="1";//占用
					}else{
						isAllOccupy+="2";//不占用
					}
				}
				if(!"".equals(isAllOccupy)&&isAllOccupy.indexOf("2")==-1){
					occRealMenu = true;//库存全占  按钮为释放按钮
				}
				
				//批量查询barcode信息列表
				Map<String,ProductBarcodeList> proBarcodeMap = new HashMap<String,ProductBarcodeList>();
				/*if(skus!=null&&skus.size()>0){
					ProductBarcodeListExample proExample = new ProductBarcodeListExample();
					proExample.or().andCustumCodeIn(skus);
					List<ProductBarcodeList> proBarcodeList = productBarcodeListMapper.selectByExample(proExample);
					if(proBarcodeList!=null && proBarcodeList.size()>0){
						for(ProductBarcodeList barCodeBean : proBarcodeList){
							proBarcodeMap.put(barCodeBean.getCustumCode(), barCodeBean);
						}
					}
				}*/
				//批量查询打折券信息
				Map<String,CardAPIBean> cardMap = new HashMap<String,CardAPIBean>();
				if(cardNoList!=null&&cardNoList.size()>0){
					long startGetcardTime=System.currentTimeMillis();
					try{
						List<CardAPIBean> cardList = cardCartSearchServiceApi.getCardBeanListByNo(cardNoList,30);
						if(cardList!=null && cardList.size()>0){
							for(CardAPIBean bean : cardList){
								if(bean!=null){
									cardMap.put(bean.getCardNo(), bean);
								}
							}
						}	
					}catch(Exception e){
						e.printStackTrace();
					}
					long endGetcardTime=System.currentTimeMillis();
					logger.info("获取打折券列表详情耗时： "+(endGetcardTime-startGetcardTime)+"ms");
				}
				//查询退单列表  用于计算退单金额和填充已退待退数量信息
				List<OrderReturnGoods> returnList = orderReturnGoodsDetailMapper.getReturnNumberByMasOrdSn(paramMap);
				//计算退单总金额
				if(returnList!=null&&returnList.size()>0){
					for(OrderReturnGoods returnBean : returnList){
						returnSettleMoney = returnSettleMoney + returnBean.getGoodsReturnNumber()*returnBean.getSettlementPrice().doubleValue();
					}
				}
				//填充商品信息
				for(MasterOrderGoodsDetail bean : mergedMasOrdGoodsDetailList){
					//给已退和待退数量赋值
					if(returnList!=null&&returnList.size()>0){
						for(OrderReturnGoods returnBean : returnList){
							if(bean.getCustomCode().equals(returnBean.getCustomCode())
									&&bean.getExtensionCode().equals(returnBean.getExtensionCode())
									&&bean.getDepotCode().equals(returnBean.getOsDepotCode())){
								if(returnBean.getCheckinStatus()==0){
									bean.setReturnRemainNum(returnBean.getGoodsReturnNumber().toString());
								}else if(returnBean.getCheckinStatus()==1){
									bean.setReturnNum(returnBean.getGoodsReturnNumber().toString());
								}
							}
						}
					}
					//给配送状态名称赋值
					bean.setShippingStatusName(getDepotShipStatusName(bean.getShippingStatus()));
					//给货号、条形码、颜色、尺码名赋值
					String custumCode = bean.getCustomCode();
					if(!"".equals(custumCode)&&custumCode!=null){
						ProductBarcodeList proBarCode = proBarcodeMap.get(custumCode);
						if(proBarCode!=null){
							//给货号、条形码赋值
							bean.setGoodsSn(proBarCode.getGoodsSn());//货号
							bean.setBarcode(proBarCode.getBarcode());//条形码
							//判断颜色名和尺码名是否为空，为空就拿11位码去查询颜色名和尺码名并给bean赋值
							String goodsColorName = bean.getGoodsSizeName();
							String goodsSizeName = bean.getSizeName();
							if("".equals(goodsColorName)||goodsColorName==null||"".equals(goodsSizeName)||goodsSizeName==null){
								bean.setGoodsColorName(proBarCode.getColorName());
								bean.setGoodsSizeName(proBarCode.getSizeName());
							}
						}
					}
					//给打折券赋值
					List<Map> couponList = new ArrayList<Map>();
					String useCard = bean.getUseCard();
					StringBuffer cardsBuffer = new StringBuffer("");
					if(!"".equals(useCard)&&useCard!=null){
						String[] cards = useCard.split(":");
						for(String cardNo : cards){//遍历打折券卡号  获取打折券列表
							CardAPIBean couponBean = cardMap.get(cardNo);
							if(couponBean!=null){
								Map couponMap = new HashMap();
								couponMap.put("cardNo", couponBean.getCardNo()==null?"":couponBean.getCardNo());//打折券卡号
								couponMap.put("cardLn", couponBean.getCardLn()==null?"":couponBean.getCardLn());//批次号
								couponMap.put("cardMoney",couponBean.getCardMoney());//折扣（打折券）
								couponMap.put("userId", couponBean.getUserId()==null?"":couponBean.getUserId());//绑定用户id
								couponMap.put("status", couponBean.getStatus());//状态(-1未启用，0已领用，1已经激活，2卡已经充值，3卡作废，4已使用)
								couponMap.put("effectDateStr", couponBean.getEffectDateStr()==null?"":couponBean.getEffectDateStr());
								couponMap.put("expireTimeStr", couponBean.getExpireTimeStr()==null?"":couponBean.getExpireTimeStr());
								couponList.add(couponMap);
							}
						}
					}
					bean.setCouponList(couponList);
				}
			}
			
			//主单按钮
			ReturnInfo returnInfo = orderStatusUtils.checkConditionOfExecution(masterOrderInfo, masterOrderSn, "库存");
			if (returnInfo != null && returnInfo.getIsOk() == Constant.OS_NO) {
				orderStatusUtils.setOccRealMenu(new ButtonMenu("释放库存", "release"));
			} else if (occRealMenu) {
				orderStatusUtils.setOccRealMenu(new ButtonMenu("释放库存", "release"));
			} else {
				orderStatusUtils.setOccRealMenu(new ButtonMenu("占用库存", "occupy"));
			}
			//移入近期状态
			if (0 != isHistory) {
				orderStatusUtils.setRecent(1);
			}
			
			//获取配送信息
			List<OrderDepotShipDetail> orderDepotShipDetailList = orderDepotShipDetailMapper.getOrderDepotShipDetail(paramMap);
			//获取子单/交货单列表
			List<OrderDistribute> orderDistributeList = orderDistributeDetailMapper.getOrderDistributeList(paramMap);
			//一个交货单和这个交货单下的商品列表和这个交货单下的配送信息列表、以及这个交货单下的按钮状态存放到一起
			List<DistributeOrderInfoVO> sonOrderList = new ArrayList<DistributeOrderInfoVO>();
			for(OrderDistribute bean : orderDistributeList){//遍历交货单
				DistributeOrderInfoVO sonOrderBean = new DistributeOrderInfoVO();
				//存放交货单信息
				sonOrderBean.setSonOrder(bean);
				//交货单的按钮状态
				String sonOrderSn = bean.getOrderSn();
				DistributeOrderStatusUtils distributeOrderStatusUtils = new DistributeOrderStatusUtils(masterOrderInfo,bean,sonOrderSn,adminUser);
				sonOrderBean.setDistributeOrderStatusUtils(distributeOrderStatusUtils);
				if (masterOrderInfo.getOrderStatus().intValue() == Constant.OI_ORDER_STATUS_CANCLED) {
					//存放交货单对应的换单
					List<String> relatingExOrRetOrdSns = new ArrayList<String>();
					MasterOrderInfoExample relatingExchangeExample = new MasterOrderInfoExample();
					MasterOrderInfoExample.Criteria exchangeCriteria = relatingExchangeExample.or();
					exchangeCriteria.andMasterOrderSnEqualTo(masterOrderSn);
					exchangeCriteria.andOrderTypeEqualTo(2);
					exchangeCriteria.andOrderStatusNotEqualTo(new Byte("2"));
					List<MasterOrderInfo> relatingExchangeList = masterOrderInfoMapper.selectByExample(relatingExchangeExample);
					if(relatingExchangeList!=null&&relatingExchangeList.size()>0){
						for(MasterOrderInfo exchangeBean : relatingExchangeList){
							relatingExOrRetOrdSns.add("1|"+exchangeBean.getMasterOrderSn());
						}
					}
					//存放交货单对应的退单
					OrderReturnExample relatingReturnExample = new OrderReturnExample();
					OrderReturnExample.Criteria returnCriteria = relatingReturnExample.or();
					returnCriteria.andMasterOrderSnEqualTo(masterOrderSn);
					returnCriteria.andReturnOrderStatusNotEqualTo(new Byte("4"));
					List<OrderReturn> relatingReturnList = orderReturnMapper.selectByExample(relatingReturnExample);
					if(relatingReturnList!=null&&relatingReturnList.size()>0){
						for(OrderReturn returnBean : relatingReturnList){
							relatingExOrRetOrdSns.add("2|"+returnBean.getReturnSn()+"|"+getReturnTypeName(returnBean.getReturnType()));
						}
					}
					sonOrderBean.setRelatingExOrRetOrdSns(relatingExOrRetOrdSns);
					distributeOrderStatusUtils.setSonAddReturn(0);
					distributeOrderStatusUtils.setSonAddExchang(0);
				} else {
					//存放交货单对应的换单
					List<String> relatingExOrRetOrdSns = new ArrayList<String>();
					MasterOrderInfoExample relatingExchangeExample = new MasterOrderInfoExample();
					MasterOrderInfoExample.Criteria exchangeCriteria = relatingExchangeExample.or();
					exchangeCriteria.andRelatingOriginalSnEqualTo(sonOrderSn);
					exchangeCriteria.andOrderTypeEqualTo(2);
					exchangeCriteria.andOrderStatusNotEqualTo(new Byte("2"));
					List<MasterOrderInfo> relatingExchangeList = masterOrderInfoMapper.selectByExample(relatingExchangeExample);
					if(relatingExchangeList!=null&&relatingExchangeList.size()>0){
						for(MasterOrderInfo exchangeBean : relatingExchangeList){
							relatingExOrRetOrdSns.add("1|"+exchangeBean.getMasterOrderSn());
						}
					}
					//存放交货单对应的退单
					OrderReturnExample relatingReturnExample = new OrderReturnExample();
					OrderReturnExample.Criteria returnCriteria = relatingReturnExample.or();
					returnCriteria.andRelatingOrderSnEqualTo(sonOrderSn);
					returnCriteria.andReturnOrderStatusNotEqualTo(new Byte("4"));
					List<OrderReturn> relatingReturnList = orderReturnMapper.selectByExample(relatingReturnExample);
					if(relatingReturnList!=null&&relatingReturnList.size()>0){
						for(OrderReturn returnBean : relatingReturnList){
							relatingExOrRetOrdSns.add("2|"+returnBean.getReturnSn()+"|"+getReturnTypeName(returnBean.getReturnType()));
						}
						distributeOrderStatusUtils.setSonAddReturn(0);
						distributeOrderStatusUtils.setSonAddExchang(0);
						orderStatusUtils.setCancel(0);
					}
					sonOrderBean.setRelatingExOrRetOrdSns(relatingExOrRetOrdSns);
				}
				
				//存放交货单是否问题单信息
				String orderSn = bean.getOrderSn();
				List<String> questionReason = new ArrayList<String>();
				int subQuestionStatus = bean.getQuestionStatus();
				if(subQuestionStatus!= Constant.OI_QUESTION_STATUS_NORMAL){//如果是问题单就获取问题单原因
					Map<String,Object> questionParamMap = new HashMap<String,Object>();
					questionParamMap.put("orderSn", orderSn);
					List<OrderQuestionNewDetail> questions = orderQuestionNewDetailMapper.getOrderQuestionNewDetail(questionParamMap);
					if(questions!=null&&questions.size()>0){
						for(OrderQuestionNewDetail question : questions){
							questionReason.add(question.getName());
						}
					}
				}
				sonOrderBean.setQuestionReason(questionReason);
				//存放交货单下商品列表
				List<MasterOrderGoodsDetail> sonOrderGoodsList = new ArrayList<MasterOrderGoodsDetail>();
				for(MasterOrderGoodsDetail goods : mergedMasOrdGoodsDetailList){
					if(sonOrderSn.equals(goods.getOrderSn())){
						sonOrderGoodsList.add(goods);
					}
				}
				sonOrderBean.setSonOrderGoodsList(sonOrderGoodsList);
				//存放交货单下的配送信息列表
				List<OrderDepotShipDetail> sonOrderDepotShipList = new ArrayList<OrderDepotShipDetail>();
				for(OrderDepotShipDetail depotShip : orderDepotShipDetailList){
					if(sonOrderSn.equals(depotShip.getOrderSn())){
						sonOrderDepotShipList.add(depotShip);
					}
				}
				sonOrderBean.setSonOrderDepotShipList(sonOrderDepotShipList);
				//把一组交货单信息放入交货单列表
				sonOrderList.add(sonOrderBean);
			}
			//获取付款信息
			List<MasterOrderPayTypeDetail> masterOrderPayTypeDetailList = masterOrderPayTypeDetailMapper.getMasterOrderPayTypeDetail(paramMap);
			//填充返回值
			masterOrderInfoVO.setMasterOrderInfo(masterOrderInfo);
			masterOrderInfoVO.setOrderStatusUtils(orderStatusUtils);
			masterOrderInfoVO.setMergedMasOrdGoodsDetailList(mergedMasOrdGoodsDetailList);
			masterOrderInfoVO.setSonOrderList(sonOrderList);
			masterOrderInfoVO.setMasterOrderPayTypeDetailList(masterOrderPayTypeDetailList);
			masterOrderInfoVO.setReturnSettleMoney(returnSettleMoney);
			masterOrderInfoVO.setOrderDepotShipDetailList(orderDepotShipDetailList);
		}catch(Exception e){
			logger.error("订单[" + masterOrderSn + "]查询异常" + e.getMessage(), e);
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		logger.info("查询主单所有信息耗时： "+(endTime-startTime)+"ms");
		return masterOrderInfoVO;
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
	
	@Override
	public Map getMasterOrderAddressInfo(AdminUser adminUser, String masterOrderSn,
			Integer isHistory) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0无数据，1有数据
		String msg = "获取收货人信息失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else{
				Map paramMap = new HashMap();
				paramMap.put("masterOrderSn", masterOrderSn);
				MasterOrderAddressInfo addrInfo = masterOrderAddressDetailMapper.selectMasOrdAddDetByOrderSnByMasterOrderSn(paramMap);
				if(addrInfo!=null){
					map.put("addrInfo", addrInfo);
					code = "1";
					msg = "获取收货人信息成功！";
				}else{
					msg = "该订单["+masterOrderSn+"]不存在！";
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
	public Map doSaveAddrEdit(AdminUser adminUser, MasterOrderAddressInfo formParam,
			String oldTel, String oldMobile) {
		// TODO Auto-generated method stub
		logger.info("执行保存收货人地址信息！");
		long startTime=System.currentTimeMillis();   //获取开始时间
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "保存收货人地址信息失败！";
		try{
			//获取主单号
			String masterOrderSn = formParam.getMasterOrderSn();
			//判断用户是否登录
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
			}else if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else{
				//处理参数
				String tel = formParam.getTel();
				if("******".equals(tel)){
					formParam.setTel(oldTel);
				}else if(StringUtil.isNotBlank(tel)){
					formParam.setTel(tel.trim());
				}
				
				String mobile = formParam.getMobile();
				if("******".equals(mobile)){
					formParam.setMobile(oldMobile);;
				}else if(StringUtil.isNotBlank(mobile)){
					formParam.setMobile(mobile.trim());
				}
				//拼装接口参数
				ConsigneeModifyInfo param = new ConsigneeModifyInfo();
				param.setConsignee(formParam.getConsignee());
				param.setCountry(formParam.getCountry());
				param.setProvince(formParam.getProvince());
				param.setCity(formParam.getCity());
				param.setDistrict(formParam.getDistrict());
				param.setStreet(formParam.getStreet());
				param.setEmail(formParam.getEmail());
				param.setZipcode(formParam.getZipcode());
				param.setAddress(formParam.getAddress());
				param.setTel(formParam.getTel());
				param.setMobile(formParam.getMobile());
				param.setSignBuilding(formParam.getSignBuilding());
				param.setBestTime(formParam.getBestTime());
				param.setActionUser(adminUser.getUserName());
				//调用保存接口进行保存
				ReturnInfo result = orderCommonService.editConsigneeInfoByMasterSn(masterOrderSn, param);
				if(result!=null){
					if(result.getIsOk()==1){
						code = "1";
						msg = "保存收货人信息成功！";
					}else{
						msg = result.getMessage();
					}
				}
			}
		}catch(Exception e){
			logger.error("保存收货人信息报错！"+e);
			e.printStackTrace();
		}
		long endTime=System.currentTimeMillis(); //获取结束时间
		logger.info("保存收货人信息耗时： "+(endTime-startTime)+"ms");
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public Map getMasterOrderInfoExtend(AdminUser adminUser, String masterOrderSn,
			Integer isHistory) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0无数据，1有数据
		String msg = "获取发票信息失败！";
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else{
				Map paramMap = new HashMap();
				paramMap.put("masterOrderSn", masterOrderSn);
				paramMap.put("isHistory", isHistory);
				//获取主订单信息
				MasterOrderDetail masterOrderInfo = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
				//获取主单扩展单信息
				MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
				if(masterOrderInfo!=null){
					map.put("masterOrderInfo", masterOrderInfo);
					map.put("masterOrderInfoExtend", masterOrderInfoExtend);
					code = "1";
					msg = "获取发票信息成功！";
				}else{
					msg = "该订单["+masterOrderSn+"]不存在！";
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
	public Map doSaveOtherEdit(AdminUser adminUser, String masterOrderSn,
			String invType, String invPayee, String invContent, String howOos,
			String postscript,String toBuyer) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "保存发票信息失败！";
		try{
			//判断用户是否登录
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
			}else if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else{
				//处理参数
				OrderOtherModifyInfo param = new OrderOtherModifyInfo();
				param.setHowOos(howOos);
				param.setInvContent(invContent);
				param.setInvPayee(invPayee);
				param.setInvType(invType);
				param.setPostscript(postscript);
				param.setToBuyer(toBuyer);
				//调用保存接口进行保存
				ReturnInfo result = orderCommonService.editOrderOther(masterOrderSn, adminUser.getUserName(), param);
				if(result!=null){
					if(result.getIsOk()==1){
						code = "1";
						msg = "保存发票信息成功！";
					}else{
						msg = result.getMessage();
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
	public Map queryExpress(String masterOrderSn, String depotCode, String invoiceNo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0无数据，1有数据
		String msg = "获取物流信息失败！";
		ExpressStatus logisticsInfo = new ExpressStatus();
		try{
			
			/*if(StringUtil.isTrimEmpty(depotCode)||Constant.DETAILS_DEPOT_CODE.toLowerCase().equals(depotCode.toLowerCase())){
				msg = "还未分配到发货仓！";
			}else{
			}*/

			//调用接口查询物流信息
			String interfaceUrl = QUERY_ORDER_EXPRESS_URL+"orderSn="+masterOrderSn+"&depotCode="+depotCode;
			String jsonResult = HttpClientUtil.get(interfaceUrl, null);
			//解析接口返回值  放入返回物流信息对象
			if(!jsonResult.isEmpty()&&!"[]".equals(jsonResult)){
				Map<String, Object> resultMap = JSON.parseObject(jsonResult, Map.class);
				if(!resultMap.isEmpty()&&resultMap!=null){
					List<ExpressInfo> erpStatus = new ArrayList<ExpressInfo>();
					//物流信息
					JSONArray expressJson = (JSONArray) resultMap.get("express");
					if(!"".equals(expressJson)&&expressJson!=null){
						List<ExpressStatus> list = JSONArray.parseArray(expressJson.toString(), ExpressStatus.class);
						if (StringUtil.isListNotNull(list)) {
							logisticsInfo = list.get(0);
							String com = logisticsInfo.getCom();
							if (StringUtil.isNotEmpty(com)) {
								SystemShippingExample example = new SystemShippingExample();
								example.or().andShippingCodeEqualTo(com);
								List<SystemShipping> shippings = systemShippingMapper.selectByExample(example);
								if (StringUtil.isListNotNull(shippings)) {
									logisticsInfo.setCom(shippings.get(0).getShippingName());
								}
							}
							if (StringUtil.isListNotNull(logisticsInfo.getData())) {
								for (int i = logisticsInfo.getData().size(); i >0; i--) {
									ExpressInfo expressInfo = logisticsInfo.getData().get(i - 1);
									erpStatus.add(expressInfo);
								}
							}
						}
					}
					//ERP发货状态信息
					JSONObject erpJson = (JSONObject) resultMap.get("erp");
					if(!"".equals(erpJson)&&erpJson!=null){
						ErpDepotInfo erpDepotInfo = JSON.parseObject(erpJson.toString(), ErpDepotInfo.class);
						if (StringUtil.isNotEmpty(erpDepotInfo.getCode()) && erpDepotInfo.getCode().equals("1")) {
							if (StringUtil.isListNotNull(erpDepotInfo.getErpStatusList())) {
								for (ErpStatus bean :erpDepotInfo.getErpStatusList()) {
									ExpressInfo expressInfo = new ExpressInfo();
									expressInfo.setTime(bean.getTime());
									StringBuffer erpMsg = new StringBuffer();
									if (StringUtil.isNotEmpty(bean.getDocType())) {
										erpMsg.append(bean.getDocType());
									}
									if (StringUtil.isNotEmpty(bean.getDepotCode())) {
										erpMsg.append(" 发货仓：" + bean.getDepotCode());
									}
									if (StringUtil.isNotEmpty(bean.getStatusInfo())) {
										erpMsg.append(" " +bean.getStatusInfo());
									}
									expressInfo.setContext(erpMsg.toString());
									erpStatus.add(expressInfo);
								}
							}
						}
					}
					logisticsInfo.setData(erpStatus);
					
					code = "1";
					msg = "获取物流信息成功！";	
				}
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("logisticsInfo", logisticsInfo);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public Map getAvaliableDelivery(DeliveryInfoParam param) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "获取可用承运商信息失败！";
		List<SystemShippingVo> avaliableShippingList = new ArrayList<SystemShippingVo>();
		try{
			if(param==null){
				msg = "无效入参！";
			}else{
				//拼接入参
				String interfaceUrl = SHIPPING_URL+"sendwarehouseCode="+param.getDepotCode()
						+ "&country=" + param.getCountry()
						+ "&province=" + param.getProvince()
						+ "&city=" + param.getCity()
						+ "&county=" + param.getDistrict()
						+ "&payType=" + param.getTransType();
				//调用接口返回可用供应商列表
				String xmlResult = HttpClientUtil.get(interfaceUrl, null);
				if(xmlResult==null||"".equals(xmlResult)){
					msg = "接口获取快递公司信息发生出错";
				}else{//解析结果
					ShippingOrg org = new APIxml(xmlResult).toObject();
					if(org!=null&&org.getOrgOk()){
						List<TrPriceInfo> list = org.getPriceInfos();
						for(TrPriceInfo tp : list){
							SystemShippingVo shippingVo = new SystemShippingVo();
							//填充其他信息
							shippingVo.setShippingFee(tp.getEnterPrice());
							shippingVo.setFreeMoney(0.00);
							shippingVo.setInsure("0.00");
							shippingVo.setDepotCode(param.getDepotCode());
							//填充承运商信息
							SystemShippingExample example = new SystemShippingExample();
							example.or().andShippingCodeEqualTo(tp.getOrgTransCode());
							List<SystemShipping> shippingList = systemShippingMapper.selectByExample(example);
							if(StringUtil.isListNotNull(shippingList)){
								SystemShipping shipping = shippingList.get(0);
								shippingVo.setShippingId(shipping.getShippingId());
								shippingVo.setShippingDesc(shipping.getShippingDesc());
								shippingVo.setShippingName(shipping.getShippingName());
							}
							StringBuilder newName = new StringBuilder();
							newName.append("[名称："+StringUtil.Null2Str(shippingVo.getShippingName())+"]");
							newName.append("[描述："+StringUtil.Null2Str(shippingVo.getShippingDesc())+"]");
							newName.append("[配送费："+shippingVo.getShippingFee()+"]");
							newName.append("[免费额度："+shippingVo.getShippingFee()+"]");
							String insure = shippingVo.getInsure()==null?"0":shippingVo.getInsure();
							newName.append("[保价费："+insure+"]");
							shippingVo.setShippingName(newName.toString());
							avaliableShippingList.add(shippingVo);
						}
					}
					// 如果是货到付款订单，则没有EMS
					if (param.getTransType() != null && param.getTransType().intValue() != Constant.OI_TRANS_TYPE_PRESHIP) {
						List<SystemShippingWithBLOBs> list = new ArrayList<SystemShippingWithBLOBs>();
						SystemShippingExample example = new SystemShippingExample();
						example.or().andIsCommonUseEqualTo(1);
						list = systemShippingMapper.selectByExampleWithBLOBs(example);
						for(SystemShippingWithBLOBs bean : list){
							SystemShippingVo shippingVo = new SystemShippingVo();
							//主要  配送名称和配送方式code字段
							StringBuilder newName = new StringBuilder();
							newName.append("[名称："+StringUtil.Null2Str(bean.getShippingName())+"]");
							newName.append("[描述："+StringUtil.Null2Str(bean.getShippingDesc())+"]");
							newName.append("[配送费：按实际运费计算]");
							newName.append("[免费额度：0.00]");
							String insure = bean.getInsure()==null?"0":bean.getInsure();
							newName.append("[保价费："+insure+"]");
							shippingVo.setShippingName(newName.toString());
							shippingVo.setShippingCode(bean.getShippingCode());
							avaliableShippingList.add(shippingVo);
						}
					}
					code = "1";
					msg = "获取可用承运商信息成功！";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("avaliableShippingList", avaliableShippingList);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public Map doSaveDeliveryChange(AdminUser adminUser,String shippingCode,String orderSn, String depotCode) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "修改承运商失败！";
		try{
			//判断用户是否登录
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
			}else if("".equals(orderSn)||orderSn==null){
				msg = "无效子单号！";
			}else if("".equals(shippingCode)||shippingCode==null||"".equals(depotCode)||depotCode==null){
				msg = "参数不全！";
			}else{
				//处理参数
				ConsigneeModifyInfo param = new ConsigneeModifyInfo();
				param.setOrderSn(orderSn);
				param.setActionUser(adminUser.getUserName());
				param.setShippingCode(shippingCode);
				param.setDepotCode(depotCode);
				//调用保存接口进行保存
				ReturnInfo result = orderCommonService.editShippingType(param);
				if(result!=null){
					if(result.getIsOk()==1){
						code = "1";
						msg = "修改承运商成功！";
					}else{
						msg = result.getMessage();
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
	public Map getMasterOrderPayInfo(String masterOrderSn) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "获取付款单信息失败！";
		MasterOrderDetail masterOrderPayInfo = new MasterOrderDetail();
		try{
			if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else{
				//拼装入参
				Map paramMap = new HashMap();
				paramMap.put("masterOrderSn", masterOrderSn);
				masterOrderPayInfo = masterOrderInfoDetailMapper.getMasterOrderPayInfo(paramMap);
				if(masterOrderPayInfo!=null){
					code = "1";
					msg = "获取付款单信息成功！";
				}else{
					msg = "查询不到该主单["+masterOrderSn+"]相关付款单信息！";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("masterOrderPayInfo", masterOrderPayInfo);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public Map doSaveShippingFee(AdminUser adminUser, String masterOrderSn,
			String shippingTotalFee) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "保存付款单信息失败！";
		try{
			//判断用户是否登录
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
			}else if("".equals(masterOrderSn)||masterOrderSn==null){
				msg = "无效主单号！";
			}else if("".equals(shippingTotalFee)||shippingTotalFee==null){
				msg = "运费不可为空！";
			}else{
				//处理参数
				Double shipFee = Double.parseDouble(shippingTotalFee);
				//调用保存接口进行保存
				ReturnInfo result = orderCommonService.editShippingFee(masterOrderSn, adminUser.getUserName(), shipFee);
				if(result!=null){
					if(result.getIsOk()==1){
						code = "1";
						msg = "保存付款单信息成功！";
					}else{
						msg = result.getMessage();
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
	public Map getCouponInfo(String cardNo, String couponType) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "获取券卡信息失败！";
		CardAPIBean couponInfo = new CardAPIBean();
		try{
			if("".endsWith(cardNo)||cardNo==null||"".endsWith(couponType)||couponType==null){
				msg = "参数不完整！";
			}else{
				couponInfo = cardCartSearchServiceApi.getCardBeanByNo(cardNo,Integer.valueOf(couponType));
				code = "1";
				msg = "获取券卡信息成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("couponInfo", couponInfo);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	@Override
	public Map getStock(String customCode, String channelCode) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "查询库存失败！";
		int stockNumber = 0;
		CardAPIBean couponInfo = new CardAPIBean();
		try{
			if("".endsWith(customCode)||customCode==null||"".endsWith(channelCode)||channelCode==null){
				msg = "参数不完整！";
			}else{
				SkuStock stock =  uniteStockService.queryStockBySku(customCode, channelCode, null);
				if(stock!=null){
					stockNumber = stock.getAllStock();
				}
				code = "1";
				msg = "查询库存成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("stockNumber", stockNumber);
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	/**
     * 根据选择不同级别订单来源获取订单来源列表
     * @param channelType
     * @param channelCode
     * @param shopCodes
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getOrderForms(String channelType, String channelCode, String[] shopCodes) {
        List<String> quesyShopCodes = new ArrayList<String>();
       try {
            if(StringUtil.isArrayNotNull(shopCodes)){
                quesyShopCodes = Arrays.asList(shopCodes);
            }else if(!StringUtil.isArrayNotNull(shopCodes) && StringUtil.isNotNull(channelCode)){
                quesyShopCodes = this.getShopIdByChannelId(String.valueOf(channelCode));
            } else if(!StringUtil.isArrayNotNull(shopCodes)
                    && StringUtil.isNull(channelCode)
                    && StringUtil.isNotNull(channelType)){
                quesyShopCodes = this.getShopIdByChannelType(Integer.valueOf(channelType));
            }
        } catch (Exception e) {
            logger.error("根据选择不同级别订单来源获取订单来源列表异常", e);
        }
        return quesyShopCodes;
    }
    
    @Override
    public Map<String, Object> getExchangeOrderDetail(AdminUser adminUser, String orderSn, Integer isHistory) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            map.put("success", false);
            OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
            if(orderDistribute == null){
               map.put("message", "关联子单不存在！") ;
               return map;
            }
            String masterOrderSn = orderDistribute.getMasterOrderSn();
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("masterOrderSn", masterOrderSn);
            paramMap.put("isHistory", 0);
            MasterOrderDetail masterOrderInfo = masterOrderInfoDetailMapper.selectMasOrdDetByMasterOrderSn(paramMap);
            //如果是换货单  获取换货单单号
			if(!"".equals(masterOrderInfo.getRelatingOriginalSn())&&masterOrderInfo.getRelatingOriginalSn()!=null){
				try{
					String relatingOriginalSn = masterOrderInfo.getRelatingOriginalSn();
					Map smallMap = new HashMap();
					smallMap.put("orderSn", relatingOriginalSn);
					smallMap.put("isHistory", isHistory);
					List<OrderDistribute> tempOrderDistributeList = orderDistributeDetailMapper.getOrderDistributeList(smallMap);
					if(tempOrderDistributeList!=null&&tempOrderDistributeList.size()>0){
						OrderDistribute temp = tempOrderDistributeList.get(0);
						String masOrdSnAndOrdSn = temp.getMasterOrderSn()+"&orderSn="+relatingOriginalSn;
						masterOrderInfo.setRelatingOriginalSn(masOrdSnAndOrdSn);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
            map.put("masterOrderInfo", masterOrderInfo);
            //获取收货人信息
            Map<String,Object> addressInfo = this.getMasterOrderAddressInfo(adminUser, masterOrderSn, isHistory);
            map.put("addressInfo", addressInfo.get("addrInfo"));
            
            //其他信息
            map.put("masterOrderInfoExtend", this.getMasterOrderInfoExtend(adminUser, masterOrderSn, isHistory).get("masterOrderInfoExtend"));
            
            //物流信息
            List<OrderDepotShipDetail> orderDepotShipDetailList = orderDepotShipDetailMapper.getOrderDepotShipDetail(paramMap);
            List<OrderDepotShipDetail> orderDepotShipList = new ArrayList<OrderDepotShipDetail>();
            for (OrderDepotShipDetail orderDepotShipDetail : orderDepotShipDetailList) {
                if(StringUtil.equalsIgnoreCase(orderDepotShipDetail.getOrderSn(), orderSn)){
                    orderDepotShipList.add(orderDepotShipDetail);
                }
            }
            map.put("orderDepotShipList", orderDepotShipList);
            
            //商品信息
            paramMap.put("orderSn", orderSn);
            List<MasterOrderGoodsDetail> masterOrderGoodsDetailList = masterOrderGoodsDetailMapper.getMergedMasOrdGoodsDetail(paramMap);
            List<String> skus = new ArrayList<String>();
            List<String> cardNoList = new ArrayList<String>();
            for(MasterOrderGoodsDetail bean : masterOrderGoodsDetailList){
                if(StringUtil.isNotNull(bean.getCustomCode())){
                    skus.add(bean.getCustomCode());
                }
                if(StringUtil.isNotNull(bean.getUseCard())){
					String useCard = bean.getUseCard();
					String[] cards = useCard.split(":");
					for(String cardNo : cards){
						if(StringUtil.isNotNull(cardNo)){
							cardNoList.add(cardNo);
						}
					}
				}
            }
            Map<String,ProductBarcodeList> proBarcodeMap = new HashMap<String,ProductBarcodeList>();
            if(skus!=null&&skus.size()>0){
                //批量查询ProductBarcodeList
                ProductBarcodeListExample proExample = new ProductBarcodeListExample();
                proExample.or().andCustumCodeIn(skus);
                /*List<ProductBarcodeList> proBarcodeList = productBarcodeListMapper.selectByExample(proExample);
                for(ProductBarcodeList barCodeBean : proBarcodeList){
                    proBarcodeMap.put(barCodeBean.getCustumCode(), barCodeBean);
                }*/
                
            }
            //批量查询打折券信息
			Map<String,CardAPIBean> cardMap = new HashMap<String,CardAPIBean>();
			if(cardNoList!=null&&cardNoList.size()>0){
				try{
					List<CardAPIBean> cardList = cardCartSearchServiceApi.getCardBeanListByNo(cardNoList,30);
					if(cardList!=null && cardList.size()>0){
						for(CardAPIBean bean : cardList){
							if(bean!=null){
								cardMap.put(bean.getCardNo(), bean);
							}
						}
					}	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
            for(MasterOrderGoodsDetail bean : masterOrderGoodsDetailList){
              //给配送状态名称赋值
                bean.setShippingStatusName(getDepotShipStatusName(bean.getShippingStatus()));
                //给货号、条形码、颜色、尺码名赋值
                String custumCode = bean.getCustomCode();
                if(!"".equals(custumCode)&&custumCode!=null){
                    ProductBarcodeList proBarCode = proBarcodeMap.get(custumCode);
                    if(proBarCode!=null){
                        //给货号、条形码赋值
                        bean.setGoodsSn(proBarCode.getGoodsSn());//货号
                        bean.setBarcode(proBarCode.getBarcode());//条形码
                        //判断颜色名和尺码名是否为空，为空就拿11位码去查询颜色名和尺码名并给bean赋值
                        String goodsColorName = bean.getGoodsSizeName();
                        String goodsSizeName = bean.getSizeName();
                        if("".equals(goodsColorName)||goodsColorName==null||"".equals(goodsSizeName)||goodsSizeName==null){
                            bean.setGoodsColorName(proBarCode.getColorName());
                            bean.setGoodsSizeName(proBarCode.getSizeName());
                        }
                    }
                }
                //给打折券赋值
				List<Map> couponList = new ArrayList<Map>();
				String useCard = bean.getUseCard();
				StringBuffer cardsBuffer = new StringBuffer("");
				if(!"".equals(useCard)&&useCard!=null){
					String[] cards = useCard.split(":");
					for(String cardNo : cards){//遍历打折券卡号  获取打折券列表
						CardAPIBean couponBean = cardMap.get(cardNo);
						if(couponBean!=null){
							Map couponMap = new HashMap();
							couponMap.put("cardNo", couponBean.getCardNo()==null?"":couponBean.getCardNo());//打折券卡号
							couponMap.put("cardLn", couponBean.getCardLn()==null?"":couponBean.getCardLn());//批次号
							couponMap.put("cardMoney",couponBean.getCardMoney());//折扣（打折券）
							couponMap.put("userId", couponBean.getUserId()==null?"":couponBean.getUserId());//绑定用户id
							couponMap.put("status", couponBean.getStatus());//状态(-1未启用，0已领用，1已经激活，2卡已经充值，3卡作废，4已使用)
							couponMap.put("effectDateStr", couponBean.getEffectDateStr()==null?"":couponBean.getEffectDateStr());
							couponMap.put("expireTimeStr", couponBean.getExpireTimeStr()==null?"":couponBean.getExpireTimeStr());
							couponList.add(couponMap);
						}
					}
				}
				bean.setCouponList(couponList);
            }
            map.put("masterOrderGoodsDetailList", masterOrderGoodsDetailList);
            
            //支付信息masterOrderPayInfo
            List<PayType> payTypeList = new ArrayList<PayType>();
            payTypeList = JSON.parseArray(orderDistribute.getPayInfo(), PayType.class);
            
            //获取主单付款信息
            List<MasterOrderPayTypeDetail> masterOrderPayTypeDetailList = masterOrderPayTypeDetailMapper.getMasterOrderPayTypeDetail(paramMap);
            List<MasterOrderPayTypeDetail> masterOrderPayList = new ArrayList<MasterOrderPayTypeDetail>();
            for(PayType payType : payTypeList){
                for(MasterOrderPayTypeDetail bean : masterOrderPayTypeDetailList){
                    // 判断支付状态和支付权限 加载支付未支付按钮
                    int pay = 0;
                    
                    int unpay = 0;
                   
                    bean.setPay(pay);
                    bean.setUnpay(unpay);
                    if(payType.getpId() == Integer.valueOf(bean.getPayId())){
                        bean.setPayTotalfee(payType.getPayFee().toString());
                        masterOrderPayList.add(bean);
                    }
                }
                
            }
            map.put("masterOrderPayList", masterOrderPayList);
            map.put("success", true);
            
        } catch (Exception e) {
            logger.error("换单：获取订单信息出错！"+e,e);
        }
        return map;
    }

	@Override
	public Paging getOrderInfoPage(Common model, PageHelper helper) throws Exception {
		logger.info("OrderInfoServiceImpl.getOrderInfoPage .  bengin      Common =  " + JSON.toJSONString(model) +";helper= "+JSON.toJSONString(helper));
		// 编辑SQL查询参数
		OrderInfoSearchExample example = new OrderInfoSearchExample();
		example.setOrderByClause("common.add_time desc");
		OrderInfoSearchExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		example.setUseWkUdDistribute(false);
		example.setMainOrderInfo(true);
		boolean condition = true;
		if (null != model) {
			if (StringUtil.isNotBlank(model.getExportTemplateType())){
				//财务模板使用
				if("1".equals(model.getExportTemplateType())){
					example.setUserOp(true);
					example.setFinance(true);
				}
				//物流模板使用
				if("2".equals(model.getExportTemplateType())){	
					example.setUserOAI(true);
					example.setUseWkUdDistribute(true);
				}
			}
			// 这里不应该默认去关联商品表信息
			example.setUserOg(true);
			//下单时间升序
			if("addTime".equals(model.getSort())){
				example.setOrderByClause("common.add_time " + model.getDir());
			}
			// 发货时间排序
			if("deliveryTime".equals(model.getSort())){
				example.setOrderByClause("common.delivery_time " + model.getDir());
			}
			//输入查询订单号集合
			if(StringUtil.isNotBlank(model.getOrderSnArr())){
				condition = false;
				String orderSnArr = model.getOrderSnArr();
				String[] orderSnArrs = orderSnArr.split(",");
				Set<String> setArr = new HashSet<String>();
				for(String orderSn : orderSnArrs){
					if(StringUtil.isNotBlank(orderSn)){
						setArr.add(orderSn);
					}
				}
				List<String> arrList = new ArrayList<String>(setArr);
				criteria.andOrderSnIn(arrList);
			}
			// 历史OR三个月订单
			if (StringUtil.isNotNull(model.getListDataType())) {
				if (model.getListDataType().equals("newDate")) {
					example.setListDataType(true);
				} else if (model.getListDataType().equals("historyDate")) {	
					example.setListDataType(false);
				}
			} else {
				example.setListDataType(true);
			}
			if (StringUtil.isNotNull(model.getMainOrChild()) && model.getMainOrChild().equals("child") ) { //子订单
				example.setMainOrderInfo(false);
			}
			// 订单所属站点
			if (StringUtil.isNotEmpty(model.getOrderFromSec()) && !model.getOrderFromSec().equals("-1")) {
				condition = false;
				criteria.andChannelCodeEqualTo(model.getOrderFromSec());
			} else {
				condition = false;
				criteria.andChannelCodeIn(model.getSites());
			}
			// 订单所属站点
			if (StringUtil.isArrayNotNull(model.getOrderFroms())) {
				if (model.getOrderFroms().length > 1) {
					condition = false;
					criteria.andOrderFromIn(Arrays.asList(model.getOrderFroms()));
				} else {
					condition = false;
					criteria.andOrderFromEqualTo(model.getOrderFroms()[0]);
				}
			}
			// 主订单号
			if (StringUtil.isNotNull(model.getMasterOrderSn())) {
				condition = false;
				criteria.andMasterOrderSnEqualTo(model.getMasterOrderSn().trim());
			}
			//子订单号
			if (StringUtil.isNotNull(model.getOrderSn())) {
				condition = false;
				criteria.andOrderSnEqualTo(model.getOrderSn());
			}
			
			// 关联换货单原订单号
			if (StringUtil.isNotNull(model.getRelatingOriginalSn())) {
				condition = false;
				criteria.andRelatingExchangeSnEqualTo(model.getRelatingOriginalSn().trim());
			}
			// 关联退单编号
			if (StringUtil.isNotNull(model.getRelatingReturnSn())) {
				condition = false;
				criteria.andRelatingReturnSnEqualTo(model.getRelatingReturnSn()
						.trim());
			}
			// 关联退款单订单号
			if (StringUtil.isNotNull(model.getRelatingRemoneySn())) {
				condition = false;
				criteria.andRelatingRemoneySnEqualTo(model.getRelatingRemoneySn().trim());
			}

			/*** 外部交易号 ***/
			if (StringUtil.isNotNull(model.getOrderOutSn())) {
				condition = false;
				criteria.andOrderOutSnEqualTo(model.getOrderOutSn());
			}
			/*** 外部交易号 ***/
			if (null != model.getTransType() && model.getTransType() >= 0) {
				condition = false;
				criteria.andTransTypeEqualTo(model.getTransType());
			}
			/*** 订单类型 ***/
			if (null != model.getOrderType() && model.getOrderType() >= 0) {
				condition = false;
				criteria.andOrderTypeEqualTo(model.getOrderType());
			}
			/*** 订单种类 ***/
			if (null != model.getOrderCategory()
					&& model.getOrderCategory() >= 0) {
				condition = false;
				criteria.andOrderCategoryEqualTo(model.getOrderCategory());
			}
			/*** 订单来源 ***/
			if (StringUtil.isNotNull(model.getReferer()) && !"-1".equals(model.getReferer())) {
					condition = false;
					criteria.andRefererLike(model.getReferer());
			}
			/*** 是否团购 ***/
			if (null != model.getIsGroup() && model.getIsGroup() >= 0) {
				condition = false;
				criteria.andIsGroupEqualTo(model.getIsGroup());
			}
			/*** 是否预售 ***/
			if (null != model.getIsAdvance() && model.getIsAdvance() >= 0) {
				condition = false;
				criteria.andIsAdvanceEqualTo(model.getIsAdvance());
			}
			/*** 支付类型ID ***/
			if (null != model.getPayId() && model.getPayId() >= 0) {
				condition = false;
				example.setUserOp(true);
				criteria.andPayIdEqualTo(model.getPayId());
			}
			/*** 订单状态 ***/
			if (null != model.getOrderStatus() && model.getOrderStatus() >= 0) {
				condition = false;
				criteria.andOrderStatusEqualTo(model.getOrderStatus());
			}
			if (null != model.getPayStatus() && model.getPayStatus() >= 0) {
				criteria.andPayStatusEqualTo(model.getPayStatus());
			}
			if (null != model.getShipStatus() && model.getShipStatus() >= 0) {
				condition = false;
				criteria.andShipStatusEqualTo(model.getShipStatus());
			}
			if (null != model.getDepotStatus() && model.getDepotStatus() >= 0) {
				condition = false;
				criteria.andDepotStatusEqualTo(model.getDepotStatus());
			}

			if (StringUtil.isNotNull(model.getSelectTimeType()) 
					&& (StringUtil.isNotNull(model.getStartTime()) 
						|| StringUtil.isNotNull(model.getEndTime())) ) {
				//下单时间
				if (model.getSelectTimeType().equals("addTime")) {
					if (StringUtil.isNotNull(model.getStartTime())
							&& StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						criteria.andAddTimeBetween(
								DateTimeUtils.parseStr(model.getStartTime()),
								DateTimeUtils.parseStr(model.getEndTime()));
					} else if (StringUtil.isNotNull(model.getStartTime())) {
						condition = false;
						criteria.andAddTimeGreaterThanOrEqualTo(DateTimeUtils
								.parseStr(model.getStartTime()));
					} else if (StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						criteria.andAddTimeLessThanOrEqualTo(DateTimeUtils
								.parseStr(model.getEndTime()));
					}
				}
				//确认时间
				if (model.getSelectTimeType().equals("confirmTime")) {
					if (StringUtil.isNotNull(model.getStartTime())
							&& StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						criteria.andConfirmTimeBetween(
								DateTimeUtils.parseStr(model.getStartTime()),
								DateTimeUtils.parseStr(model.getEndTime()));
					} else if (StringUtil.isNotNull(model.getStartTime())) {
						condition = false;
						criteria.andConfirmTimeGreaterThanOrEqualTo(DateTimeUtils
								.parseStr(model.getStartTime()));
					} else if (StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						criteria.andConfirmTimeLessThanOrEqualTo(DateTimeUtils
								.parseStr(model.getEndTime()));
					}
				}
				
				//付款时间
				if (model.getSelectTimeType().equals("payTime")) {
					if (StringUtil.isNotNull(model.getStartTime())
							&& StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						example.setUserOp(true);
						criteria.andPayTimeBetween(
								DateTimeUtils.parseStr(model.getStartTime()),
								DateTimeUtils.parseStr(model.getEndTime()));
					} else if (StringUtil.isNotNull(model.getStartTime())) {
						condition = false;
						example.setUserOp(true);
						criteria.andPayTimeGreaterThanOrEqualTo(DateTimeUtils
								.parseStr(model.getStartTime()));
					} else if (StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						example.setUserOp(true);
						criteria.andPayTimeLessThanOrEqualTo(DateTimeUtils
								.parseStr(model.getEndTime()));
					}
				}
				//发货时间
				if (model.getSelectTimeType().equals("deliveryTime")) {
					if (StringUtil.isNotNull(model.getStartTime())
							&& StringUtil.isNotNull(model.getEndTime())) {
						
						condition = false;
						criteria.andDeliveryTimeBetween(
								DateTimeUtils.parseStr(model.getStartTime()),
								DateTimeUtils.parseStr(model.getEndTime()));
						
					} else if (StringUtil.isNotNull(model.getStartTime())) {
						
						condition = false;
						criteria.andDeliveryTimeGreaterThanOrEqualTo(DateTimeUtils
								.parseStr(model.getStartTime()));
						
					} else if (StringUtil.isNotNull(model.getEndTime())) {
						
						condition = false;
						criteria.andShippingTimeLessThanOrEqualTo(DateTimeUtils
								.parseStr(model.getEndTime()));
					}
				}
				//结算时间
				if (model.getSelectTimeType().equals("clearTime")) {
					if (StringUtil.isNotNull(model.getStartTime())
							&& StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						criteria.andClearTimeBetween(
								DateTimeUtils.parseStr(model.getStartTime()),
								DateTimeUtils.parseStr(model.getEndTime()));
					} else if (StringUtil.isNotNull(model.getStartTime())) {
						condition = false;
						criteria.andClearTimeGreaterThanOrEqualTo(DateTimeUtils
								.parseStr(model.getStartTime()));
					} else if (StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						criteria.andClearTimeLessThanOrEqualTo(DateTimeUtils
								.parseStr(model.getEndTime()));
					}
				}
				
				//付款单生成时间
				if (model.getSelectTimeType().equals("createTime")) {
					
					example.setUserOp(true);
					if (StringUtil.isNotNull(model.getStartTime())
							&& StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						
						criteria.andCreateTimeBetween(
								DateTimeUtils.parseStr(model.getStartTime()),
								DateTimeUtils.parseStr(model.getEndTime()));
					} else if (StringUtil.isNotNull(model.getStartTime())) {
						condition = false;
						criteria.andCreateTimeGreaterThanOrEqualTo(DateTimeUtils
								.parseStr(model.getStartTime()));
					} else if (StringUtil.isNotNull(model.getEndTime())) {
						condition = false;
						criteria.andCreateTimeLessThanOrEqualTo(DateTimeUtils
								.parseStr(model.getEndTime()));
					}
				}
			}
			
			if (StringUtil.isNotNull(model.getUserName())) {
				condition = false;
				criteria.andUserNameEqualToByOrderInfo(model.getUserName());
			}
			
			//收货人
			if (StringUtil.isNotNull(model.getConsignee())) {
				example.setUserOAI(true);
				condition = false;
				criteria.andConsigneeEqualTo(model.getConsignee());
			}
			
			if (StringUtil.isNotNull(model.getAddress())) {
				condition = false;
				criteria.andAddressLike("%" + model.getAddress() + "%");
			}
			
			if (null != model.getShippingId() && model.getShippingId() >= 0) {
				condition = false;
				criteria.andShippingIdEqualTo(model.getShippingId());
			}
			/*** 订单商品号 ***/
			if (StringUtil.isNotNull(model.getSkuSn())) {
				condition = false;
				example.setUserOg(true);
				if ( 11 ==  model.getSkuSn().length() ) {
					criteria.andCustomCodeEqualTo(model.getSkuSn());
				} else if(6 ==   model.getSkuSn().length() ) {
					criteria.andCustomCodeLike(model.getSkuSn());
				}
			}
			
			//地区
			if (StringUtil.isNotNull(model.getDistrict())) {
				if (Integer.parseInt(model.getDistrict()) > 0) {
					example.setUserOAI(true);
					condition = false;
					criteria.andDistrictEqualTo(Integer.parseInt(model
							.getDistrict()));
				}
			}
			
			//城市
			if (StringUtil.isNotNull(model.getCity())) {
				if (Integer.parseInt(model.getCity()) > 0) {
					example.setUserOAI(true);
					condition = false;
					criteria.andCityEqualTo(Integer.parseInt(model.getCity()));
				}
			}
			
			//省份
			if (StringUtil.isNotNull(model.getProvince())) {
				if (Integer.parseInt(model.getProvince()) > 0) {
					example.setUserOAI(true);
					condition = false;
					criteria.andProvinceEqualTo(Integer.parseInt(model
							.getProvince()));
				}
			}
			
			//国家
			if (StringUtil.isNotNull(model.getCountry())) {
				if (Integer.parseInt(model.getCountry()) > 0) {
					example.setUserOAI(true);
					condition = false;
					criteria.andCountryEqualTo(Integer.parseInt(model
							.getCountry()));
				}
			}
			
			//邮费
			if (null != model.getShippingTotalFee() 
					&& model.getShippingTotalFee().doubleValue() >= 0) {
				condition = false;
				criteria.andShippingTotalFeeEqualTo(model.getShippingTotalFee());
			}
			
			//收货人电话
			if (StringUtil.isNotEmpty(model.getTel())) {
				example.setUserOAI(true);
				condition = false;
				String encTel = model.getTel();
				List<String> tels = new ArrayList<String>();
				tels.add(model.getTel());
				tels.add(encTel);
				criteria.andTelIn(tels);
			}
			
			//收货人手机
			if (StringUtil.isNotEmpty(model.getMobile())) {
				example.setUserOAI(true);
				condition = false;
				String encMobile = model.getMobile();
				List<String> mobiles = new ArrayList<String>();
				mobiles.add(model.getMobile());
				mobiles.add(encMobile);
				criteria.andMobileIn(mobiles);
			}
			
			//注册人手机
			if (StringUtil.isNotEmpty(model.getRegisterMobile())) {
				condition = false;
				criteria.andRegisterMobileEqualTo(model.getRegisterMobile());
			}
			
			// 问题单状态
			if (model.getQuestionStatus() != null && model.getQuestionStatus().intValue() != -1) {
				condition = false;
				criteria.andQuestionStatusEqualTo(model.getQuestionStatus());
			}
			
			// 预售订单
			if (model.getIsAdvance() != null && model.getIsAdvance().intValue() != -1) {
				condition = false;
				criteria.andIsAdvanceEqualTo(model.getIsAdvance());
			}
			// 拆单状态
			if (model.getSplitStatus() != null && model.getSplitStatus().intValue() != -1) {
				condition = false;
				criteria.andSplitStatusEqualTo(model.getSplitStatus());
			}
			
			if (StringUtil.isNotEmpty(model.getInvoiceNo())) {
				condition = false;
				criteria.andInvoiceNoEqualTo(model.getInvoiceNo());
			}
			//发货方
			if(StringUtil.isNotEmpty(model.getDeliveryType())){
				condition = false;
				criteria.andDeliveryTypeEqualTo(model.getDeliveryType());
			}
			//source
			if(model.getSource()!=null){
				condition = false;
				criteria.andSourceEqualTo(model.getSource());
			}
	
			//订单有效、隐藏、全部状态
			if (null != model.getOrderView()) {
				if (model.getOrderView() == 0) {//默认显示有效订单
					criteria.andOrderStatusNotEqualTo((byte)2);
				} else if (2 == model.getOrderView()) {//显示隐藏订单
					condition = false;
					criteria.andOrderStatusEqualTo((byte)2);
				} else if( 1  == model.getOrderView()){//显示全部订单
					condition = false;
				}
			}
		}
		if (condition) {
			//显示一周内订单
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			Date date = calendar.getTime();
			criteria.andAddTimeGreaterThanOrEqualTo(date);
		}
		List<Common> commons = null;
		int num = 0;
		logger.info("OrderInfoServiceImpl.getOrderInfoPage .start Sql.ExportTemplateType =  " + model.getExportTemplateType()+"; Common= " + JSON.toJSONString(model));
		/*if(StringUtil.isNotBlank(model.getExportTemplateType()) && "1".equals(model.getExportTemplateType())){//财务模板使用
			commons = orderInfoSearchMapper.selectCommonByFinance(example);
			logger.info("OrderInfoServiceImpl.getOrderInfoPage ...Sqling..ExportTemplateType =  " + model.getExportTemplateType() );
			num = orderInfoSearchMapper.countCommonByFinance(example);
		logger.info("OrderInfoServiceImpl.getOrderInfoPage ...Sqling..ExportTemplateType =  " + model.getExportTemplateType()+"num = "+ num);
		}else if(StringUtil.isNotBlank(model.getExportTemplateType()) && "2".equals(model.getExportTemplateType())){//物流模板使用
			commons = orderInfoSearchMapper.selectCommonByLogistics(example);
			logger.info("OrderInfoServiceImpl.getOrderInfoPage ...Sqling..ExportTemplateType =  " + model.getExportTemplateType());
			num =  orderInfoSearchMapper.countCommonByLogistics(example);
			logger.info("OrderInfoServiceImpl.getOrderInfoPage ...Sqling..ExportTemplateType =  " + model.getExportTemplateType() +"num = "+ num);
		}*/
		if(StringUtil.isNotBlank(model.getExportTemplateType()) && "3".equals(model.getExportTemplateType())){//导出店铺商品使用
			commons = orderInfoSearchMapper.selectCommonGoodsByExample(example);
			logger.info("OrderInfoServiceImpl.getOrderInfoPage .Sqling..ExportTemplateType =  " + model.getExportTemplateType());
			num = orderInfoSearchMapper.countCommonGoodsByExample(example);
			logger.info("OrderInfoServiceImpl.getOrderInfoPage .Sqling..ExportTemplateType =  " + model.getExportTemplateType()+"num = "+ num);
		} else {//默认模板使用
			commons = orderInfoSearchMapper.selectCommonByExample(example);
			logger.info("OrderInfoServiceImpl.getOrderInfoPage .Sqling..ExportTemplateType =  " + model.getExportTemplateType());
			num = orderInfoSearchMapper.countCommonByExample(example);
			logger.info("OrderInfoServiceImpl.getOrderInfoPage .Sqling..ExportTemplateType =  " + model.getExportTemplateType()+"num = "+ num);
		}
		logger.info("OrderInfoServiceImpl.getOrderInfoPage .end Sql." );
		Paging paging = new Paging(num, commons);
		logger.info("OrderInfoServiceImpl.getOrderInfoPage .end.");
		return paging;
	}

	@Override
	public Common InputIsDerivedOrderListByOrderSnOrOrderOutSn(InputStream is,
			StringBuffer sb, int inputType) throws Exception {
		List<String> list = new ArrayList<String>();
		
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环Excel表格内容
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				String errorLine = "第" + (rowNum + 1) + "行";
				if (hssfRow == null) {
					continue;
				}
				HSSFCell cell0 = hssfRow.getCell((short) 0);
				// 订单号
				String orderSn = getValue(cell0);
				
				if(StringUtil.isNotBlank(orderSn)){
					list.add(orderSn);
				}
				
			}
		}
		
		Common common = new Common();
		if(0 ==inputType){
		//	common.setOrderSnList(list);
			common.setMasterOrderSnList(list);
		}else if (1 ==inputType){
			common.setOrderOutSnList(list);
		}

		return common;
		
	}

	/***
	 *问题单列表 
	 ***/
	public Paging getorderQuestionSearchResultVOsPage(
			OrderQuestionSearchVO searchVO) throws Exception {
	
		if (StringUtil.isNotNull(searchVO.getOrderFormSec())) {
			// 二级关联有值
			if (StringUtil.isTaoBaoChannel(searchVO.getOrderFormSec()) ) {
				// 表示淘宝
				searchVO.setChannelUseLevel(searchVO.getUseLevel());
				searchVO.setUseLevel(null);
			}
		}
		
		if (StringUtil.isEmpty(searchVO.getOrderClause())) {
			searchVO.setOrderClause("oi.add_time DESC");
		}
	
		if(StringUtil.isNotBlank(searchVO.getReferer())){
			searchVO.setReferer(searchVO.getReferer().trim());
		}
		
		//排序
		if("addTime".equals(searchVO.getSort())){
			searchVO.setOrderClause("oi.add_time " + searchVO.getDir());
		}
		// 发货时间排序
		/*if("shippingTime".equals(searchVO.getSort())){
			searchVO.setOrderByClause("oit.shipping_time " + searchVO.getDir());
		}*/
		
		//用户级别
		if(Constant.PLEASE_SELECT_STRING.equals(searchVO.getUseLevel())){
			searchVO.setUseLevel(null);
			searchVO.setChannelUseLevel(null);
		}
		
		if("addTime".equals(searchVO.getSelectTimeType())){
			if (StringUtil.isEmpty(searchVO.getAddTimeStart())) {
				searchVO.setAddTimeStart(null);
			}
			if (StringUtil.isEmpty(searchVO.getAddTimeEnd())) {
				searchVO.setAddTimeEnd(null);
			}
		}
		
		if("questionTime".equals(searchVO.getSelectTimeType())){
			if (StringUtil.isEmpty(searchVO.getAddTimeStart())) {
				searchVO.setAddTimeStart(null);
			}
			if (StringUtil.isEmpty(searchVO.getAddTimeEnd())) {
				searchVO.setAddTimeEnd(null);
			}
		}
	
		/*	if (StringUtil.isEmpty(searchVO.getQuestionTimeStart())) {
				searchVO.setQuestionTimeStart(null);
			}
			if (StringUtil.isEmpty(searchVO.getQuestionTimeEnd())) {
				searchVO.setQuestionTimeEnd(null);
			}*/
		
		// 订单所属店铺
		if (StringUtil.isNotEmpty(searchVO.getOrderFormFirst()) && !searchVO.getOrderFormFirst().equals("-1")) {
			List<String> orderForms = getOrderForms(searchVO.getOrderFormFirst(), searchVO.getOrderFormSec(), searchVO.getOrderFormsVo());
			if (StringUtil.isListNotNull(orderForms)) {
				searchVO.setOrderForms(orderForms);
			} else {
				logger.error("查询订单来源列表失败！");
				Paging paging = new Paging(0, null);
				paging.setMessage("查询订单来源列表失败！");
				return paging;
			}
		}
		
		List<OrderQuestionSearchResultVO> resultVOs = null;
		int count = 0;
		Paging paging = null;
		
		if("child".equals(searchVO.getMainOrChild())){ // 交货单问题单

			if("1".equals(searchVO.getExportState()) &&  "false".equals(searchVO.getListDataType()) ){ //导出问题单   //缺货问题单
				 resultVOs = orderInfoSearchMapper.getOrderChildQuestionVOByexport(searchVO);
				 count = orderInfoSearchMapper.countOrderChildQuestionVOByexport(searchVO);
				 paging = new Paging(count, resultVOs);
				
			}else{ //查询问题单
				 resultVOs = orderInfoSearchMapper.getOrderChildQuestionVOBySearch(searchVO);
				 count = orderInfoSearchMapper.getOrderChildQuestionVOBySearchCounts(searchVO);
				 paging = new Paging(count, resultVOs);
			}

		} else {  //订单号问题单
	
			if("1".equals(searchVO.getExportState())  &&  "false".equals(searchVO.getListDataType()) ){ //导出问题单 //缺货问题单
				 resultVOs = orderInfoSearchMapper.getOrderChildQuestionVOByexport(searchVO);
				 count = orderInfoSearchMapper.countOrderChildQuestionVOByexport(searchVO);
				 paging = new Paging(count, resultVOs);
				
			}else{ //查询问题单
			
				 resultVOs = orderInfoSearchMapper.getOrderQuestionVOBySearch(searchVO);
				 count = orderInfoSearchMapper.getOrderQuestionVOBySearchCounts(searchVO);
				 paging = new Paging(count, resultVOs);
			 
			}
		}
		
		return paging;

	}

	@Override
	public List<ShortageQuestion> getShortageQuestionList(String orderSn,
			Integer id) {
		
		OrderQuestionSearchVO searchVO = new OrderQuestionSearchVO();
		searchVO.setOrderSn(orderSn);
		searchVO.setId(id);	
		List <ShortageQuestion> list = orderInfoSearchMapper.getShortageQuestionList(searchVO);
		return list;
		
		
	}
	
	//转换excel类型
	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (null == hssfCell) {
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			Double temp = hssfCell.getNumericCellValue();
			BigDecimal bd = new BigDecimal(temp);
			return String.valueOf(bd.toString());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	@Resource
	private OrderInfoSearchMapper orderInfoSearchMapper;
	
//	@Resource
//	private SystemAdminUserService systemAdminUserService;
	
	
	public List<String> getShopIdByChannelId(String channelCode) throws Exception {
		ReturnInfo<List<ChannelShop>> info = channelInfoService.findChannelShopByChannelCode(channelCode);
		List<String> shopList = new ArrayList<String>();
		List<ChannelShop> infos = null;
		if (null != info && info.getIsOk() == Constant.OS_YES) {
			infos = info.getData();
			if (infos != null) {
				for (int i = 0; i < infos.size(); i++) {
					shopList.add(infos.get(i).getChannelCode());
				}
				return shopList;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	

	public List<String> getShopIdByChannelType(int channelType)
			throws Exception {
		
		ReturnInfo<List<CsChannelInfo>> info = channelInfoService.findChannelInfoByChannelType((short)channelType);
		List<String> shopCodeList = new ArrayList<String>();
		List<CsChannelInfo> infos = null;
		if (null != info && info.getIsOk() == Constant.OS_YES) {
			infos = info.getData();
			if (infos != null) {
				for (int i = 0; i < infos.size(); i++) {
					List<String> shopListByChannelId = getShopIdByChannelId(infos
							.get(i).getChanelCode());
					if (null != shopListByChannelId
							&& shopListByChannelId.size() > 0) {
						shopCodeList.addAll(shopListByChannelId);
					}
				}
				return shopCodeList;

			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	private String getReturnTypeName(int returnType){
		String returnTypeName = "";
		if(returnType==1){
			returnTypeName = "退货单";
		}else if(returnType==2){
			returnTypeName = "拒收入库单";
		}else if(returnType==3){
			returnTypeName = "普通退款单";
		}else if(returnType==4){
			returnTypeName = "额外退款单";
		}else if(returnType==5){
			returnTypeName = "失货退货单";
		}
		return returnTypeName;
	}

	@Override
	public Map initSendMessage(String masterOrderSn, String channelCode) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "获取初始化数据失败！";
		String encodedMobile = "";
		String shopCode="";
		try{
			if(StringUtil.isEmpty(masterOrderSn)){
				msg="主订单号为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			if(StringUtil.isEmpty(channelCode)){
				msg="订单来源为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//根据订单号获取联系方式
			MasterOrderAddressInfo info = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(info==null){
				msg="订单不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//加密的联系方式
			String mobile = info.getMobile();
			String tel = info.getTel();
			if(StringUtil.isNotEmpty(mobile)){
				encodedMobile = mobile;
			}else if(StringUtil.isNotEmpty(tel)){
				encodedMobile = tel;
			}else{
				msg="获取联系方式失败！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//根据订单来源获取短信通道
			ChannelShop channelShop = new ChannelShop();
			ReturnInfo<ChannelShop> returnInfo = channelInfoService.findShopInfoByShopCode(channelCode);
			if (null != returnInfo && returnInfo.getIsOk() == Constant.OS_YES && null != returnInfo.getData()) {
				channelShop = returnInfo.getData();
				if(channelShop!=null&&StringUtil.isNotEmpty(channelShop.getChannelCode())){
					String realChannelCode = channelShop.getChannelCode();
					shopCode = "mb";
					code = "1";
					msg = "获取数据成功！";
				}else{
					msg="获取短信通道失败！";
					map.put("code", code);
					map.put("msg", msg);
					return map;	
				}
			}else{
				msg="获取短信通道失败！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		map.put("encodedMobile", encodedMobile);
		map.put("shopCode", shopCode);
		return map;
	}

	@Override
	public Map doSendMessage(AdminUser adminUser, String encodedMobile,
			String shopCode, String masterOrderSn, String channelCode,
			String message) {
		// TODO Auto-generated method stub
		//这里的channelCode即orderFrom,shopCode即短信通道
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "发送短信失败！";
		try{
			//判断用户是否登录
			if(adminUser==null){
				msg = "获登录失效！请重新登录！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			MasterOrderInfo info = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if(info==null){
				msg = "该订单["+masterOrderSn+"]不存在！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//解密联系方式
			String decodeMobile = encodedMobile.trim();
			//调用发送短信接口
			String sendType = "";
			if("bg".equals(shopCode)){
				sendType="bg_121";
			}else if("yf".equals(shopCode)){
				sendType="zxs_mb";
			}else if("mb".equals(shopCode)){
				sendType="mb_121";
			}
			if(StringUtil.isNotEmpty(sendType)&&
					StringUtil.isNotEmpty(decodeMobile)&&
					StringUtil.isNotEmpty(message)&&StringUtil.isNotEmpty(channelCode)){
				User user = new User();
				user.setUsername(ConfigCenter.getProperty("smsUser"));
				user.setPassword(ConfigCenter.getProperty("smsPwd"));
				Message messageBean = new Message();
				messageBean.setPhoneNO(decodeMobile);
				messageBean.setChannelCode("123");//不能为空  但是值随便
				messageBean.setSendType(sendType);
				messageBean.setMsgContent(message);
				State resultState = sMSService.send(user, messageBean);
				if(resultState!=null){
					if(!State.SUCCESSFULLY.equals(resultState.getState())){
						msg = resultState.getMessage();
					}else{
						code = "1";
						msg = "发送短信成功！";
					}
				}
				//记日志
				MasterOrderAction masterOrderAction = new MasterOrderAction();
				masterOrderAction.setMasterOrderSn(masterOrderSn);
				masterOrderAction.setActionUser(adminUser.getUserName());
				masterOrderAction.setOrderStatus(info.getOrderStatus());
				masterOrderAction.setShippingStatus(info.getShipStatus());
				masterOrderAction.setPayStatus(info.getPayStatus());
				masterOrderAction.setActionNote("发送短信内容："+message+"|发送结果："+msg);
				masterOrderAction.setLogTime(new Date());
				masterOrderActionMapper.insert(masterOrderAction);
			}else{
				msg = "提交参数不全！！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	@Override
	public Map toolsDoSendMessage(AdminUser adminUser, String mobile,
			String sendType, String message) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "发送短信失败！";
		try{
			//判断用户是否登录
			if(adminUser==null){
				msg = "获登录失效！请重新登录！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//转换短信通道
			if("bg".equals(sendType)){
				sendType="bg_121";
			}else if("yf".equals(sendType)){
				sendType="zxs_mb";
			}else if("mb".equals(sendType)){
				sendType="mb_121";
			}
			//调用发送短信接口
			if(StringUtil.isNotEmpty(sendType)&&
					StringUtil.isNotEmpty(mobile)&&
					StringUtil.isNotEmpty(message)){
				User user = new User();
				user.setUsername(ConfigCenter.getProperty("smsUser"));
				user.setPassword(ConfigCenter.getProperty("smsPwd"));
				Message messageBean = new Message();
				messageBean.setPhoneNO(mobile);
				messageBean.setChannelCode("123");//不能为空  但是值随便
				messageBean.setSendType(sendType);
				messageBean.setMsgContent(message);
				State resultState = sMSService.send(user, messageBean);
				if(resultState!=null){
					if(!State.SUCCESSFULLY.equals(resultState.getState())){
						msg = resultState.getMessage();
					}else{
						code = "1";
						msg = "发送短信成功！";
					}
				}
				//记日志
				SendMessageToolRecord record = new SendMessageToolRecord();
				record.setActionUser(adminUser.getUserName());
				record.setSendTime(new Date());
				record.setSendType(sendType);
				record.setSendContent(message);
				record.setSendNumber(mobile);
				record.setSendResult(msg);
				sendMessageToolRecordMapper.insertSelective(record);
			}else{
				msg = "提交参数不全！！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	

}
