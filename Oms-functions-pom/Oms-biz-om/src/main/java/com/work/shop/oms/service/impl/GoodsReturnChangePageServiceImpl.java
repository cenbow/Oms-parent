package com.work.shop.oms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.bean.GoodsReturnChangeActionExample;
import com.work.shop.oms.bean.GoodsReturnChangeExample;
import com.work.shop.oms.bean.GoodsReturnChangePageListExample;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.common.bean.GoodsReturnChangeBean;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.common.bean.GoodsReturnChangeVO;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.GoodsReturnChangeActionMapper;
import com.work.shop.oms.dao.GoodsReturnChangeMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.define.GoodsReturnChangePageListMapper;
import com.work.shop.oms.dao.define.OrderMapper;
import com.work.shop.oms.order.service.GoodsReturnChangeService;
import com.work.shop.oms.service.GoodsReturnChangePageService;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.PageHelper;

@Service
public class GoodsReturnChangePageServiceImpl implements  GoodsReturnChangePageService{
	
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
	
//	@Autowired
//	private ProductBarcodeListMapper productBarcodeListMapper;
//	
//	@Autowired
//	private ProductGoodsMapper productGoodsMapper;
	
	@Autowired
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	
/*	@Autowired
	private PrGroupPromotionMapper prGroupPromotionMapper;*/
	
	
	@Override
	public Paging getPageList(GoodsReturnChangeVO goodsReturnChangeVO,PageHelper helper) {
		GoodsReturnChangePageListExample example =queryList(goodsReturnChangeVO);
		example.or().limit(helper.getStart(),helper.getLimit());
		Paging paging = new Paging(goodsReturnChangePageListMapper.countByExample(example),
				goodsReturnChangePageListMapper.selectByExample(example));
		return paging;
	}
	

