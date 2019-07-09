package com.work.shop.oms.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.work.shop.oms.common.utils.ExceptionStackTraceUtil;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.utils.FtpUtil;

@Controller
public class DownloadController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 文件下载
	 * 
	 * @param path
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "downloadFile.spmvc")
	public void download(String path, HttpServletResponse response,
			HttpServletRequest request) {
		logger.debug("下载系统文件" + path + "开始");
		String serverPath = "";
		InputStream fis = null;
		try {
			if (StringUtil.isEmpty(path.trim())) {
				return;
			}
			// 文件所在服务器绝对路径
//			String filePath = path.substring(0 ,path.lastIndexOf("/"));
			// 文件名
			String fileName = path.substring(path.lastIndexOf("/") + 1);
			serverPath = this.getDeployPath() + "/" + path;
			File file = new File(serverPath);
			fis = new BufferedInputStream(new FileInputStream(serverPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream;charset=GB2312;");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			logger.debug("下载系统文件" + path + "结束");
		} catch (IOException ex) {
			logger.debug("下载系统文件" + path + "异常：" + ExceptionStackTraceUtil.getExceptionTrace(ex));
		} finally {
			File file = new File(serverPath);
			if (file.isDirectory()) {
				for (File csvfile : file.listFiles()) {
					csvfile.delete();
				}
			}
		}
	}
	
	
	/**
	 * 模板文件下载
	 * 
	 * @param path
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "downloadTemp.spmvc")
	public void downloadTemp(String path, HttpServletResponse response,
			HttpServletRequest request) {
		logger.debug("下载文件路径：" + path);
		String serverPath = "";
		InputStream fis = null;
		try {
			if (StringUtil.isEmpty(path.trim())) {
				return;
			}
			path = java.net.URLDecoder.decode(path,"UTF-8");
//			path = new String(path.getBytes("iso8859-1"), "utf-8");
			// 文件所在服务器绝对路径
//			String filePath = path.substring(0 ,path.lastIndexOf("/"));
			// 文件名
			String fileName = path.substring(path.lastIndexOf("/") + 1);
			serverPath = this.getDeployPath() + "/" + path;
			File file = new File(serverPath);
			fis = new BufferedInputStream(new FileInputStream(serverPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
//			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream;charset=GB2312;");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			File file = new File(serverPath);
			if (file.isDirectory()) {
				for (File csvfile : file.listFiles()) {
					csvfile.delete();
				}
			}
		}
	}

	/**
	 * ftp文件下载
	 * @param path
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "downloadFtpFile.spmvc")
	public void downloadFtpFile(String path,String fileName, HttpServletResponse response,
			HttpServletRequest request) {
		
		logger.debug("downloadFtpFile .................  begin   String = ：       " + path  +";  fileName= "+fileName );
		
		try{
			if (StringUtil.isEmpty(path.trim())) {
				return;
			}
			FtpUtil.downFtpFile2(fileName, path, response);
			
			logger.debug("downloadFtpFile ................. end  " );
		}catch(Exception e){
			logger.error("ftp下载失败 !" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
	}
	
	

	
	
}
