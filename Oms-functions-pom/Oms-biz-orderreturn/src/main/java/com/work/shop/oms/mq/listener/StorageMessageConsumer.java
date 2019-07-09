package com.work.shop.oms.mq.listener;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.param.bean.StorageMessageReturn;
import com.work.shop.oms.bean.OrderReturnGoods;
import com.work.shop.oms.bean.OrderReturnGoodsExample;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.utils.Constant;

@Service("storageMessageConsumer")
public class StorageMessageConsumer extends Consumer{

    private static Logger logger = Logger.getLogger(StorageMessageConsumer.class);
    
    @Resource
    private OrderReturnGoodsMapper orderReturnGoodsMapper;
    
    @Override
    public String getDATA(String text) {
        logger.info("storageMessageConsumer,erp处理退单入库数据返回信息："+text);
        try {
            StorageMessageReturn storageMessageReturn = JSON.parseObject(text, StorageMessageReturn.class);
            OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
            orderReturnGoodsExample.or().andStorageTimeStampEqualTo(storageMessageReturn.getReturnId());
            List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
            if(CollectionUtils.isNotEmpty(orderReturnGoodsList)){
                for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
                    OrderReturnGoods updateGoods = new OrderReturnGoods();
                    updateGoods.setId(orderReturnGoods.getId());
                    updateGoods.setStorageTreateTime(storageMessageReturn.getCurrentTime());
                    if(StringUtils.isEmpty(storageMessageReturn.getErpEntryException())){
                        updateGoods.setStorageStatus(Constant.STORAGE_STATUS_FINISHED);
                    }else{
                        updateGoods.setStorageStatus(Constant.STORAGE_STATUS_FAILED);
                    }
                    orderReturnGoodsMapper.updateByPrimaryKeySelective(updateGoods);
                }
            }
        } catch (Exception e) {
            logger.error("处理erp返回退单入库数据信息出错！"+e,e);
        }
        return null;
    }

}
