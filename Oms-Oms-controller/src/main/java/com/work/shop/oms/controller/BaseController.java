package com.work.shop.oms.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.utils.PageHelper;

public class BaseController {

	public static final String RESULT_YES = "200"; 

	public static final String RESULT_NO = "500";
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	protected void writeObject(Object object,HttpServletResponse response) {
		try {
			String jsonObject = JSONObject.toJSONString(object);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(jsonObject);
			response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void writeMsgSucc(boolean code,String msg, HttpServletResponse response) throws Exception {
		try {
			response.setContentType("text/html;charset=utf-8"); 
			response.getWriter().write("{\"success\":\""+code+"\",\"msg\":\""+ msg + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void writeMsgSucc(boolean code, String path, String fileName, HttpServletResponse response) throws Exception {
		try {
			response.setContentType("text/html;charset=utf-8"); 
			response.getWriter().write("{    \"success\":\""+code+"\",\"path\":\""+ path +"\",\"fileName\":\""+ fileName +"\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void writeJson(Paging paging, HttpServletResponse response) throws IOException {	
		write(PageHelper.toJsonString(paging), response);
	}

	protected void write(String outputStr, HttpServletResponse response) {
		response.setContentType("application/json;charset=utf-8");
		try {
			response.getWriter().write(outputStr);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���JSON�ַ�
	 * 
	 * @param response
	 * @param obj
	 */
	public static void outPrintJson(HttpServletResponse response, Object obj) {
		response.setCharacterEncoding("UTF-8");
		response.addHeader("CacheControl", "no-cache");
		response.addHeader("Content-Type", "application/json");
		PrintWriter out = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			out = response.getWriter();
			String jsonStr = mapper.writeValueAsString(obj);
			out.print(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
		}
	}
	
	public void exportCsvFile1(HttpServletRequest request,HttpServletResponse response, StringBuffer sb,String fileName) throws Exception{
		PrintWriter printer = initPrintWriter(fileName, response);
		writeData(printer, sb.toString(), true);
	}
	
	/**
	 * ��ʼ�����
	 * @param fileName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected PrintWriter initPrintWriter(String fileName,HttpServletResponse response) throws Exception{
			response.setContentType("application/octet-stream;charset=GBK");
			response.setHeader("Content-Disposition","attachment;  filename="+fileName);
			PrintWriter out = response.getWriter();//���ڵ�һ���ǻ�������� 
			return out;
	}
	
	/**
	 * ������
	 * @param writer
	 * @param data
	 * @param isEnd
	 * @throws Exception
	 */
	protected void writeData(PrintWriter writer ,String data , boolean isEnd) throws Exception{
		writer.write(data);
		writer.flush();
		if(isEnd){
			writer.close();
		}
	}

	/**
	 * ȡ��Ŀ��ַ
	 * 
	 **/
	public String getDeployPath() {
		String path = "";
		try {
			path = this.getClass().getResource("/").getPath(); // �õ���Ŀ����WEB-INF/classes/·��
			path = path.substring(0, path.indexOf("WEB-INF/classes"));// ��·���ַ���ȡ������·��
			path = path.replace("%20", " ");
			File file = new File(path);
			if (!file.exists()) {
				path = path.substring(1, path.length());
			} else {
				path = file.getPath();
			}	
		} catch (Exception e) {
			logger.error("��ȡ��Ŀ·���쳣��" + e.getMessage());
		}
		return path;
	}
}
