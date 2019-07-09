package com.work.shop.oms.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.work.shop.oms.bean.MasterOrderGoodsDetail;
import com.work.shop.oms.bean.ProductBarcodeList;
import com.work.shop.oms.common.bean.OrderInfoUpdateInfo;
import com.work.shop.oms.service.OrderGoodsEditService;
import com.work.shop.oms.utils.ConfigCenter;

@Controller
@RequestMapping(value = "orderGoodsEdit")
public class OrderGoodsEditController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String BG_INTERFACE_URL = ConfigCenter.getProperty("bg_interface_url");
	
	@Resource
	private OrderGoodsEditService orderGoodsEditService;
	
	/**
	 * 根据主单号获取交货单号comb列表
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @return
	 */
	@RequestMapping(value = "getOrderSnCombStore")
	@ResponseBody
	public List<Map<String,String>> getOrderSnCombStore(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn){
		return orderGoodsEditService.getOrderSnCombStore(masterOrderSn);
	}
	
	/**
	 * 根据主单号获取去重的供应商comb列表
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @return
	 */
	@RequestMapping(value = "getSupplierCodeCombStore")
	@ResponseBody
	public List<Map<String,String>> getSupplierCodeCombStore(HttpServletRequest request,
			HttpServletResponse response,String masterOrderSn){
		return orderGoodsEditService.getSupplierCodeCombStore(masterOrderSn);
	}
	
	/**
	 * 根据商品码查询sku列表
	 * @param request
	 * @param response
	 * @param goodsSn
	 * @param type 默认0 查询全部商品sku款,1： 查询尺码sku款列表,2： 查询颜色码sku款列表
	 * @return
	 */
	@RequestMapping(value = "getProductBarcodeListByGoodsSn")
	@ResponseBody
	public List<ProductBarcodeList> getProductBarcodeListByGoodsSn(HttpServletRequest request, HttpServletResponse response, String goodsSn, String type){
		return orderGoodsEditService.getProductBarcodeListByGoodsSn(goodsSn, type);
	}
	
	/**
	 * 获取订单编辑商品列表
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param orderSn
	 * @return
	 */
	@RequestMapping(value = "getOrderGoodsEdit")
	@ResponseBody
	public Map getOrderGoodsEdit(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String orderSn){
		return orderGoodsEditService.getOrderGoodsEdit(masterOrderSn, orderSn);
	}
	
	/**
	 * 保存订单商品编辑信息
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @param orderInfoUpdateInfo
	 * @return
	 */
	@RequestMapping(value = "doSaveOrderGoodsEdit")
	@ResponseBody
	public Map doSaveOrderGoodsEdit(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,String orderSn,OrderInfoUpdateInfo orderInfoUpdateInfo){
		return orderGoodsEditService.doSaveOrderGoodsEdit(getLoginUser(request), masterOrderSn,orderSn, orderInfoUpdateInfo);
	}
	
	/**
	 * 根据渠道号和商品码查询商品/颜色列表/尺码列表
	 * @param request
	 * @param response
	 * @param goodsSn
	 * @param channelCode
	 * @return
	 */
	@RequestMapping(value = "searchGoods")
	@ResponseBody
	public Map searchGoods(HttpServletRequest request, HttpServletResponse response,
			String goodsSn,String channelCode, String userId){
		return orderGoodsEditService.searchGoods(goodsSn, channelCode, userId);
	}
	
	/**
	 * 根据商品货号和颜色值获取商品尺码列表
	 * @param goodSn
	 * @param colorCode
	 * @return
	 */
	@RequestMapping(value="getSizeListByColorCode")
	@ResponseBody
	public Map getSizeListByColorCode(String goodSn,String colorCode){
		return orderGoodsEditService.getSizeListByColorCode(goodSn, colorCode);
	}
	
	/**
	 * 根据商品货号和尺码值获取商品颜色列表
	 * @param goodSn
	 * @param sizeCode
	 * @return
	 */
	@RequestMapping(value="getColorListBySizeCode")
	@ResponseBody
	public Map getColorListBySizeCode(String goodSn,String sizeCode){
		return orderGoodsEditService.getColorListBySizeCode(goodSn, sizeCode);
	}
	
	/**
	 * 根据商品信息查询库存
	 * @param request
	 * @param response
	 * @param barcodeList
	 * @param shopCode
	 * @param type
	 * @return
	 */
	@RequestMapping(value="getCustomStock")
	@ResponseBody
	public Map getCustomStock(HttpServletRequest request, HttpServletResponse response,
			ProductBarcodeList barcodeList, String shopCode, int type){
		return orderGoodsEditService.getCustomStock(barcodeList, shopCode, type);
	}
	
	/**
	 * 添加订单商品
	 * @param request
	 * @param response
	 * @param masterOrderSn
	 * @return
	 */
	@RequestMapping(value="addOrderGoods")
	@ResponseBody
	public Map addOrderGoods(HttpServletRequest request, HttpServletResponse response,String masterOrderSn,
			String channelCode,MasterOrderGoodsDetail masterOrderGoodsDetail){
		return orderGoodsEditService.addOrderGoods(masterOrderSn,channelCode, masterOrderGoodsDetail);
	}
	
	/**
	 * 红包试算
	 * @param request
	 * @param response
	 * @param infoUpdateInfo
	 * @param bonusIds
	 * @return
	 */
	@RequestMapping(value="recalculate")
	@ResponseBody
	public Map recalculate(HttpServletRequest request, HttpServletResponse response, OrderInfoUpdateInfo infoUpdateInfo , String[] bonusIds){
		return orderGoodsEditService.recalculate(infoUpdateInfo ,bonusIds);
	}
	
	/**
	 * 根据面料ID获取颜色/尺码列表
	 * @param request
	 * @param response
	 * @param clsId
	 * @param queryMode 查询类型：0查询颜色，1查询尺码
	 * @return
	 */
	@RequestMapping(value="getPropertyListByClsId")
	@ResponseBody
	public List getPropertyListByClsId(HttpServletRequest request, HttpServletResponse response, String clsId,String queryMode){
		List list = new ArrayList();
		try{
			if("".equals(clsId)||clsId==null){
				return list;
			}
			//拼装请求参数
			JSONObject headObj = new JSONObject();
			headObj.put("ip", "127.0.0.1");
			if("0".equals(queryMode)){
				headObj.put("identifier", "2.6");
			}else if("1".equals(queryMode)){
				headObj.put("identifier", "2.4");
			}
			headObj.put("version", "1.0");
			headObj.put("timestamp", System.currentTimeMillis());
			JSONObject bodyObj = new JSONObject();
			bodyObj.put("clsId", Integer.parseInt(clsId));
			JSONObject paramObj = new JSONObject();
			paramObj.put("head", headObj);
			paramObj.put("body", bodyObj);
			//调用接口
			//创建连接
            URL url = new URL(BG_INTERFACE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(paramObj.toString());
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            logger.info(sb.toString());
            /*JSONObject returnObj = JSONObject.fromObject(sb.toString());
            JSONArray returnArray = JSONArray.fromObject(returnObj.get("body"));
            for (int i = 0; i < returnArray.size(); i++) {
            	JSONObject tempObj = (JSONObject) returnArray.get(i);
            	Map<String,String> map = new HashMap<String,String>();
            	map.put("id",String.valueOf(tempObj.get("id")));
            	map.put("name",(String)tempObj.get("name"));
                list.add(map);
            }*/
            reader.close();
            // 断开连接
            connection.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取是否验证库存数量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getIsVerifyStock")
	@ResponseBody
	public Map getIsVerifyStock(HttpServletRequest request, HttpServletResponse response){
		return orderGoodsEditService.getIsVerifyStock();
	}

}
