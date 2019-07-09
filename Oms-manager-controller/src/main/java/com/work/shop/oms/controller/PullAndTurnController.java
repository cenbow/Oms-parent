package com.work.shop.oms.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.ChannelDeliveryReturn;
import com.work.shop.oms.common.bean.PullAndTurn;
import com.work.shop.oms.orderget.service.ChannelManagerService;


@Controller
@RequestMapping(value = "pullAndTurn")
public class PullAndTurnController extends BaseController {
	
	private final static Logger logger = Logger.getLogger(PullAndTurnController.class);
	
	//@Resource(name = "channelManagerService")
	private ChannelManagerService channelManagerService;
	
	@RequestMapping(value = "/downloadTrades.ac")
	@ResponseBody
	public void downloadTrades(
			@RequestParam(value="codeFile") MultipartFile codeFile,
			HttpServletRequest request,HttpServletResponse response,
			String dealCodes,
			String type)
			throws UnsupportedEncodingException {
		logger.debug("downloadTrades start: codeFile:"+codeFile.getName()+" dealCodes:"+dealCodes+" type:"+type);
		Set<String> codes = new HashSet<String>(0);

		if(StringUtils.isNotBlank(dealCodes)) {
			codes = pickCodes(dealCodes.trim());
		}
		
		if(codeFile != null) {
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			try {
				inputStreamReader = new InputStreamReader(codeFile.getInputStream());
				reader = new BufferedReader(inputStreamReader);
				
				String line = null;
				
				while((line = reader.readLine()) != null) {
					codes.add(line.trim());
				}
			} catch (IOException e) {
				write("{success:false,data:'下载失败！'}",response);
				return;
			} finally {
				try {
					if(reader != null) {
						reader.close();
					}
					if(inputStreamReader != null) {
						inputStreamReader.close();
					}
				} catch (IOException e) { }
			}
		}
		
		try {
			download(codes,type);
		} catch (Exception e) {
			write("{success:false,data:'下载失败！'}",response);
			return;
		}
		write("{success:true,data:'下载成功！'}",response);
		logger.debug("downloadTrades end: codeFile:"+codeFile.getName()+" dealCodes:"+dealCodes+" type:"+type);
	}
	
	private void download(Set<String> codes, String shortName) {
//		channelManagerService.downloadChannelOrder(codes, shortName);
	}

	private Set<String> pickCodes(String codes) {
		String[] cds = codes.split(",");
		Set<String> rs = new HashSet<String>(0);
		for(String cd : cds) {
			if(StringUtils.isNotBlank(cd)) {
				rs.add(cd);
			}
		}
		return rs;
	}
	
	/**
	 * 转单
	 * @param channelOrderSns
	 * @param channelCode
	 */
	@RequestMapping("/convertOrder.ac")
	@ResponseBody
	public void convertOrder(String channelOrderSns,String channelCode){
		if(channelOrderSns.length()>0){
			String [] arr = channelOrderSns.split(",");
			List<String> orderIdList = Arrays.asList(arr);
			channelManagerService.convertChannelOrder(orderIdList, channelCode);
		}
	}
	
	/**
	 * 查询拉单
	 * @param request
	 * @param response
	 * @param pullAndTurn
	 */
	@RequestMapping("/channelOrderList.ac")
	public @ResponseBody void channelOrderList(HttpServletRequest request,HttpServletResponse response,PullAndTurn pullAndTurn){
		Paging paging = new Paging();
		try {
			paging = channelManagerService.channelOrderList(pullAndTurn);
			writeJson(paging, response);
		} catch (Exception e) {
			logger.error("转单查询数据失败"+e);
		}
	}
	
	/**
	 * 转单---同步
	 * @param request
	 * @param response
	 * @param channelOrderSns
	 * @param channelCode
	 */
	@RequestMapping("/deliveryCallOrder.ac")
	public void deliveryCallOrder(HttpServletRequest request, HttpServletResponse response,String channelOrderSns,String channelCode){
		
		logger.info("deliveryCallOrder.ac.channelOrderSns:"+channelOrderSns);
		List<ChannelDeliveryReturn> returnList = null;
		try{
			if(channelOrderSns.length()>0){
				List<String> orderIdList = Arrays.asList(channelOrderSns.split(","));
				if(CollectionUtils.isEmpty(orderIdList)){
					return ;
				}
//				returnList = channelManagerService.deliveryChannelOrder(orderIdList, channelCode, false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		outPrintJson(response,returnList);
	}	
	
	/**
	 * 转单---强制同步
	 * @param request
	 * @param response
	 * @param channelOrderSns
	 * @param channelCode
	 */
	@RequestMapping("/enforceDeliveryOrder.ac")
	public void enforceDeliveryOrder(HttpServletRequest request, HttpServletResponse response,String channelOrderSns,String channelCode){
		
		logger.info("enforceDeliveryOrder.ac.channelOrderSns:"+channelOrderSns);
		if(channelOrderSns.length()>0){
			List<String> orderIdList = Arrays.asList(channelOrderSns.split(","));
			if(CollectionUtils.isEmpty(orderIdList)){
				return ;
			}
//			channelManagerService.deliveryChannelOrder(orderIdList, channelCode, true);
		}
	}	
}
