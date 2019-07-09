package com.work.shop.oms.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;


import com.work.shop.oms.api.param.bean.OrderQuestionParam;
import com.work.shop.oms.bean.OrderQuestionExample;
import com.work.shop.oms.common.bean.LackSkuParam;
import com.work.shop.oms.common.utils.StringUtil;
import com.work.shop.oms.dao.OrderQuestionMapper;
import com.work.shop.oms.service.OrderLogisticsQuestionService;
import com.work.shop.oms.vo.QuestionTypeVO;

@Service
public class OrderLogisticsQuestionServiceImpl implements
		OrderLogisticsQuestionService {
	@Resource
	private OrderQuestionMapper orderQuestionMapper;

	@Override
	public List<QuestionTypeVO> judgeQuestionType(String masterOrderSn) {
		// TODO Auto-generated method stub
		//物流问题单记录数
		OrderQuestionExample example = new OrderQuestionExample();
		example.or().andRelatingOrderSnEqualTo(masterOrderSn).andProcessStatusEqualTo(0).andQuestionTypeEqualTo(1);
		int wlqCount =  this.orderQuestionMapper.countByExample(example);
		
		// OS问题单记录数
		OrderQuestionExample questionExample = new OrderQuestionExample();
		questionExample.or().andRelatingOrderSnEqualTo(masterOrderSn).andProcessStatusEqualTo(0).andQuestionTypeEqualTo(0);
		int osqCount = this.orderQuestionMapper.countByExample(questionExample);

		String desc = "";
		List<QuestionTypeVO> list = new ArrayList<QuestionTypeVO>();
		if (wlqCount > 0 && osqCount > 0) {
			desc = "此问题单既是OS问题单又是物流问题单。";
			QuestionTypeVO osTypeVO = new QuestionTypeVO(desc, "OS问题单", "0");
			list.add(osTypeVO);
			QuestionTypeVO LogiTypeVO = new QuestionTypeVO(desc, "物流问题单", "1");
			list.add(LogiTypeVO);
			QuestionTypeVO allTypeVO = new QuestionTypeVO(desc, "全部正常化", "2");
			list.add(allTypeVO);
		} else {
			if (wlqCount > 0) {
				desc = "此问题单是物流问题单。";
				QuestionTypeVO LogiTypeVO = new QuestionTypeVO(desc, "物流问题单", "1");
				list.add(LogiTypeVO);
			}
			if (osqCount > 0) {
				desc = "此问题单是OMS问题单。";
				QuestionTypeVO osTypeVO = new QuestionTypeVO(desc, "OMS问题单", "0");
				list.add(osTypeVO);
			}
		}
		return list;
	}

	/**
	 * 导入问题单
	 * @param is
	 * @param sb
	 * @param logType 问题单类型  0：问题单 1：物流问题单
	 * @param code 问题单原因
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OrderQuestionParam> importOrderLogisticsQuestionList(InputStream is,
			StringBuffer sb, int logType, String code, String mainChild) throws Exception {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Map<String, Object> map = new HashMap<String, Object>();
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环Excel表格内容
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				String errorLine = "第" + (rowNum + 1) + "行";
				if (hssfRow == null) {
					continue;
				}
				HSSFCell cell0 = hssfRow.getCell((short) 0);
				// 订单号
				String orderSn = getValue(cell0);
				if (logType == 0) {
					// 一般问题单
					if (StringUtil.isNotEmpty(orderSn)) {
						if (null == map.get(orderSn) || StringUtil.isEmpty((String) map.get(orderSn))) {
							map.put(orderSn, orderSn);
						}
					}
				} else { //物流
					LackSkuParam sqParam = new LackSkuParam();
					// 38:短配
					// OS订单号(必填项)	商品编码(必填项)	发货仓编码(必填项)	短配数量	缺货原因
					if ("38".equals(code.trim())) {
						HSSFCell cell1 = hssfRow.getCell((short) 1);
						// 商品编码(必填项)
						String sku = getValue(cell1);
						HSSFCell cell2 = hssfRow.getCell((short) 2);
						// 发货仓库编码(必填项)
						String depotCode = getValue(cell2);
						HSSFCell cell3 = hssfRow.getCell((short) 3);
						// 缺货数量
						String lackNum = getValue(cell3);
						
						HSSFCell cell4 = hssfRow.getCell((short) 4);
						// 缺货原因
						String lackReason = getValue(cell4);
						if (StringUtil.isNotBlank(orderSn) && StringUtil.isNotEmpty(sku) && StringUtil.isNotEmpty(depotCode) 
								&& StringUtil.isNotEmpty(lackNum)){
							sqParam.setOrderSn(orderSn);
							sqParam.setQuestionCode(code.trim());
							sqParam.setCustomCode(sku);
							sqParam.setDepotCode(depotCode);
							sqParam.setDeliverySn(null);
							sqParam.setLackReason(lackReason);
						//	if (!StringUtil.isTrimEmpty(lackNum)) {
								sqParam.setLackNum(Short.valueOf(lackNum.trim()));
						/*	} else {
								sqParam.setLackNum((short)1);
							}*/
						//订单号,交货单编码,问题原因必填项为空	
						} else {
							if (StringUtil.isEmpty(orderSn) || StringUtil.isEmpty(sku) || StringUtil.isEmpty(depotCode) 
									||StringUtil.isEmpty(lackNum) ) {
								sb.append(errorLine + "[" + "OS订单号：" + orderSn + ";商品编码：" + sku 
										+ ";发货仓库编码:" + depotCode + ";短配数量： "+lackNum+"+] 导入数据不符合模板要求");
								break;
							} else {
								continue;
							}
						}
					} else {
						// OS订单号(必填项)	交货单编码(必填项)	商品编码(必填项)	发货仓编码	短拣数量
						//37: 短拣
						HSSFCell cell1 = hssfRow.getCell((short) 1);
						// 交货单编码(必填项)
						String deliverySn = getValue(cell1);
						HSSFCell cell2 = hssfRow.getCell((short) 2);
						// 商品编码(必填项)
						String sku = getValue(cell2);
						
						HSSFCell cell3 = hssfRow.getCell((short) 3);
						// 发货仓库编码(必填项)
						String depotCode = getValue(cell3);
						HSSFCell cell4 = hssfRow.getCell((short) 4);
						// 缺货数量
						String lackNum = getValue(cell4);
						if (StringUtil.isNotBlank(orderSn) && StringUtil.isNotEmpty(deliverySn)
								&& StringUtil.isNotEmpty(sku) &&  StringUtil.isNotEmpty(depotCode)
								&& StringUtil.isNotEmpty(lackNum)){
							//订单号,交货单编码,问题原因必填项为空
							sqParam.setOrderSn(orderSn);
							sqParam.setOpType(code.trim());
							sqParam.setCustomCode(sku);
							sqParam.setDepotCode(depotCode);
							sqParam.setDeliverySn(deliverySn);
						//	if (!StringUtil.isTrimEmpty(lackNum)) {
								sqParam.setLackNum(Short.valueOf(lackNum.trim()));
							/*} else {
								sqParam.setLackNum((short)1);
							}*/
						} else {
							if (StringUtil.isEmpty(orderSn) || StringUtil.isEmpty(deliverySn) || StringUtil.isEmpty(sku) 
									|| StringUtil.isEmpty(depotCode)  ||  StringUtil.isEmpty(lackNum) ) {
								sb.append(errorLine + "[" + "OS订单号：" + orderSn + ";交货单编码：" + deliverySn
										+ ";商品编码：" + sku + " ;发货仓库编码 :"+ depotCode +";缺货数量:"+ lackNum+ "] 导入数据不符合模板要求");
								break;
							} else {
								continue;
							}
						}
					}
					if (null != map.get(orderSn) && !((List<LackSkuParam>) map.get(orderSn)).isEmpty()) {
						List<LackSkuParam> params = (List<LackSkuParam>) map.get(orderSn);
						if (StringUtil.isListNotNull(params)) {
							params.add(sqParam);
						} else {
							params = new ArrayList<LackSkuParam>();
							params.add(sqParam);
							map.put(orderSn, params);
						}
						map.put(orderSn, params);
					} else {
						List<LackSkuParam> params = new ArrayList<LackSkuParam>();
						params.add(sqParam);
						map.put(orderSn, params);
					}
				}
			}
		}
		List<OrderQuestionParam> list = new ArrayList<OrderQuestionParam>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			OrderQuestionParam questionParam = new OrderQuestionParam();
			String orderSn = it.next().toString();
			
			if (logType == 0) { //一般问题单
				if("main".equals(mainChild)){
					questionParam.setMasterOrderSn(orderSn);
				}else{
					questionParam.setOrderSn(orderSn);
				}
			}else{
				questionParam.setOrderSn(orderSn);
			}
			
			questionParam.setLogType(logType);
			questionParam.setCode(code);

		/*	if(logType==0){ //一般
				questionParam.setMainChild(mainChild);
			} else{
				questionParam.setMainChild("child");	
			}*/

			if (logType == 0) {
				questionParam.setNote("设问题单：");
			} else {
				questionParam.setLackSkuParams((List<LackSkuParam>)map.get(orderSn));
			}
			list.add(questionParam);
		}
//		if (StringUtil.isNotEmpty(sb.toString())) {
//			throw new Exception(sb.toString());
//		}
		return list;

	}
	
	//转换excel类型
	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (null == hssfCell) {
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			Double temp = hssfCell.getNumericCellValue();
			BigDecimal bd = new BigDecimal(temp);
			return String.valueOf(bd.toString());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

}
