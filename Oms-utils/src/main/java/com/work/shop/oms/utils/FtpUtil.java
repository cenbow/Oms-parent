package com.work.shop.oms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;


public class FtpUtil {
	
	private static final String url = ConfigCenter.getProperty("ftp_url");
	private static final int port = Integer.valueOf(ConfigCenter.getProperty("ftp_port"));
	private static final String username = ConfigCenter.getProperty("ftp_username");
	private static final String password = ConfigCenter.getProperty("ftp_password");

	/**
	 * Description: 向FTP服务器上传文件
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	private static Logger log = Logger.getLogger(FtpUtil.class);

	/**
	 * 
	 * @param filename 文件名称
	 * @param input 写入流
	 * @param path
	 * @return
	 */
	public static HashMap<String, Object> uploadFile(String filename, InputStream input, String path) {
		// 初始表示上传失败
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("path", "");
		// 创建FTPClient对象
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.connect(url, port);
			// 登录ftp
			ftp.login(username, password);
			ftp.setFileType(2);
			// 看返回的值是不是230，如果是，表示登陆成功
			reply = ftp.getReplyCode();
			// 以2开头的返回值就会为真
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				resultMap.put("isok", false);
				return resultMap;
			}
			
			if(path == null || "".equals(path.trim())) {
				Calendar cal = Calendar.getInstance();
				String yearString = Integer.toString(cal.get(Calendar.YEAR));
				String monthString = Integer.toString(cal.get(Calendar.MONTH) + 1);
				ftp.makeDirectory(yearString);
				ftp.makeDirectory(yearString + "//" + monthString);
				path = yearString + "//" + monthString;
			} else {
				String[] subPath = path.split("/");
				String creatPath = subPath[0].trim();
				ftp.makeDirectory(creatPath);
				for(int i=1; i<subPath.length;++i) {
					if(!"".equals(subPath[i].trim())) {
						creatPath += "//"+subPath[i].trim();
						ftp.makeDirectory(creatPath);
					}
				}
				path = creatPath;
			}
			// 转到指定上传目录
			ftp.changeWorkingDirectory(path);
			
		//	ftp.setBufferSize(1024); 
			ftp.setControlEncoding("GBK");
			// 将上传文件存储到指定目录
			//input.available();
			
			//ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
			ftp.storeFile(filename, input);
			// 关闭输入流
			input.close();
			// 退出ftp
			ftp.logout();
			// 表示上传成功
			resultMap.put("path", path);
			resultMap.put("isok", true);

		} catch (IOException e) {
			resultMap.put("isok", false);
			log.error(ExceptionStackTraceUtil.getExceptionTrace(e));
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return resultMap;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public boolean downFile(String remotePath, String fileName, String localPath) {
		// 初始表示下载失败
		boolean success = false;
		// 创建FTPClient对象
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.connect(url, port);
			// 登录ftp
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			} // 转到指定下载目录
			ftp.changeWorkingDirectory(remotePath);
			// 列出该目录下所有文件
			FTPFile[] fs = ftp.listFiles();
			// 遍历所有文件，找到指定的文件
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					// 根据绝对路径初始化文件
					File localFile = new File(localPath + "/" + ff.getName());
					// 输出流
					OutputStream is = new FileOutputStream(localFile);
					// 下载文件
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			// 退出ftp
			ftp.logout();
			// 下载成功
			success = true;
		} catch (IOException e) {
			log.error(ExceptionStackTraceUtil.getExceptionTrace(e));
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public boolean downFtpFile(String remotePath, String fileName,
			String localPath, OutputStream is,String taskPath, File file ) {
		// 初始表示下载失败
		boolean success = false;
		// 创建FTPClient对象
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.connect(url, port);
			// 登录ftp
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			} // 转到指定下载目录
			ftp.changeWorkingDirectory(remotePath);
			// 列出该目录下所有文件
			FTPFile[] fs = ftp.listFiles();
			// 遍历所有文件，找到指定的文件
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					// 根据绝对路径初始化文件
					file = new File(localPath + "/" + ff.getName());
					// 输出流
					is = new FileOutputStream(file);
					// 下载文件
	
					ftp.retrieveFile(ff.getName(), is);
				//	is.close();
	
				}
			}
			// 退出ftp
			//ftp.logout();
			// 下载成功
			success = true;
		} catch (IOException e) {
			log.error(ExceptionStackTraceUtil.getExceptionTrace(e));
		} finally {
		/*	if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}*/
		}
		return success;
	}
	
	
	/**
	 * Description: 从FTP服务器下载文件
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 */
	public static void downFtpFile2(String fileName, String ftpPath, HttpServletResponse response) {
		OutputStream outputStream = null;
		FTPClient ftp = new FTPClient();
		try{
			if (StringUtil.isEmpty(ftpPath.trim())) {
				return ;
			}
			int reply;
			ftp.connect(url, port);
			// 登录ftp
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return ;
			} // 转到指定下载目录
			ftp.changeWorkingDirectory(ftpPath);
			// 列出该目录下所有文件
			FTPFile[] fs = ftp.listFiles();
			// 遍历所有文件，找到指定的文件
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					outputStream = response.getOutputStream();
					response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes()));
					response.setContentType("application/octet-stream;charset=GBK;");
					ftp.retrieveFile(ff.getName(), outputStream);
					break;
				}
			}
		}catch(Exception e){
			log.error("ftp下载失败 !" + ExceptionStackTraceUtil.getExceptionTrace(e));
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (ftp != null) {
					ftp.disconnect();
				}
			} catch (IOException e) {
				log.error("ftp下载关闭输出流异常 !" + ExceptionStackTraceUtil.getExceptionTrace(e));
			}
		}
	}
}
