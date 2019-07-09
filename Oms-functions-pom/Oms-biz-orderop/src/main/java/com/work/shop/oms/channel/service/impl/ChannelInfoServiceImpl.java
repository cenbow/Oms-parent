package com.work.shop.oms.channel.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.bean.bgchanneldb.ChannelShopExample;
import com.work.shop.oms.bean.bgchanneldb.CsChannelInfo;
import com.work.shop.oms.bean.bgchanneldb.CsChannelInfoExample;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.ChannelShopMapper;
import com.work.shop.oms.dao.CsChannelInfoMapper;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.OrderAttributeUtil;
import com.work.shop.oms.utils.StringUtil;

@Service
public class ChannelInfoServiceImpl implements ChannelInfoService {

	private static Logger logger = Logger.getLogger(ChannelInfoServiceImpl.class);

	@Resource
	private RedisClient redisClient;
	@Resource
	private CsChannelInfoMapper channelInfoMapper;
	@Resource
	private ChannelShopMapper channelShopMapper;
	
	private static final String SHOP_CODE_PREFIX = "OS_SHOP_CODE_";
	private static final String CHANNEL_CODE_PREFIX = "OS_CHANNEL_CODE_";

	public ReturnInfo<List<CsChannelInfo>> findChannelInfoByChannelType(Short channelType) {
		ReturnInfo<List<CsChannelInfo>> info = new ReturnInfo<List<CsChannelInfo>>(Constant.OS_NO);
		List<CsChannelInfo> infos = new ArrayList<CsChannelInfo>();
		try {
			if (channelType == null) {
				channelType = 0;
			}
			CsChannelInfoExample siteInfoExample = new CsChannelInfoExample();
//			siteInfoExample.or().andSiteTypeEqualTo(channelType);
			siteInfoExample.or().andChannelTypeEqualTo((short)0);
			infos = channelInfoMapper.selectByExample(siteInfoExample);
			info.setIsOk(Constant.OS_YES);
			info.setData(infos);
		} catch (Exception e) {
			logger.error("根据渠道类型[" + channelType + "]获取渠道信息列表失败：" + e.getMessage(), e);
			info.setMessage("根据渠道类型[" + channelType + "]获取渠道信息列表失败：" + e.getMessage());
		}
		return info;
	}

	public ReturnInfo<List<ChannelShop>> findChannelShopByChannelCode(String siteCode) {
		ReturnInfo<List<ChannelShop>> info = new ReturnInfo<List<ChannelShop>>(Constant.OS_NO);
		List<ChannelShop> infos = new ArrayList<ChannelShop>();
		try {
			if (StringUtil.isTrimEmpty(siteCode)) {
				info.setMessage("[siteCode]渠道编码不能为空");
				return info;
			}
			ChannelShopExample infoExample = new ChannelShopExample();
			infoExample.or().andChannelCodeEqualTo(siteCode).andShopStatusEqualTo((byte)1);
			infos = channelShopMapper.selectByExample(infoExample);
			info.setData(infos);
			info.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			logger.error("根据站点编码[" + siteCode + "]获取渠道店铺信息列表失败：" + e.getMessage(), e);
			info.setMessage("根据站点编码[" + siteCode + "]获取渠道店铺信息列表失败：" + e.getMessage());
		}
		return info;
	}

	public ReturnInfo<ChannelShop> findShopInfoByShopCode(String shopCode) {
		ReturnInfo<ChannelShop> info = new ReturnInfo<ChannelShop>(Constant.OS_NO);
		try {
			ChannelShopExample shopExample = new ChannelShopExample();
			shopExample.or().andShopCodeEqualTo(shopCode);
			List<ChannelShop> shops = channelShopMapper.selectByExample(shopExample);
			if (StringUtil.isListNull(shops)) {
				info.setMessage("查询结果为空！");
				return info;
			}
			info.setData(shops.get(0));
			info.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			logger.error("根据店铺编码[" + shopCode + "]获取渠道店铺信息列表失败：" + e.getMessage(), e);
			info.setMessage("根据店铺编码[" + shopCode + "]获取渠道店铺信息列表失败：" + e.getMessage());
		}
		return info;
	}

