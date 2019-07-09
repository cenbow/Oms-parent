package com.work.shop.oms.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.united.client.dataobject.User;
import com.work.shop.united.client.facade.UserStore;

public class BaseController {

	public static final String RESULT_YES = "200"; 

	public static final String RESULT_NO = "500";
	
	public static final String orderListPageNo = "order_list";
	
	public static final String orderInfoPageNo = "order_info_";
	
	public static final String sonOrderResourceHead = "order_info_distribute_";
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);  
	}

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
	 * 输出JSON字符串
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
	 * 初始化输出
	 * @param fileName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected PrintWriter initPrintWriter(String fileName,HttpServletResponse response) throws Exception{
			response.setContentType("application/octet-stream;charset=GBK");
			response.setHeader("Content-Disposition","attachment;  filename="+fileName);
			PrintWriter out = response.getWriter();//放在第一句是会出现乱码 
			return out;
	}
	
	/**
	 * 输出数据
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
	 * 取项目地址
	 * 
	 **/
	public String getDeployPath() {
		String path = "";
		try {
			path = this.getClass().getResource("/").getPath(); // 得到项目工程WEB-INF/classes/路径
			path = path.substring(0, path.indexOf("WEB-INF/classes"));// 从路径字符串中取出工程路劲
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
	
	/**
	 * 获取当前用户对象
	 * */
	public String  getUserName(HttpServletRequest request){
		String userName = "";
		if(UserStore.get(request)!=null){
			userName = UserStore.get(request).getUserName()== null ? "" : UserStore.get(request).getUserName();
		}
		return userName;
	}
	
	/**
	 * 获取登录用户
	 * @param request
	 * @return
	 */
	public AdminUser getLoginUser(HttpServletRequest request){
		AdminUser adminUser = new AdminUser();
		try{
			if(UserStore.get(request)!=null){
				User user = UserStore.get(request);
				if (user != null) {
					adminUser = new AdminUser();
					adminUser.setUserId(user.getId() + "");
					adminUser.setUserName(user.getEmpNo());
//					HttpSession session = request.getSession();
//					int supRole = (Integer) session.getAttribute(Constant.SESSION_UNLOCK_KEY);
//					if (1 == supRole) {
//						adminUser.setSupRole("sup");
//					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return adminUser;
	}

	
	public static final String ftpRootPath = ConfigCenter.getProperty("ftp_path");
	
	public static final String tempFileFolder = "tempFileFolder";
	
	public static String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }
	
}
