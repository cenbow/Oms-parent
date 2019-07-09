package com.work.shop.oms.controller;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;





import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderBillVO;
import com.work.shop.oms.service.BfbRefundSettlementService;
import com.work.shop.oms.utils.PageHelper;

@Controller
@RequestMapping(value = "bfbRefundSettlement")
public class BfbRefundSettlementController extends BaseController {
	@Resource
	private BfbRefundSettlementService bfbRefundSettlementService;
	
	/**
	 * 邦付宝退款结算：邦付宝退款批次列表查询
	 * @param vo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getBfbRefundSettlementList")
	@ResponseBody
	public Paging getBfbRefundSettlementList(OrderBillVO vo,PageHelper helper){
		return bfbRefundSettlementService.getBfbRefundSettlementList(vo,helper);
	}
	
	/**
	 * 邦付宝退款结算：废弃选中的结算批次
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="delSettlement")
	@ResponseBody
	public Map delSettlement(HttpServletRequest request,String ids){
		return bfbRefundSettlementService.delSettlement(ids,request);
	}
	
	/**
	 * 邦付宝退款结算：下载指定调整单号的结算清单
	 * @param request
	 * @param response
	 * @param billNo
	 * @return
	 */
	@RequestMapping(value="downloadRecord")
	@ResponseBody
	public void downloadRecord(HttpServletRequest request,HttpServletResponse response,String billNo){
		try{
			Map<String,Object> map = bfbRefundSettlementService.downloadRecord(request, response, billNo);
			String fileName = ".csv";//文件名
			LinkedHashMap headNameMap = new LinkedHashMap();//头部名称
			List<Map> exportData = new ArrayList<Map>();//数据
			if(map!=null){
				fileName = (String) map.get("fileName");
				headNameMap = (LinkedHashMap) map.get("headNameMap");
				exportData = (List<Map>) map.get("exportData");
			}
			response.reset();
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition","attachment; filename=" + fileName +".csv");
            response.setContentType("application/octet-stream;charset=gbk");
            
			OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(),"GBK");
        	BufferedWriter bw = new BufferedWriter(osw);
        	// 写入文件头部  
            for (Iterator propertyIterator = headNameMap.entrySet().iterator(); propertyIterator.hasNext();) {  
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();  
                bw.write("\""+ propertyEntry.getValue().toString() + "\"");  
                if (propertyIterator.hasNext()) {  
                	bw.write(",");  
                }  
            }
            //换行
            bw.newLine();
            //写数据
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {   
                LinkedHashMap row = (LinkedHashMap) iterator.next();
                for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {    
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next(); 
                    bw.write("\""+propertyEntry.getValue().toString() + "\"");    
                   if (propertyIterator.hasNext()) {    
                	   bw.write(",");    
                    }    
                }    
                if (iterator.hasNext()) {    
                	bw.newLine();    
                }    
            }
            bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 邦付宝退款结算：获取指定调整单号的结算清单列表
	 * @param billNo
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getBfbRefSetDetailList")
	@ResponseBody
	public Paging getBfbRefSetDetailList(String billNo,PageHelper helper){
		return bfbRefundSettlementService.getBfbRefSetDetailList(billNo,helper);
	}
	
	/**
	 * 邦付宝退款结算：生成调整单
	 * @param request
	 * @param filepath
	 * @param note
	 * @return
	 */
	@RequestMapping(value="doImport.spmvc", headers = "content-type=multipart/*")
	@ResponseBody
	public Map doImport(HttpServletRequest request,@RequestParam MultipartFile myfile,String note){
		return bfbRefundSettlementService.doImport(request,myfile,note);
	}
	
	/**
	 * 邦付宝退款结算：执行选中的调整单
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="doExecute")
	@ResponseBody
	public Map doExecute(HttpServletRequest request,String ids){
		return bfbRefundSettlementService.doExecute(request,ids);
	}

}