	/**
	 * 根据订单店铺号获取店铺渠道号
	 *   获取订单店铺号以及将店铺信息放入redis中
	 * @param orderFrom
	 * @return
	 */
	public String getChannelCode(String shopCode) {
		String siteCode = "";
		String redisChannelCodeKey = CHANNEL_CODE_PREFIX + shopCode;
		redisClient.del(redisChannelCodeKey);
		String redisChannelCode = redisClient.get(redisChannelCodeKey);
		if (StringUtil.isEmpty(redisChannelCode)) {
			try {
				ReturnInfo<ChannelShop> info = findShopInfoByShopCode(shopCode);
				if (info.getIsOk() == Constant.OS_NO) {
					logger.error("根据店铺号[" + shopCode + "]获取店铺信息:" + info.getMessage());
					return null;
				}
				ChannelShop channelShop = info.getData();
				// 店铺属于分销渠道使用该店铺的父店铺号
				if (channelShop != null && StringUtil.isNotNull(channelShop.getChannelCode())) {
					siteCode = channelShop.getChannelCode();
				}
				if (StringUtil.isNotEmpty(siteCode)) {
					redisClient.set(redisChannelCodeKey, siteCode);
				}
			} catch (Exception e) {
				logger.error("根据店铺号[" + shopCode + "]获取店铺信息异常" + e.getMessage(), e);
			}
		} else {
			siteCode = redisChannelCode;
		}
		return siteCode;
	}

	@Override
	public ReturnInfo<CsChannelInfo> findSiteInfoBySiteCode(String siteCode) {
		ReturnInfo<CsChannelInfo> info = new ReturnInfo<CsChannelInfo>(Constant.OS_NO);
		List<CsChannelInfo> infos = new ArrayList<CsChannelInfo>();
		try {
			if (StringUtil.isTrimEmpty(siteCode)) {
				info.setMessage("[siteCode]渠道编码不能为空");
				return info;
			}
			CsChannelInfoExample siteInfoExample = new CsChannelInfoExample();
			siteInfoExample.or().andChanelCodeEqualTo(siteCode);
			infos = channelInfoMapper.selectByExample(siteInfoExample);
			if (StringUtil.isListNotNull(infos)) {
				info.setData(infos.get(0));
			}
			info.setIsOk(Constant.OS_YES);
		} catch (Exception e) {
			logger.error("根据站点编码[" + siteCode + "]获取站点信息失败：" + e.getMessage(), e);
			info.setMessage("根据站点编码[" + siteCode + "]获取站点信息失败：" + e.getMessage());
		}
		return info;
	}

	/**
	 * 根据订单店铺号和订单referer 获取库存目标店铺号
	 *   获取订单店铺占用库存的店铺号以及将店铺信息放入redis中
	 * @param orderFrom
	 * @param referer
	 * @return
	 */
	public String getShopCode(String shopCode, Integer source) {
		String targetChannel = shopCode;
		if (OrderAttributeUtil.isPosOrder(source)) {//全流通
			targetChannel = null;
		} else {
			String redisShopCodeKey = SHOP_CODE_PREFIX + shopCode;
			String redisChannelCodeKey = CHANNEL_CODE_PREFIX + shopCode;
			String targetShopCode = redisClient.get(redisShopCodeKey);
			String channelCode = redisClient.get(redisChannelCodeKey);
			if (StringUtil.isEmpty(targetShopCode) || StringUtil.isEmpty(channelCode)) {
				ReturnInfo<ChannelShop> info = findShopInfoByShopCode(shopCode);
				if (info.getIsOk() == Constant.OS_NO) {
					logger.error("根据店铺号[" + shopCode + "]获取店铺信息:" + info.getMessage());
					return null;
				} else {
					if (info.getData() == null) {
						logger.error("根据店铺号[" + shopCode + "]获取店铺信息:查询数据为空");
						return null;
					}
					ChannelShop channelShop = info.getData();
					// 店铺属于分销渠道使用该店铺的父店铺号
					targetShopCode = channelShop.getChannelCode();
					channelCode = channelShop.getChannelCode();
					if (StringUtil.isNotEmpty(targetShopCode)) {
						redisClient.set(redisShopCodeKey, targetShopCode);
						targetChannel = targetShopCode;
					}
					if (StringUtil.isNotEmpty(channelCode)) {
						redisClient.set(redisChannelCodeKey, channelCode);
					}
				}
			} else {
				targetChannel = targetShopCode;
			}
		}
		return targetChannel;
	}
}