	public GoodsReturnChangePageListExample queryList(GoodsReturnChangeVO goodsReturnChangeVO){
		GoodsReturnChangePageListExample example = new GoodsReturnChangePageListExample();
		example.setOrderByClause(" grc.create desc ");
		GoodsReturnChangePageListExample.Criteria criteria = example.or();
		if(null!=goodsReturnChangeVO){
			/**
			 * newData三个月以内数据
			 * historyData历史数据
			 */
			if(StringUtil.isNotNull(goodsReturnChangeVO.getListDataType())){
					if(goodsReturnChangeVO.getListDataType().equals("historyData")){
						example.setHistoryData(true);	
					}else{
						example.setHistoryData(false);
					}
			}
			
			criteria.andSKU();
			if(StringUtil.isNotNull(goodsReturnChangeVO.getChannelType())&&!goodsReturnChangeVO.getChannelType().equals("-1")){
				criteria.andChannelTypeEqualTo(goodsReturnChangeVO.getChannelType());
			}
			
			if(StringUtil.isNotNull(goodsReturnChangeVO.getOrderSn())){
				criteria.andOrderSnEqualTo(goodsReturnChangeVO.getOrderSn());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getReturnchangeSn())){
				criteria.andReturnChangeSnEqualTo(goodsReturnChangeVO.getReturnchangeSn());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getReturnSn())){
				criteria.andReturnSnEqualTo(goodsReturnChangeVO.getReturnSn());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getReturnPaySn())){
				criteria.andReturnPaySnEqualTo(goodsReturnChangeVO.getReturnPaySn());
			}
			
			if(StringUtil.isNotNull(goodsReturnChangeVO.getUserId())){
				criteria.andUserIdEqualTo(goodsReturnChangeVO.getUserId());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getSkuSn())){
				criteria.andSkuSnEqualTo(goodsReturnChangeVO.getSkuSn());
			}
			if(null!=goodsReturnChangeVO.getReturnType()&&goodsReturnChangeVO.getReturnType()>=0){
				criteria.andReturnTypeEqualTo(goodsReturnChangeVO.getReturnType());
			}
			if(null!=goodsReturnChangeVO.getReason()&&goodsReturnChangeVO.getReason()>=0){
				criteria.andReasonEqualTo(goodsReturnChangeVO.getReason());
			}
			if(null!=goodsReturnChangeVO.getRedemption()&&goodsReturnChangeVO.getRedemption()>=0){
				criteria.andRedemptionEqualTo(goodsReturnChangeVO.getRedemption());
			}
			if(null!=goodsReturnChangeVO.getTagType()&&goodsReturnChangeVO.getTagType()>=0){
				criteria.andTagTypeEqualTo(goodsReturnChangeVO.getTagType());
			}
			if(null!=goodsReturnChangeVO.getExteriorType()&&goodsReturnChangeVO.getExteriorType()>=0){
				criteria.andExteriorTypeEqualTo(goodsReturnChangeVO.getExteriorType());
			}
			if(null!=goodsReturnChangeVO.getGiftType()&&goodsReturnChangeVO.getGiftType()>=0){
				criteria.andGiftTypeEqualTo(goodsReturnChangeVO.getGiftType());
			}
			if(null!=goodsReturnChangeVO.getStatus()&&goodsReturnChangeVO.getStatus()>=0){
				criteria.andStatusEqualTo(goodsReturnChangeVO.getStatus());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getContactName())){
				criteria.andContactNameEqualTo(goodsReturnChangeVO.getContactName());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getContactMobile())){
				criteria.andContactMobileEqualTo(goodsReturnChangeVO.getContactMobile());
			}
			if(null!=goodsReturnChangeVO.getStReturnSum()&&null!=goodsReturnChangeVO.getEnReturnSum()){
				criteria.andReturnSumBetween(goodsReturnChangeVO.getStReturnSum(), goodsReturnChangeVO.getEnReturnSum());
			}
			if(null!=goodsReturnChangeVO.getStReturnSum()){
				criteria.andReturnSumGreaterThanOrEqualTo(goodsReturnChangeVO.getStReturnSum());
			}
			if(null!=goodsReturnChangeVO.getEnReturnSum()){
				criteria.andReturnSumLessThanOrEqualTo(goodsReturnChangeVO.getEnReturnSum());
			}
			
			// 订单所属站点
			if (StringUtil.isNotEmpty(goodsReturnChangeVO.getSiteCode()) 
					&& !goodsReturnChangeVO.getSiteCode().equals("-1")) {
				criteria.andSiteCodeEqualTo(goodsReturnChangeVO.getSiteCode());
			} else {
				criteria.andSiteCodeIn(goodsReturnChangeVO.getSites());
			}
			// 订单所属渠道
			if (StringUtil.isNotEmpty(goodsReturnChangeVO.getChannelCode())
					&& !goodsReturnChangeVO.getChannelCode().equals("-1")) {
				criteria.andShopCodeEqualTo(goodsReturnChangeVO.getChannelCode());
			}
			if(StringUtil.isNotNull(goodsReturnChangeVO.getStartTime())&&StringUtil.isNotNull(goodsReturnChangeVO.getEndTime())){
				criteria.andCreateBetween(DateTimeUtils.parseStr(goodsReturnChangeVO.getStartTime()),DateTimeUtils.parseStr(goodsReturnChangeVO.getEndTime()));
			}else if(StringUtil.isNotNull(goodsReturnChangeVO.getStartTime())){
				criteria.andCreateGreaterThanOrEqualTo(DateTimeUtils.parseStr(goodsReturnChangeVO.getStartTime()));
			}else if(StringUtil.isNotNull(goodsReturnChangeVO.getEndTime())){
				criteria.andCreateLessThanOrEqualTo(DateTimeUtils.parseStr(goodsReturnChangeVO.getEndTime()));
			}
		}
		return example;
	}


	@Override
	public List<GoodsReturnChangeBean> getList(
			GoodsReturnChangeVO goodsReturnChangeVO) {
		GoodsReturnChangePageListExample example =queryList(goodsReturnChangeVO);
		return goodsReturnChangePageListMapper.selectByExample(example);
	}


	@Override
	public List<GoodsReturnChangeAction> findActionByReturnChangeSn(
			String returnChangeSn) {
		GoodsReturnChangeActionExample example=new GoodsReturnChangeActionExample();
		GoodsReturnChangeActionExample.Criteria criteria = example.or();
		criteria.andReturnchangeSnEqualTo(returnChangeSn);
		return goodsReturnChangeActionMapper.selectByExampleWithBLOBs(example);
	}


	@Override
	public GoodsReturnChange findGoodsReturnChangeById(Integer id) {

		return goodsReturnChangeMapper.selectByPrimaryKey(id);

	}


	@Override
	public ReturnInfo updateStatusBatch(Integer status, Integer[] ids,
			String userCode) {

		for(Integer id:ids){
			GoodsReturnChange goodsReturnChange=new GoodsReturnChange(); 	
		    goodsReturnChange=goodsReturnChangeMapper.selectByPrimaryKey(id);
		    ReturnInfo returnInfo=goodsReturnChangeService.updateStatus(status,id,goodsReturnChange.getOrderSn(),userCode,"更新");
			if(returnInfo.getIsOk()==0){
				return returnInfo;
			}
		}
		ReturnInfo returnInfo=new ReturnInfo();
		returnInfo.setIsOk(1);
		return returnInfo;
	
	}


	@Override
	public List<GoodsReturnChangeInfoVO> findGoodsReturnChangeBySn(
			String orderSn) {
		List<GoodsReturnChangeInfoVO>list=orderMapper.selectGoodsReturnChangeBySn(orderSn);
		for(GoodsReturnChangeInfoVO goodsReturnChangeInfoVO:list){
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
		return list;
	}

	@Override
	public List<GoodsReturnChange> findGoodsReturnChangeByOrderSn(String orderSn) {
		GoodsReturnChangeExample example = new GoodsReturnChangeExample();
		example.or().andOrderSnEqualTo(orderSn);
		return goodsReturnChangeMapper.selectByExample(example);
	}

}
