package com.work.shop.oms.express.jms.listener;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.work.shop.oms.api.express.service.OrderExpressPullService;
import com.work.shop.oms.api.express.service.OrderExpressService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.express.service.OrderExpressTracingService;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.OrderExpressTracingKey;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单发货快递消费处理
 * @author lemon
 */
@Service
public class OrderExpressPullConsumer implements MessageListener{

	private final static Logger logger = Logger.getLogger(OrderExpressPullConsumer.class);

	@Resource
	private OrderExpressTracingService orderExpressTracingService;

	@Resource
	private OrderExpressPullService orderExpressPullService;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;

	@Resource
	private SystemShippingMapper systemShippingMapper;

    /**
     * 消费处理
     * @param text
     */
	@Override
	public void onMessage(Message text) {
		TextMessage textMessage = (TextMessage) text;
		String masterOrderSn = "";
		try{
			masterOrderSn = URLDecoder.decode(textMessage.getText() , "UTF-8");
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (master == null) {
				return ;
			}
			MasterOrderAddressInfo oai = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			ReturnInfo<List<OrderDepotShip>> info = orderExpressPullService.selectEffectiveShip(masterOrderSn);
			if (info.getIsOk() == Constant.OS_NO || StringUtil.isListNull(info.getData())) {
				return ;
			}

			List<OrderDepotShip> orderShipList = info.getData();
			for (OrderDepotShip orderShip : orderShipList) {
				SystemShipping shipping = systemShippingMapper.selectByPrimaryKey(orderShip.getShippingId());
				String depotCode = orderShip.getDepotCode();
				if (StringUtil.isNotEmpty(orderShip.getInvoiceNo()) && null != orderShip.getShippingId()) {
					String trackNo = orderShip.getInvoiceNo();
					OrderExpressTracingKey expressTracingKey = new OrderExpressTracingKey();
					expressTracingKey.setDeliverySn(orderShip.getOrderSn());
					expressTracingKey.setDepotCode(depotCode);
					expressTracingKey.setOrderSn(masterOrderSn);
					expressTracingKey.setTrackno(trackNo);
					OrderExpressTracing oet = orderExpressTracingService.selectByPrimaryKey(expressTracingKey);
					if (null == oet) {
						OrderExpressTracing e=new OrderExpressTracing();
						e.setAddress(oai.getAddress());
						e.setAddTime(new Date());
						e.setCity(oai.getCity());
						e.setCityStr(orderExpressTracingService.selectNameByPrimaryKey(oai.getCity()));
						e.setCompanyCode(shipping.getShippingCode());
						e.setConfirmTime(master.getConfirmTime());
						e.setConsignee(oai.getConsignee());
						e.setCountry(Short.valueOf(oai.getCountry()));
						e.setProvinceStr(orderExpressTracingService.selectNameByPrimaryKey(oai.getProvince()));
						e.setDistrictStr((orderExpressTracingService.selectNameByPrimaryKey(oai.getDistrict())));
						e.setDeliveryTime(orderShip.getDeliveryTime());
						e.setDepotCode(depotCode);
						e.setDistrict(oai.getDistrict());
						e.setExpressStatus(0F);
						e.setFetchCount((byte) 0);
						e.setFetchFlag((byte) 0);
						e.setOrderFrom(master.getOrderFrom());
						e.setOrderSn(masterOrderSn);
                        // 订单
						e.setOrderType(1);
						e.setProvince(oai.getProvince());
						e.setTrackno(trackNo);
						e.setDeliverySn(orderShip.getOrderSn());
						orderExpressTracingService.insertSelective(e);
					} else {
						oet.setTrackno(trackNo);
						oet.setCompanyCode(shipping.getShippingCode());
						orderExpressTracingService.updateByPrimaryKeySelective(oet);
					}
				}
			}
		} catch (Exception e) {
			logger.error("订单" + masterOrderSn + e.getMessage(),e);
		}
	}

}
