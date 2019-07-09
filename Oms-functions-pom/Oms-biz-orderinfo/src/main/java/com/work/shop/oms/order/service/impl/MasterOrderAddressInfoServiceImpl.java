package com.work.shop.oms.order.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.common.bean.MasterShip;
import com.work.shop.oms.config.service.SystemShippingService;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.order.service.MasterOrderAddressInfoService;
import com.work.shop.oms.utils.StringUtil;

/**
 * 主订单收货人信息创建
 * @author lemon
 *
 */
@Service
public class MasterOrderAddressInfoServiceImpl implements MasterOrderAddressInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	
	@Resource
	private SystemShippingService systemShippingService;
	
	/**
	 * 保存订单地址信息
	 * @param masterShip 订单地址信息
	 * @param masterOrderSn 订单号
	 */
	@Override
	public void insertMasterOrderAddressInfo(MasterShip masterShip, String masterOrderSn) {
		logger.info("订单[" + masterOrderSn + "]保存收货地址信息");
		MasterOrderAddressInfo addressInfo = new MasterOrderAddressInfo();
		// 订单编码
		addressInfo.setMasterOrderSn(masterOrderSn);
        // 生成时间
        addressInfo.setCreatTime(new Date());
		// 自提点编码
		addressInfo.setCacCode(masterShip.getCacCode());

		// 填充用户地址信息
        fillUserAddressInfo(masterShip, addressInfo);

        // 订单承诺发货天数
		addressInfo.setShippingDays(Byte.valueOf("" + masterShip.getShippingDays()));
        // 0. 不限 1.POS机支付; 2.现金支付;
		addressInfo.setChargeType((int) masterShip.getChargeType());

        // 发货仓地区ID
		addressInfo.setRegionId(masterShip.getRegionId() == null ? 0 : masterShip.getRegionId());
        // 运费付款方式 01:到付;02:自付
		addressInfo.setWayPaymentFreight(masterShip.getWayPaymentFreight());
        // 商品关联的发货仓库编码
		addressInfo.setDepotCode(masterShip.getDepotCode());
        // 用户身份证号码
		addressInfo.setUserCardNo(masterShip.getUserCardNo());
        // 用户身份证姓名
		addressInfo.setUserCardName(masterShip.getUserCardName());

        // 店长省份编码
		addressInfo.setProvinceCode(masterShip.getProvinceCode());
        // 店长市编码
		addressInfo.setCityCode(masterShip.getCity());
        // 店长区域编码
		addressInfo.setDistrictCode(masterShip.getDistrictCode());
        // 店长地区编码
		addressInfo.setAreaCode(masterShip.getAreaCode());
        // 收货地址
		addressInfo.setShippingAddress(masterShip.getShippingAddress());
		
		// 用户经纬度
		addressInfo.setLongitude(masterShip.getLongitude());
		addressInfo.setLatitude(masterShip.getLatitude());
		// 配送时间段
		addressInfo.setDeliveryTime(masterShip.getDeliveryTime());

		// 填充发票地址信息
        fillInvInfo(masterShip, addressInfo);
		
		// 订单收货人信息
		masterOrderAddressInfoMapper.insertSelective(addressInfo);
	}

    /**
     * 填充用户地址信息
     * @param masterShip 配送信息
     * @param addressInfo 地址信息
     */
	private void fillUserAddressInfo(MasterShip masterShip, MasterOrderAddressInfo addressInfo) {
        // 收货人的姓名
        addressInfo.setConsignee(masterShip.getConsignee());
        // 收货人的详细地址
        addressInfo.setAddress(masterShip.getAddress());
        // 收货人的国家
        addressInfo.setCountry(masterShip.getCountry());
        // 收货人的地区
        addressInfo.setDistrict(masterShip.getDistrict());
        // 收货人的城市
        addressInfo.setCity(masterShip.getCity());
        // 收货人的省份
        addressInfo.setProvince(masterShip.getProvince());
        // 收货人的最佳送货时间
        addressInfo.setBestTime(masterShip.getBestTime());
        // 收货人的电子邮件
        addressInfo.setEmail(masterShip.getEmail());
        // 收货人的手机号码
        if (StringUtil.isNotBlank(masterShip.getMobile())) {
            addressInfo.setMobile(masterShip.getMobile());
        }
        // 收货人的电话号码
        if (StringUtil.isNotBlank(masterShip.getTel())) {
            addressInfo.setTel(masterShip.getTel());
        }
        // 收货人店铺名称
        addressInfo.setUserShopName(masterShip.getShopName());
        // 收货人的邮政编码
        addressInfo.setZipcode(masterShip.getZipcode());
        // 收货人的地址的标志性建筑
        addressInfo.setSignBuilding(masterShip.getSignBuilding());
        // 物流配送方式
        SystemShipping shipping = systemShippingService.getSystemShipByShipCode(masterShip.getShippingCode());
        if (shipping != null) {
            // 用户选择的配送方式id
            addressInfo.setShippingId(shipping.getShippingId());
            // 配送方式的名称
            addressInfo.setShippingName(shipping.getShippingName());
        }
    }

    /**
     * 填充发票信息
     * @param masterShip 配送信息
     * @param addressInfo 地址信息
     */
	private void fillInvInfo(MasterShip masterShip, MasterOrderAddressInfo addressInfo) {
        // 发票收货人姓名
        addressInfo.setInvConsignee(masterShip.getInvConsignee());
        // 发票收货人手机号码
        addressInfo.setInvMobile(masterShip.getInvMobile());
        // 发票收货人地址
        addressInfo.setInvAddress(masterShip.getInvAddress());
    }
	
	/**
	 * 通过订单号获取订单地址信息
	 */
	public MasterOrderAddressInfo selectAddressInfo(String masterOrderSn) {
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			return null;
		}
		return masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
	}
}
