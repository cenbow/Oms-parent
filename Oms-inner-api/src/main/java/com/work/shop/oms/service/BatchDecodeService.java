package com.work.shop.oms.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.AdminUser;

public interface BatchDecodeService {
	/**
	 * 从excel读取批量导入数据
	 * @param adminUser
	 * @param myfile
	 * @param ip
	 * @return
	 */
	public Map doImport(AdminUser adminUser,MultipartFile myfile,String ip);
	
	/**
	 * 获取导入批次列表
	 * @param helper
	 * @return
	 */
	public Paging getBatchDecodeList(PageHelper helper);
	
	/**
	 * 检查是否符合下载条件
	 * @param adminUser
	 * @param billNo
	 * @return
	 */
	public Map checkDownloadDecodeList(AdminUser adminUser,String billNo);

}
