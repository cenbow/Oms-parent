package com.work.shop.oms.controller;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.work.shop.oms.bean.BatchDecodeList;
import com.work.shop.oms.bean.BatchDecodeListExample;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.dao.BatchDecodeListMapper;
import com.work.shop.oms.dao.MasterOrderAddressDetailMapper;
import com.work.shop.oms.service.BatchDecodeService;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.AdminUser;

@Controller
@RequestMapping(value = "batchDecode")
public class BatchDecodeController extends BaseController {
	@Resource
	private BatchDecodeService batchDecodeService;
	@Resource
	private BatchDecodeListMapper batchDecodeListMapper;
	@Resource
	private MasterOrderAddressDetailMapper masterOrderAddressDetailMapper;
	
	/**
	 * 从excel读取批量导入数据
	 * @param request
	 * @param myfile
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="doImport.spmvc", headers = "content-type=multipart/*")
	@ResponseBody
	public void doImport(HttpServletRequest request,HttpServletResponse response,@RequestParam MultipartFile myfile) throws Exception{
		Map map = batchDecodeService.doImport(getLoginUser(request),myfile,getIpAddr(request));
		String code = (String) map.get("code");
		String msg = (String) map.get("msg");
		if("0".equals(code)){
			writeMsgSucc(true, "导入成功",  response);
		}else{
			writeMsgSucc(false, msg,  response);
		}
	}
	
	/**
	 * 获取导入批次列表
	 * @param helper
	 * @return
	 */
	@RequestMapping(value="getBatchDecodeList")
	@ResponseBody
	public Paging getBatchDecodeList(PageHelper helper){
		return batchDecodeService.getBatchDecodeList(helper);
	}
	
	@RequestMapping(value="checkDownloadDecodeList")
	@ResponseBody
	public Map checkDownloadDecodeList(HttpServletRequest request,HttpServletResponse response,String billNo){
		return batchDecodeService.checkDownloadDecodeList(getLoginUser(request),billNo);
	}
	/**
	 * 下载解码后的联系方式
	 * @param request
	 * @param response
	 * @param billNo
	 */
	@RequestMapping(value="downloadDecodeList")
	public void downloadDecodeList(HttpServletRequest request,HttpServletResponse response,String billNo){
		try{
			//定义文件名
			String fileName = "联系方式批量解码清单_";
			//拼用户名
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				throw new Exception("登录失效！");
			}
			String userName = adminUser.getUserName();
			if(!"".equals(userName)||userName!=null){
				fileName+=userName+"_";
			}
			//拼上时间
			String now = DateTimeUtils.format(new Date(), DateTimeUtils.YYYYMMDDHHmmss);
			fileName+=now;
			fileName+= ".csv";//完整文件名
			
			//定义导出文件头部
			LinkedHashMap headNameMap = new LinkedHashMap();
			headNameMap.put("1", "订单号");
			headNameMap.put("2", "姓名");
			headNameMap.put("3", "联系方式");
			headNameMap.put("4", "详细地址");
			
			//获取要导出的数据
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("billNo", billNo);
			List<MasterOrderAddressInfo> dataList = masterOrderAddressDetailMapper.getBatchDecodeDoloadList(paramMap);
			List<Map> exportData = new ArrayList<Map>();
			for(MasterOrderAddressInfo vo : dataList){
				Map<String,String> row = new LinkedHashMap<String,String>();
				row.put("1", vo.getMasterOrderSn()+"\t");
				row.put("2", vo.getConsignee()+"\t");
				row.put("3", vo.getMobile()+"\t");
				row.put("4", vo.getAddress()+"\t");
				exportData.add(row);
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
	 * 下载订单号列表
	 * @param request
	 * @param response
	 * @param billNo
	 */
	@RequestMapping(value="downloadOrderSnList")
	public void downloadOrderSnList(HttpServletRequest request,HttpServletResponse response,String billNo){
		try{
			//定义文件名
			String fileName = "订单号清单_";
			//拼用户名
			AdminUser adminUser = getLoginUser(request);
			if(adminUser==null){
				throw new Exception("登录失效！");
			}
			String userName = adminUser.getUserName();
			if(!"".equals(userName)||userName!=null){
				fileName+=userName+"_";
			}
			//拼上时间
			String now = DateTimeUtils.format(new Date(), DateTimeUtils.YYYYMMDDHHmmss);
			fileName+=now;
			fileName+= ".csv";//完整文件名
			
			//定义导出文件头部
			LinkedHashMap headNameMap = new LinkedHashMap();
			headNameMap.put("1", "订单号");
			
			//获取要导出的数据
			BatchDecodeListExample example = new BatchDecodeListExample();
			example.or().andBillNoEqualTo(billNo);
			List<BatchDecodeList> dataList = batchDecodeListMapper.selectByExample(example);
			List<Map> exportData = new ArrayList<Map>();
			for(BatchDecodeList vo : dataList){
				Map<String,String> row = new LinkedHashMap<String,String>();
				row.put("1", vo.getMasterOrderSn()+"\t");
				exportData.add(row);
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

}
