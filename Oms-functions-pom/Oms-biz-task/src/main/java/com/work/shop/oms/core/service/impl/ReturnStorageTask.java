package com.work.shop.oms.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnExample;
import com.work.shop.oms.bean.OrderReturnGoods;
import com.work.shop.oms.bean.OrderReturnGoodsExample;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.dao.OrderReturnGoodsDetailMapper;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.orderReturn.service.OrderSettleService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.vo.SettleParamObj;
import com.work.shop.oms.vo.StorageGoods;
@Service("returnStorageTask")
public class ReturnStorageTask extends ATaskServiceProcess{

    private static Logger logger = LoggerFactory.getLogger(ReturnStorageTask.class);
    
    @Resource
    private OrderReturnMapper orderReturnMapper;
    @Resource
    private OrderSettleService orderSettleService;
    @Resource
    private OrderReturnGoodsDetailMapper orderReturnGoodsDetailMapper;
    @Resource
    private OrderReturnGoodsMapper orderReturnGoodsMapper;
    
    @Override
    public void initTaskConfig() {
        super.taskName = ConstantTask.TASK_JOB_TYPE_RETURN_STORAGE_TASK;
        super.initTaskConfig();
    }
    
    @Override
    public List<BaseTask> queryServiceData(List<String> orderIdList, Integer dataLimit) {
        logger.debug(taskName + "queryServiceData.dataLimit:" + dataLimit);
        List<String> returnSnList = orderReturnGoodsDetailMapper.getReturnToErpData(new HashMap<String, Object>());
        List<BaseTask> taskDatas = new ArrayList<BaseTask>();
        if(CollectionUtils.isNotEmpty(returnSnList)){
            for (String returnSn : returnSnList) {
                BaseTask obj = new BaseTask();
                obj.setOrderSn(returnSn);
                taskDatas.add(obj);
            }
        }
        return taskDatas;
    }

    @Override
    public ReturnTask executeTask(BaseTask obj) {
        logger.debug(taskName + "executeTask.BaseObj:" + JSON.toJSONString(obj));
        String returnSn = obj.getOrderSn();
        ReturnTask returnTask = new ReturnTask();
        returnTask.setIsOk(Constant.OS_NO);
        try {
            OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
            orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn).andToErpEqualTo(Constant.TOERP_NEED);
            List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
            if(CollectionUtils.isEmpty(orderReturnGoodsList)){
                returnTask.setMsg("退单关联商品为空！");
                return returnTask;
            }
            // 调用入库接口
            List<StorageGoods> storageList = new ArrayList<StorageGoods>();
            for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
                StorageGoods storageGoods = new StorageGoods();
                storageGoods.setId(orderReturnGoods.getId());
                storageGoods.setProdScanNum(orderReturnGoods.getGoodsReturnNumber());
                storageGoods.setCustomCode(orderReturnGoods.getCustomCode());
                storageGoods.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
                storageList.add(storageGoods);
            }
            
            SettleParamObj paramObj = new SettleParamObj();
            paramObj.setDealCode(StringUtils.trimToEmpty(returnSn));
            paramObj.setBussType(Constant.ERP_BUSS_TYPE_INPUT);
            paramObj.setStorageGoods(storageList);
            /*if(StringUtils.isNotBlank(goodsIds)){
                paramObj.setGoodsId(goodsIds);
            }*/
            logger.info("入库推送数据至erp，paramObj："+JSON.toJSONString(paramObj));
            ReturnInfo<String> returnVal = orderSettleService.inputReturnOrder(paramObj);
            logger.info("executeTask.returnSn:" + returnSn+",returnMessage:"+JSON.toJSONString(returnVal));
            for (StorageGoods storageGoods : storageList) {
                OrderReturnGoods updateGoods = new OrderReturnGoods();
                updateGoods.setId(storageGoods.getId());
                updateGoods.setToErp(Constant.TOERP_ALREADY);
                updateGoods.setStorageStatus(Constant.STORAGE_STATUS_DOING);
                orderReturnGoodsMapper.updateByPrimaryKeySelective(updateGoods);
            }
            returnTask.setIsOk(Constant.OS_YES);
            returnTask.setMsg("退单入库推送erp数据成功");
        } catch (Exception e) {
            logger.error("executeTask.returnSn:"+ returnSn +" ,错误信息:"+e.getMessage(),e);
            returnTask.setMsg("退单入库推送erp数据异常：" + e.getMessage());
        }
        return returnTask;
    }

}
