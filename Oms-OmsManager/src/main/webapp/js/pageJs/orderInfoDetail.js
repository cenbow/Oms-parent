var orderActionListUrl = basePath + "/custom/orderInfo/orderActionList.spmvc";
var userLevelDiscount = "${infoVO.common.userLevelDiscount}";
var prevSn = '';
var nextSn = '';
var orderType = '';
var buttons = null;


function changeOrderStatusT(obj,type) {
	if (type == 'lock') {
//		var name=prompt("Please enter your name","");
//		var form = this.up('form').getForm();
		$("#changeOrderForm").action = basePath + "custom/orderStatus/lockOrder.spmvc";
		formSubmit(null, "订单锁定");
		console.dir(name);
	}
}
/**
 * 订单详情内容编辑
 * @param oi
 * @returns {String}
 */
function createPageBody(oi) {
	prevSn = oi.prevSn;
	nextSn = oi.nextSn;
	orderType = oi.common.orderType;
	userLevelDiscount = oi.common.userLevelDiscount;
	buttons = oi.buttons;
	var html = "";
	html += createOrderInfo(oi.common, oi.buttons);
	html += createConsig(oi.consig);
	html += createOther(oi.common);
	$('#orderinfodetail').html(html);// 订单详情
	$("#orderShips").html(createShipGoods(oi.orderShipVos, oi.orderGoods) + createDelivery(oi.deliveryInfos));// 订单发货单商品
	$("#orderPays").html(createOrderPay(oi.orderPaysVo));// 订单支付单
	$("#totalFee").html(createOrderFee(oi.common));// 订单费用
	$("#orderActions").html(createOrderAction(oi.orderActions));// 订单操作日志
	$("#orderOperate").html(createOrderOperate(oi.buttons, oi.common.orderSn));// 订单操作功能按钮
	$('#orderActions tr:nth-child(3n+1)').css('background-color','#EAECFD');
	$('#editDepot').bind('click', function(){
		editDepotStatus();
	});
	return html;
}


/**
 * 顶部按钮状态设置
 * @param buttons
 * @param orderSn
 * @returns {String}
 */
function getTopBtns(buttons, orderSn) {
	var html = '';
	if (buttons == undefined || buttons == null) {
		return html;
	}
	html += '<tr class="tr25" align="center"><td colspan="4">';
	html += TopButton("前一个订单", buttons.prevSn, 'prevSn');
	html += TopButton("生成拒收入库单", buttons.refuse, 'refuse');
	html += TopButton("生成退货单", buttons.returned, 'returned');
	html += TopButton("生成换货单", buttons.exchanged, 'exchanged');
	html += TopButton("额外退款单", buttons.extReturnAcc, 'extReturnAcc');
	html += TopButton("后一个订单", buttons.nextSn, 'nextSn');
	html += ' </td></tr>';
	return html;
}

/**
 * 
 * @param name
 * @param able
 * @param url
 * @param sn
 * @param his
 * @returns {String}
 */
function TopButton(name, able, type) {
	if (type == undefined || type == null || type == '') {
		return '';
	}
	var disabled = "";
	if (able != undefined && able != null && able == '0') {
		disabled = "disabled";
	}
	var html = '<input type="button" style="margin-left: 10px;" class="btn2" onclick="topButtonClick(\''+ type +'\');" value="' + name + '" ' + disabled + ' />';
	return html;
}
/**
 * 
 * @param name
 * @param able
 * @param type
 * @param sn
 * @param his
 * @returns {String}
 */
function operateButton(name, able, type, paySn) {
	var disabled = "";
	if (type == undefined || type == null || type == '') {
		return '';
	}
	if (able == undefined || able == null || able == '0') {
		disabled = "disabled";
	}
	var opClick = '';
	if (type == 'pay') {
		opClick = 'onclick="changePayStatus(this,\''+type+'\',\''+paySn+'\', \'付款\');"';
	} else if (type == 'unPay') {
		opClick = 'onclick="changePayStatus(this,\''+type+'\',\''+paySn+'\', \'未付款\');"';
	} else {
		opClick = 'onclick="changeOrderStatusT(this,\''+type+'\');"';
	}
	var html = '<input type="button" class="btn1" '+opClick+' value="' + name + '" ' + disabled + ' />';
	return html;
}

function editButton(auth,type) {
	if (auth == undefined || auth == null || auth == '' && auth == 0) {
		return '';
	}
	if (type == undefined || type == null || type == '') {
		return '';
	}
	var editHtml = '<div style="float: right;margin-right:20px;">'
		+'<a href="javascript:void(0);" onclick="editOrderInfo(\''+type+'\');">编辑</a></div>';
	return editHtml;
}

function topButtonClick(type) {
	var actionUrl = '';
	if (type == 'prevSn') {
		actionUrl = basePath + '/custom/orderInfo/orderInfoDetail.spmvc?orderSn='+ prevSn +'&isHistory='+isHistory;
	}
	if (type == 'nextSn') {
		actionUrl = basePath + '/custom/orderInfo/orderInfoDetail.spmvc?orderSn='+ nextSn +'&isHistory='+isHistory;
	}
	if (type == 'refuse') {
		actionUrl = basePath + '/custom/orderReturn/orderReturnDetail?orderSn='+ orderSn +'&returnType=1&orderType='+ orderType;
	}
	if (type == 'returned') {
		actionUrl = basePath + '/custom/orderReturn/orderReturnDetail?orderSn='+ orderSn +'&returnType=1&orderType='+ orderType;
	}
	if (type == 'exchanged') {
		actionUrl = basePath + '/custom/exchangeorder/index.spmvc?order_sn='+ orderSn +'&step=add';
	}
	if (type == 'extReturnAcc') {
		actionUrl = basePath + '/custom/orderReturn/orderReturnDetail?orderSn='+orderSn +'&returnType=3&orderType=' + orderType;
	}
	location.href = actionUrl;
}


/**
 * 创建订单信息展示
 * @param oi
 * @param buttons
 */
function createOrderInfo (oi, buttons, returnChange) {
	var oiHtml = '';
	if (oi == undefined || oi == null) {
		return oiHtml;
	}
	oiHtml += tableStart() + '<tbody>';
	if (returnChange == undefined || returnChange == null || returnChange == '') {
		oiHtml += getTopBtns(buttons, oi.orderSn);				// 操作按钮
		oiHtml += tr25Color() + tdCols('<h3 style="margin-left:50px;">订单协同处理</h3>', 4) + trEnd();
		oiHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>基本信息</h3>', 4) + trEnd();
	} else {
		oiHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>原订单信息</h3>', 4) + trEnd();
	}
	oiHtml += tr25Color() + tdW20Right('订单号：') + tdW30H12Left(oi.orderSn);
	var quest = oi.questionStatus;
//	var logq =  oi.logqStatus;
	var isQorS = '&nbsp;';
	if (quest == 1) {
		isQorS += '<span style="color:red;font-weight:bold;">（问题单）</span>';
	}
//	if (logq == 1) {
//		isQorS += '<span style="color:red;font-weight:bold;">（${infoVO.common.logqDesc}）</span>';
//	}
	var timeoutStatus = oi.timeoutStatus;
	var timeoutRsn = oi.timeoutRsn;
	if (timeoutStatus == 1 && timeoutRsn != '') {
		isQorS += '<span style="color:red;font-weight:bold;">（' + c.timeoutRsn + '）</span>';
	}
	var orderStatusStr = getCombineStatus(orderStatus, payStatus, shipStatus);
	
	oiHtml += tdW20Right('订单状态：') + tdW30H12Left(orderStatusStr+isQorS) + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('外部交易号：') + tdW30H12Left(oi.orderOutSn);
	oiHtml += tdW20Right('ERP订单状态：') + tdW30H12Left('<a href="javascript:void(0);" onclick="showERPStatusInfo(120427572511);">ERP状态查询</a>') + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('订单来源：') + tdW30H12Left(getAttrValue(oi.channelName));
	oiHtml += tdW20Right('结算时间：') + tdW30H12Left(format(oi.dearTime)) + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('购货人：') + tdW30H12Left(oi.userName + 
	'<span style="color:red;font-weight:bold;">（'+oi.userLevelName +' / '+oi.userTypeName+'）</span>');
	oiHtml += tdW20Right('下单时间：') + tdW30H12Left(format(oi.addTime)) + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('订单交易类型：') + tdW30H12Left(getValue(SELECT.trans_type, oi.transType, true));
	oiHtml += tdW20Right('分仓发货状态：') + tdW30H12Left(createDepotStatus(oi.depotStatus)) + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('订单类型：') + tdW30H12Left(getValue(SELECT.order_type, oi.orderType, true));
	var returnSn = '';
	if (isNotNull(oi.relatingReturnSn) && oi.relatingReturnSn != '') {
		returnSn = '<span id="curntStatus"> ' +
		'<a target="_blank" href="<%=basePath %>/custom/orderReturn/returnOrderInfo?returnSn='+oi.relatingReturnSn+'">'
		+ oi.relatingReturnSn +'</a>'
		+'</span>';
	}
	oiHtml += tdW20Right('关联退货单号：') + tdW30H12Left('<span id="curntStatus"> ' +
			'<a target="_blank" href="<%=basePath %>/custom/orderReturn/returnOrderInfo?returnSn='+returnSn+'">'+ returnSn +'</a>'
			+'</span>') + trEnd();
	oiHtml += tr25Color() + tdW20Right('促销信息：') + tdW30H12Left(oi.prName);
	var exchangeSn = '';
	if (isNotNull(oi.relatingExchangeSn) && oi.relatingExchangeSn != ''){
		exchangeSn = '<span id="curntStatus">'+
			'<a target="_blank" href="<%=basePath %>/custom/orderInfo/orderInfoDetail.spmvc?orderSn='+oi.relatingExchangeSn+'">' +
			oi.relatingExchangeSn + '</a></span>';
	}
	if (isNotNull(oi.relatingReturnSn)) {
		returnSn = oi.relatingReturnSn;
	}
	oiHtml += tdW20Right('关联原订单号：') + tdW30H12Left(exchangeSn) + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('取消订单原因：') + tdW30H12Left(oi.cancelReason==undefined ? '': oi.cancelReason);
	oiHtml += tdW20Right('订单种类：') + tdW30H12Left(getValue(SELECT.order_category, oi.orderCategory, true)) + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('原因：') + tdW30H12Left(getValue(SELECT.reason, oi.reason, true));
	oiHtml += tdW20Right('单据组织：') + tdW30H12Left(oi.invoicesOrganization) + trEnd();
	
	oiHtml += tr25Color() + tdW20Right('团购订单：') + tdW30H12Left(getValue(SELECT.group_order, oi.isGroup, true));
	oiHtml += tdW20Right('团购订单含预售商品：') + tdW30H12Left(getValue(SELECT.pre_group_order, oi.isAdvance, true)) + trEnd();
	oiHtml += '</tbody>' + tableEnd();
	return oiHtml;
}

/**
 * 创建收货人信息展示
 * @param consig
 * @returns {String}
 */
function createConsig(consig) {
	var conHtml = '';
	if (consig == undefined || consig == null) {
		return conHtml;
	}
	
	// <c:if test="${infoVO.buttons.editConsig > 0}">
	if (buttons.editConsig > 0) { // 查看权限
		var editConsig='';
		if (buttons.editConsig == 2) {	// 编辑权限
			editConsig = editButton(buttons.editConsig,'editConsig');
		}
		conHtml += tableStart() + '<tbody>';
		conHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>收货人信息 '+editConsig+'</h3>', 4) + trEnd();
		conHtml += tr25Color() + tdW20Right('收货人：') + tdW30H12Left(consig.name);
		conHtml += tdW20Right('电子邮件：') + tdW30H12Left(consig.email) + trEnd();
		
		var addr = '[<span id="hidden_countryName">'+ (consig.countryName== undefined ? '': consig.countryName+'&nbsp;') +'</span>' + 
					'<span id="hidden_provinceName">'+ (consig.provinceName== undefined ? '': consig.provinceName+'&nbsp;') +'</span>' +
					'<span id="hidden_cityName">'+ (consig.cityName==undefined ? '': consig.cityName+'&nbsp;') +'</span>' +
					'<span id="hidden_areaName">'+ (consig.areaName==undefined ? '': consig.areaName) +'</span>]' +
					(consig.add==undefined ? '': ' '+ consig.add);
		conHtml += tr25Color() + tdW20Right('地址：') + tdW30H12Left(addr);
		conHtml += tdW20Right('邮编：') + tdW30H12Left(consig.code==undefined ? '': consig.code) + trEnd();
		
		conHtml += tr25Color() + tdW20Right('电话：') + tdW30H12Left(consig.tel==undefined ? '':consig.tel);
		conHtml += tdW20Right('手机：') + tdW30H12Left(consig.mbl==undefined ? '': consig.mbl) + trEnd();
		
		conHtml += tr25Color() + tdW20Right('标志性建筑：') + tdW30H12Left(consig.sign==undefined ? '':consig.sign);
		conHtml += tdW20Right('最佳送货时间：') + tdW30H12Left(consig.bestTime==undefined ? '': consig.bestTime) + trEnd();
		conHtml += '</tbody>' + tableEnd();
	}
	
	return conHtml;
}

/**
 * 创建其他信息展示
 * @param other
 * @returns {String}
 */
function createOther(other) {
	var othHtml = '';
	if (other == undefined || other == null) {
		return othHtml;
	}
	othHtml += tableStart() + '<tbody>';
	othHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>其他信息'+editButton(buttons.editOther,'editOther')+'</h3>', 4) + trEnd();
	
	othHtml += tr25Color() + tdW20Right('发票类型：') + '<td class="td12" align="left" colspan="3">' + other.invType + '</td>'+trEnd();
	
	//othHtml += tr25Color() + tdW20Right('发票类型：') + tdW30H12Left(other.invType);
	
	othHtml += tr25Color() + tdW20Right('发票抬头：') + tdW30H12Left(other.invPayee);
	othHtml += tdW20Right('发票内容：') + tdW30H12Left(other.invContent==undefined ? '': other.invContent) + trEnd();
	
	othHtml += tr25Color() + tdW20Right('电话：') + tdW30H12Left(other.tel==undefined ? '':other.tel);
	othHtml += tdW20Right('手机：') + tdW30H12Left(other.mbl==undefined ? '': other.mbl) + trEnd();
	
	othHtml += tr25Color() + tdW20Right('客户给商家的留言：') + '<td class="td12" align="left" colspan="3">' + other.postscript + '</td>'+trEnd();

	othHtml += tr25Color() + tdW20Right('缺货处理：') + '<td class="td12" align="left" colspan="3">' + other.howOos + '</td>'+trEnd();
	
	othHtml += tr25Color() + tdW20Right('商家给客户的留言：') + '<td class="td12" align="left" colspan="3">' + other.toBuyer + '</td>'+trEnd();

	othHtml += '</tbody>' + tableEnd();
	return othHtml;
}
var totalFee = 0.00;
/**
 * 创建发货单商品信息展示
 * @param oss
 * @param ogs
 * @returns {String}
 */
function createShipGoods(oss, ogs) {
	var osHtml = '';
	if (oss == undefined || oss == null || oss.length == 0) {
		return osHtml;
	}
	if (ogs == undefined || ogs == null || ogs.length == 0) {
		return osHtml;
	}
	osHtml += divStart() + tableStart() + '<tbody>';
	osHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>商品信息 '+editButton(buttons.editGoods,'editGoods')+'</h3>', 4) + trEnd();
	for (var i = 0;i < oss.length;i++) {
		var os = oss[i];
		var shipSn = os.shipSn;
		osHtml += ' <tr height="40">';
		osHtml += '<td class="td12" align="left" colspan="2" valign="bottom">';
		osHtml += '<b>发货单编号 '+ shipSn +'</b>';
		osHtml += '</td>';
		osHtml += '<td class="tdW20" align="right" valign="bottom">ERP发货单状态：</td>';
		osHtml += '<td class="tdW30 td12" align="left" valign="bottom">';
		osHtml += '<a href="javascript:void(0);" onclick="window.open(' + 
		'http://order.bang-go.com.cn/BGOrderManager/manager/erpShipInfo?shipSn=' + shipSn +',\'ERP发货单状态查询\', \'height=280, width=680, top=45.5, left=334.5, toolbar=no,menubar=no, scrollbars=no, resizable=no,location=n o, status=no\');">'+
		'ERP发货单状态查询</a>';
		osHtml += '</td>';
		osHtml += '</tr>';
		
		osHtml += tr25Color() + tdW20Right('快递单号：') + tdW30H12Left(getAttrValue(os.invoiceNo));
		osHtml += tdW20Right('物流状态：') + tdW30H12Left('<a href="javascript:void(0);" onclick="window.open(\'http://www.kuaidi100.com/\',\'物流状态查询\');">物流状态查询</a>') + trEnd();
		
		osHtml += tr25Color() + tdW20Right('配送方式：') + tdW30H12Left(getAttrValue(os.shippingName));
		osHtml += tdW20Right('配送状态：') + tdW30H12Left(getValue(SELECT.ship_status_ship, os.shippingStatus, true)) + trEnd();
	
		osHtml += tr25Color() + tdW20Right('配送仓库：') + tdW30H12Left(getAttrValue(os.regionName) + getAttrValue(os.depotCode) );
		osHtml += tdW20Right('发货最后期限：') + tdW30H12Left(format(os.deliveryLasttime)) + trEnd();
		
		osHtml += tr25Color() + tdCols('商品信息',4) + trEnd();
		// 发货单商品编辑
		osHtml += tr25Color() + tdCols(createOrderGoods(ogs,shipSn),4) + trEnd();
	}
	osHtml += '</tbody>' + tableEnd();
	osHtml += tableStart() + '<tbody><tbody><tr height="50" align="right">' + tdValue("<b>总计：￥"+ numFixed(totalFee)+"元</b>");
	osHtml += '</tbody>' + tableEnd();
	osHtml += '</div>';
	return osHtml;
}

function createOrderGoods(ogs, shipSn) {
	var ogHtml = '';
	if (isNull(ogs) || ogs.length == 0) {
		return ogHtml;
	}
	if (isNull(shipSn)) {
		return ogHtml;
	}
	ogHtml += '<div style="margin-top:-1px; border:1px solid #c8c8c8; border-top:0; border-bottom:0;border-left:0;border-right:0;">'
				+ tableStart() + '<tbody>';
	ogHtml += trH25CenterColor()
					+ tdValue('商品名称')
					+ tdValue('商品属性 ')
					+ tdValue('货号')
					+ tdValue('规格')
					+ tdValue('系统SKU码')
					+ tdValue('产品条形码')
					+ tdValue('企业SKU码')
					+ tdValue('商品价格')
					+ tdValue('会员价格')
					+ tdValue('成交价格')
					+ tdValue('财务价格')
					+ tdValue('分摊金额')
					+ tdValue('商品促销')
					+ tdValue('打折券')
					+ tdValue('折让金额')
					+ tdValue('数量')
					+ tdValue('可用数量')
					+ tdValue('小计')
				+ trEnd();
	var subTotal = 0.00;																		//商品合计
	for(var j = 0;j < ogs.length;j++){
		var og = ogs[j];
		if (shipSn == og.shipSn) {
			var trStyle = '';
			if (isNotNull(og.mergeFrom) && og.mergeFrom >= '1') {
				trStyle = trH25CenterColor('#EB7153');
			} else {
				trStyle = trH25Center();
			}
			var cardCoupon = '';
			if (isNotNull(og.cardCoupon)) {
				cardCoupon = og.cardCoupon.cardNo + '<font color="red">[' +og.cardCoupon.cardMoney+ '</font><br/>';
			}
			var minTotal = og.transactionPrice * og.goodsNumber;								// 商品小计
			subTotal += minTotal;																// 商品合计 = 商品小计总和
			var html = "";
			var occupyNumber =  parseInt(og.sendNumber);										// 占用数量
			var goodsNumber =  parseInt(og.goodsNumber);										// 购买商品数量
			var allStock =  parseInt(og.skuStock.allStock == "" ? 0 : og.skuStock.allStock);	// 库存数量
			var returnRemainNum =  parseInt(og.returnRemainNum == "" ? 0 : og.returnRemainNum);	// 在退商品数量
			var returnNum = parseInt(og.returnNum == "" ? 0 : og.returnNum);					// 已退商品数量
			var shipStatus = parseInt(og.shippingStatus);										// 发货单状态
			html = occupyStockEdit(allStock, occupyNumber, goodsNumber, returnRemainNum, returnNum, shipStatus);
			ogHtml += trStyle
					+ tdValue('<a href="" target="_blank">'+og.goodsName+'</a>')
					+ tdValue(extensionCodeChange(getAttrValue(og.extensionCode)))
					+ tdValue(getAttrValue(og.goodsSn))
					+ tdValue('颜色：' + getAttrValue(og.goodsSn) + '<br>尺寸：' + getAttrValue(og.goodsSizeName))
					+ tdValue(getAttrValue(og.skuSn))
					+ tdValue(getAttrValue(og.barcode))
					+ tdValue(getAttrValue(og.customCode))
					+ tdValue("￥"+ numFixed(og.goodsPrice)+"元")
					+ tdValue("￥"+ numFixed((userLevelDiscount * og.goodsPrice) / 100)+"元")
					+ tdValue("￥"+ numFixed(og.transactionPrice)+"元")
					+ tdValue("￥"+ numFixed(og.settlementPrice)+"元")
					+ tdValue("￥"+ numFixed(og.shareBonus)+"元")
					+ tdValue(getAttrValue(og.promotionDesc))
					+ tdValue(cardCoupon)
					+ tdValue("￥"+ numFixed(og.discount)+"元")
					+ tdValue(html)
					+ tdValue(allStock)
					+ tdValue("￥"+ numFixed(minTotal)+"元");
		}
	}
	totalFee += subTotal;
	ogHtml += tr25Color() + '<td colspan="13" align="right">合计：￥'+numFixed(subTotal)+'元</td>'+ tdCols("", 5) + trEnd();
	ogHtml += '</tbody>' + tableEnd() + '</div>';
	return ogHtml;
}

/**
 * 创建配送信息展示
 * @param deliveryInfos
 * @returns {String}
 */
function createDelivery(deliveryInfos) {
	var deliHtml = '';
	deliHtml += divStart();
	deliHtml += tableStart() + '<tbody>';
	deliHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>配送信息</h3>', 5) + trEnd();
	deliHtml += tr25Color() + '<td class="tdW30 td12" colspan="5" align="left" valign="bottom">'
				+ '<a href="javascript:void(0);" onclick="window.open(\'http://www.kuaidi100.com/\',\'物流状态查询\');">物流状态查询</a>'
				+ '</td>' + trEnd();
	deliHtml += tr25Color() + tdH12W20Middle('仓库')
			+ tdH12W20Middle('承运商')
			+ tdH12W20Middle('快递单号')
			+ tdH12W20Middle('商品sku')
			+ tdH12W20Middle('商品数量') + trEnd();
	if (isNull(deliveryInfos) || deliveryInfos.length == 0) {
		deliHtml += '</tbody>' + tableEnd();
		return deliHtml;
	}
	for (var i = 0;i < deliveryInfos.length;i++) {
		var delivery = deliveryInfos[i];
		deliHtml += tr25Color() + tdH12W20Middle(getAttrValue(delivery.depotCode))
		+ tdH12W20Middle(getAttrValue(delivery.carriers))
		+ tdH12W20Middle(getAttrValue(delivery.invoiceNo))
		+ tdH12W20Middle(getAttrValue(delivery.custumCode))
		+ tdH12W20Middle(getAttrValue(delivery.goodsNumber)) + trEnd();
	}
	deliHtml += '</tbody>' + tableEnd();
	deliHtml += '</div>';
	return deliHtml;
}

/**
 * 创建付款信息展示
 * @param orderPays
 * @returns {String}
 */
function createOrderPay(orderPays, returnChange) {
	var opHtml = '';
	opHtml += '<div style="margin-top:-1px; border:1px solid #c8c8c8; border-top:0; border-bottom:0;">';
	opHtml += tableStart() + '<tbody>';
	opHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>付款信息'+editButton(buttons.editPayment,'editPayment')+'</h3>', 11) + trEnd();
	opHtml += trH25CenterColor()
			+ tdValue('付款单编号')
			+ tdValue('支付方式')
			+ tdValue('付款备注')
			+ tdValue('使用红包')
			+ tdValue('余额支付')
			+ tdValue('付款总金额')
			+ tdValue('支付状态')
			+ tdValue('支付时间')
			+ tdValue('客户支付时间')
			+ tdValue('付款最后期限');
			if (returnChange == undefined || returnChange == null || returnChange == '') {
				opHtml += tdValue('操作');
			}
			opHtml += trEnd();
	if (orderPays == undefined || orderPays == null || orderPays.length == 0) {
		opHtml += '</tbody>' + tableEnd();
		return opHtml;
	}
	for (var i = 0;i < orderPays.length;i++) {
		var op = orderPays[i];
		var paySn = getAttrValue(op.paySn);
		var changePay = '';
		if (buttons.changePayment == 1) {
			changePay = '&nbsp;<a href="javascript:void(0);" onclick="editOrderInfo(\'changePayment\');">编辑</a>';
		}
		opHtml += trH25CenterColor() 
			+ tdValue(paySn)
			+ tdValue(getAttrValue(op.payName) + changePay)
			+ tdValue(getAttrValue(op.payNote))
			+ tdValue(getAttrValue(op.bonusName))
			+ tdValue(getAttrValue(op.surplus))
			+ tdValue(getAttrValue(op.payTotalfee))
			+ tdValue(getValue(SELECT.pay_status, op.payStatus, true) + '<input type="hidden" name="hiddenPaySts" value="0">')
			+ tdValue(format(op.payTime))
			+ tdValue(format(op.userPayTime))
			+ tdValue(format(op.payLasttime));
			if (returnChange == undefined || returnChange == null || returnChange == '') {
				opHtml += '<td width="200">'
				+ operateButton("已付款", buttons.pay , 'pay', paySn)
				+ operateButton("未付款", buttons.unPay , 'unPay', paySn)
			+'</td>';
			}
			opHtml += trEnd();
	}
	opHtml += '</tbody>' + tableEnd();
	opHtml += '</div>';
	return opHtml;
}

/**
 * 创建订单费用展示
 * @param oi
 * @returns {String}
 */
function createOrderFee(oi) {
	var feeHtml = '';
	if (oi == undefined || oi == null || oi.length == 0) {
		feeHtml += '';
		return feeHtml;
	}
	var goodsAmount = "商品总金额：￥"+ numFixed(oi.goodsAmount)+"元 - ";
	var discount = "折让：￥"+ numFixed(oi.discount)+"元 + ";
	var tax = "发票税额：￥"+ numFixed(oi.tax)+"元 + ";
	var shippingTotalFee = "配送费用：￥"+ numFixed(oi.shippingTotalFee)+"元 + ";
	var insureTotalFee = "保价费用：￥"+ numFixed(oi.insureTotalFee)+"元 + ";
	var payTotalFee = "支付费用：￥"+ numFixed(oi.payTotalFee)+"元 = ";
	var totalFee = "<b>订单总金额：￥"+ numFixed(oi.totalFee)+"元</b>";
	var content = goodsAmount + discount + tax + shippingTotalFee + insureTotalFee + payTotalFee + totalFee;
	
	var totalFee = "订单总金额：￥"+ numFixed(oi.totalFee)+"元 - ";
	var moneyPaid = "已付款金额：￥"+ numFixed(oi.moneyPaid)+"元 - ";
	var surplus = "使用邦购币：￥"+ numFixed(oi.surplus)+"元 - ";
	var bonus = "使用红包【"+oi.bonusName+"】：￥"+ numFixed(oi.bonus)+"元 = ";
	var totalPayable = "<b>应付款金额：￥"+ numFixed(oi.totalPayable)+"元</b>";
	var content2 = totalFee + moneyPaid + surplus + bonus + totalPayable;
	
	feeHtml += tableStart() + '<tbody>';
	feeHtml += trH25Right() + tdValue(content) + trEnd();
	feeHtml += trH25Right() + tdValue(content2) + trEnd();
	feeHtml += '</tbody>' + tableEnd();
	return feeHtml;
}

/**
 * 创建订单操作按钮
 * @param buttons
 * @param orderSn
 * @returns
 */
function createOrderOperate(buttons) {
	operateHtml = '';
	if (buttons == undefined || buttons == null) {
		return operateHtml;
	}
	operateHtml += tableStart() + '<tbody>';
	operateHtml += trH25CenterColor('#C0D0DD') + tdCols('<h3>操作信息</h3>', 11) + trEnd();
	operateHtml += trH25Right() + '<td width="5%" align="center" valign="middle">操作：</td>';
	operateHtml += '<td align="left" valign="middle">';
	operateHtml += operateButton("锁定", buttons.lock, 'lock');
	operateHtml += operateButton("解锁", buttons.unlock, 'unlock');
	operateHtml += operateButton("确认", buttons.confirm, 'confirm');
	operateHtml += operateButton("未确认", buttons.unConfirm, 'unConfirm');
	operateHtml += operateButton("取消", buttons.cancel, 'cancel');
	operateHtml += operateButton("设为问题单", buttons.qustion, 'qustion');
	operateHtml += operateButton("返回正常单", buttons.returnNormal, 'returnNormal');
	operateHtml += operateButton("通知收款", buttons.noticeGathring, 'noticeGathring');
	operateHtml += operateButton("结算", buttons.deal, 'deal');
	operateHtml += operateButton("沟通", buttons.chat, 'chat');
	operateHtml += operateButton("释放库存", buttons.release, 'release');
	operateHtml += operateButton("复活", buttons.relive, 'relive');
	operateHtml += '</td>';
	operateHtml += '</tbody>' + tableEnd();
	return operateHtml;
}
/**
 * 创建操作日志信息展示
 * @param orderActions
 * @returns {String}
 */
function createOrderAction(orderActions) {
	var actionHtml = '';
	if (orderActions == undefined || orderActions == null || orderActions.length == 0) {
		actionHtml += '';
		return actionHtml;
	}
	actionHtml += '<div id="orderActions" style="margin-top:-1px; border:1px solid #c8c8c8; border-top:0;">';
	actionHtml += tableStart() + '<tbody>';
	actionHtml += '<tr class="tr25" align="center" bgcolor="#C0D0DD" style="background-color: rgb(234, 236, 253);">';
	actionHtml += '<td width="130"><h3>操作者</h3></td>'
				+'<td width="160"><h3>操作时间</h3></td>'
				+'<td width="85"><h3>订单状态</h3></td>'
				+'<td width="85"><h3>付款状态</h3></td>'
				+'<td width="125"><h3>发货状态</h3></td>'
				+'<td ><h3>备注</h3></td>'
		+ trEnd();
		for (var i = 0; i < orderActions.length;i++) {
			var action = orderActions[i];
			actionHtml += tr25Color()
				+ tdValue('<span style="padding-left:10px;">'+action.actionUser+'</span>')
				+ '<td align="center">' + format(action.logTime) + '</td>'
				+ '<td align="center">' + getOrderStatus(action.orderStatus) + '</td>'
				+ '<td align="center">' + getPayStatus(action.payStatus) + '</td>'
				+ '<td align="center">' + getShipStatus(action.shippingStatus) + '</td>'
				+ tdValue('<div style="padding-left:10px;line-height:20px;">'+ action.actionNote + '</div>')
			+ trEnd();
		}
		actionHtml += '</tbody>' + tableEnd() + '</div>';
	return actionHtml;
}
/**
 * 
 * @param depotStatus
 * @returns {String}
 */
function createDepotStatus(depotStatus) {
	var dshtml = '';
	if (depotStatus == undefined || depotStatus == null) {
		return dshtml;
	}
	var editDepot = '';
	if (isNotNull(buttons.editDepot) && buttons.editDepot == 1) {
		editDepot = '<a style="color:gray;float:right;margin-right:10px;" href="javascript:void(0);" onclick="editDepotStatus();" id="editDepot">编辑</a>';
	}
	dshtml += '<table style="border-collapse:collapse;border-spacing:0px;font-family: \'宋体\',\'Arial\';font-size: 12px;" border="0" width="100%"></tbody>';
	dshtml += '<tr><td id="depotTdLeft" style="border:none;"><span id="curntStatus">' + 
					getValue(SELECT.depot_status, depotStatus, true) + 
				'</span>' +
				'<input type="hidden" value="'+ depotStatus +'" id="depotStatusHidden">' + 
				'<select style="display:none;" id="depotStatusSelect"></select>' + 
				'<span style="display:none;" id="clickA">' +
					'&nbsp;&nbsp; <a id="saveDepot" href="javascript:void(0);">保存</a>'+
					'&nbsp;&nbsp; <a id="cancelDepot" href="javascript:void(0);">取消</a>' +
				'<span>' +
				'</span></span>'+
				'</td>'+
				'<td width="90" style="border:none;" id="rightTD">' + editDepot +
				'</td></tr></tbody></table>';
	return dshtml;
}




function isNotNull(v) {
	if (v == undefined || v == null || v == '') {
		return false;
	}
	return true;
}

function isNull(v) {
	if (v == undefined || v == null || v == '') {
		return true;
	}
	return false;
}

function getAttrValue(v) {
	if (v == undefined || v == null) {
		return '';
	}
	return v;
}
function tableStart() {
	return '<table width="100%" style="border: solid 1px #c8c8c8;border-collapse: collapse;font-family: \'宋体\',\'Arial\';font-size: 12px;background: #fff;color: #000;">';
}

function divStart() {
	return '<div style="margin-top:-1px; border:1px solid #c8c8c8; border-top:0; border-bottom:0;">';
}

function tableEnd() {
	return '</table>';
}

/**
 * 创建Table TR height=25px 开启标签
 * @param w					TR宽 {%}
 * @param red				是否是红色样式 y:是;n:不是
 * @returns {String}
 */
function tr25Color(red) {
	var sClass = "";
	if (red != undefined && red != null && (red == 'y' || red == 'Y')) {
		sClass = "isRed";
	}
	return '<tr class="tr25 ' + sClass + '">';
}

/**
 * TR 标题行 样色可自定义
 * @param color
 * @returns {String}
 */
function trH25CenterColor(color) {
	var c = '';
	if (isNotNull(color)) {
		c = 'bgcolor="' + color + '"';
	}
	return '<tr class="tr25" align="center" ' + c + '">';
}

function trH25Center() {
	return '<tr class="tr25" align="center">';
}

function trH25Right() {
	return '<tr class="tr25" align="right">';
}
/**
 * 创建Table TR 关闭标签
 * @returns {String}
 */
function trEnd() {
	return '</tr>';
}
/**
 * 
 * @param value
 * @returns {String}
 */
function tdW30H12Left(value) {
	return '<td class="tdW30 td12" align="left">' +value+ '</td>';
}

/**
 * 
 * @param value
 * @returns {String}
 */
function tdW20Right(value) {
	return '<td class="tdW20" align="right">' +value+ '</td>';
}

function tdH12W20Middle(value) {
	return '<td class="tdW20 td12" align="middle" valign="center">'+ value + '</td>';
}
/**
 * 创建普通TD{不含样式}
 * @param value
 * @returns {String}
 */
function tdValue(value) {
	return '<td>' +value+ '</td>';
}

/**
 * TD 多列合并
 * @param value					TD内容
 * @param cols					列数
 * @returns
 */
function tdCols(value, cols) {
	var colspan = '';
	if (cols != undefined && cols != null && cols != '') {
		colspan = 'colspan="' + cols + '"';
	}
	return '<td '+colspan+'>' + value + '</td>';
}

/**
 * 创建Table TD 标签
 * @param h					TD高 {px}
 * @param w					TD宽 {%}
 * @param align				文本对齐位置 left;right;center等
 * @returns {String}
 */
function createTd(h, w, align) {
//	var sClass = " class='";
//	if (h != undefined && h != null && h != '') {
//		sClass += " td" + h;
//	}
//	if (w != undefined && w != null && w != '') {
//		sClass += " tdW" + w;
//	}
//	
//	sClass += "'";
//	var ca = "align='"; 
//	if (align != undefined && align != null && align != '') {
//		ca += "w;
//	}
	
	return '<td class="tdW30 td12" align="left">';
}

function changeOrderStatus(obj, url, type, paySn) {
	if ($(obj).val() == '返回正常单' && (eval($('#result').val())[0].comm.payStatus) == 1) {
		var py = eval($('#result').val())[0].pays;
		for ( var i = 0; i < py.length; i += 1) {
			var pay = numFixed(eval($('#result').val())[0].pays[i].totalPay);
			if (py[i].status == 0 && pay > 0) {
				var needpay = pay < 0 ? 0.00 : pay;
				alert("该订单还有￥" + needpay + "元未付款，需等待付款后才能发货，故此单暂时不能返回正常单。");
				return;
			} else if (pay < 0) {
				var lastpay = numFixed(INFO.payTotal);
				var pp = lastpay < 0 ? 0.00 : lastpay;
				alert("该订单还有￥" + pp + "元未付款，需等待付款后才能发货，故此单暂时不能返回正常单。");
				return;
			}
		}
	}
	if (obj) {
		if ($(obj).val() == '通知收款' && $('#backScriptForm').val().length < 6) {
			alert('请在备注输入支付方式相关信息，至少6个字！');
			return;
		}
		if ($(obj).val() == '取消'
				&& (eval($('#result').val())[0].other).orderType == 2
				&& !confirm("是否确定取消该换货订单？")) {
			return;
		}
	}
	if(eval($('#result').val())[0].comm.ref == "全流通"){
		if($(obj).val() == '占用库存'){
			alert("全流通订单不允许占用库存操作！");
			return;
		}
		if($(obj).val() == '释放库存'){
			alert("全流通订单不允许释放库存操作！");
			return;
		}
	}
	if ($(obj).val() == '确认'
			&& (eval($('#result').val())[0].comm.tranType) != 2
			&& (eval($('#result').val())[0].comm.payStatus) <= 1
			&& !confirm("该订单还没付款或支付金额不足，要继续确认吗？")) {
		return;
	} else {
		var formS = $('#orderStatusForm');
		var isSubmit = false;
		if (paySn) {
			$('#paySn').val(paySn);
		}
		if (url != null && url.length > 1) {
			formS.attr('action', url);
			isSubmit = true;
		} else {
			formS.attr('action', '');
			isSubmit = false;
		}
		if (type != null && type != '') {
			var oFloat = $('#' + type);
			oFloat.find('textarea').val('');
			// 2010/10/20 add begin
			if (type == 'questionFloat') {
				initSelectValue('qReason', 3, false, 0);
			} else if (type == 'cancelFloat') {
				initSelectValue('cReason', 8, false, 0);
			} else if (type == 'uQuestionFloat') {
				osandlogisticsinfo();
			}
			// 2010/10/20 add end
			oFloat.showPop({
				closeable : false
			});
		} else {
			if (isSubmit) {
				/**
				 * 2011.11.03号添加 //使按钮灰色，防止重复点击
				 * if(validateDisable($(obj).val())) { $(obj).attr('disabled',
				 * true); var actionStr = $(obj).val() + '处理中...';
				 * $(obj).val(actionStr); }
				 */
				formS.submit();
			}
		}
	}
}

function refresh() {
	location.href = basePath + "/custom/orderInfo/orderInfoDetail.spmvc?orderSn="+orderSn+"&isHistory="+isHistory;
}
/**
 * form 表单提交
 */
function formSubmit(form,desc) {
	confirmMsg("确认","您确定要"+desc+"吗?", function(btn) {
		if (btn == "yes") {
			var json = {
				success: function(form, action){
					if (action.result.success  == "false") {
						errorMsg("结果", action.result.msg);
					} else{
						refresh();
						//Ext.Msg.alert('结果',action.result.msg,function(xx){
						//	refresh();
						//});
					}
				},
				failure: function(form, action){
					errorMsg("结果", action.result.msg);
				},
				waitMsg:'Loading...'
			};
			form.submit(json);
		}
	});
}
function changePayStatus(obj,type,paySn,desc) {
	var params = {'paySn': paySn, 'orderSn': orderSn};
	confirmMsg("确认","您确定要"+desc+"吗?", function(btn) {
		if (btn == "yes") {
			Ext.Ajax.request( {
				waitMsg : '请稍等.....',
				timeout : 100000,
				url : basePath + '/custom/orderStatus/'+type+'.spmvc',
				method : 'post',
				params : params, 
				success : function(response) {
					var respText = Ext.JSON.decode(response.responseText);
					if(respText.success == "true"){
						Ext.Msg.alert('结果',respText.msg, function(xx){
							refresh();
						});
					}else{
						errorMsg("结果", respText.msg);
					}
				},
				failure : function(response) {
				//	var respText = Ext.util.JSON.decode(response.responseText); 
					alert("失败");
				}
			});
		}
	});
}

function changeDepotStatus(depotStatus, statusName, desc) {
	var params = {'orderSn': orderSn, 'message': statusName, 'depotStatus': depotStatus};
	confirmMsg("确认","您确定要"+desc+"吗?", function(btn) {
		if (btn == "yes") {
			Ext.Ajax.request( {
				waitMsg : '请稍等.....',
				timeout : 100000,
				url : basePath + '/custom/orderStatus/updateDepot.spmvc',
				method : 'post',
				params : params, 
				success : function(response) {
					var respText = Ext.JSON.decode(response.responseText);
					if(respText.success == "true"){
						Ext.Msg.alert('结果',respText.msg, function(xx){
							refresh();
						});
					}else{
						errorMsg("结果", respText.msg);
					}
				},
				failure : function(response) {
				//	var respText = Ext.util.JSON.decode(response.responseText); 
					alert("失败");
				}
			});
		}
	});
}

function editOrderInfo(type) {
	//http://localhost:8080/OmsManager//custom/orderGoods/updateOrderGoods.spmvc?method=init&orderSn=110103001737
	var editUrl =  basePath+"custom/orderInfo/"+type+".spmvc?method=init&orderSn="+orderSn;
	if (type == 'editConsig') {
		FormEditWin.showAddDirWin("popWins", editUrl, null,"编辑订单 - 编辑收货人信息", '1000', '480');
	} else if (type == 'editOther') {
		FormEditWin.showAddDirWin("popWins", editUrl, null,"编辑订单 - 编辑其他信息", 624, 320);
	} else if (type == 'editGoods') {
		editUrl =  basePath+"/custom/orderGoods/updateOrderGoods.spmvc?method=init&orderSn="+orderSn;
		//window.location.href = editUrl;
		FormEditWin.showAddDirWin("popWins", editUrl, null,"编辑订单 - 编辑商品信息", '95%', 650);
	} else if (type == 'editPayment') {
		editUrl =  basePath+"custom/paymentInfo/"+type+".spmvc?method=init&orderSn="+orderSn;
		FormEditWin.showAddDirWin("popWins", editUrl, null,"编辑订单 - 编辑付款信息", 624, 260);
	} else if (type == 'changePayment') {
		var editUrl =  basePath+"custom/paymentWay/"+type+".spmvc?method=init&orderSn="+orderSn;
		FormEditWin.showAddDirWin("popWins", editUrl, null,"编辑订单 - 编辑支付方式", 624, 360);
	} else if (type == 'editDelivery') {
	//	editUrl =  basePath+"/custom/DistributionWay/editDelivery.spmvc?method=init&orderSn="+orderSn;
	//	FormEditWin.showAddDirWin("popWins", editUrl, null,"编辑订单 - 编辑配送方式", 624, 260);
	} else if (type == 'editWarehouse') {
		editUrl =  basePath+"/custom/orderInfo/editWarehouse.spmvc";
		var parmas = "orderSn="+orderSn;
		 //createAjaxData("editWarehouseAjax",editUrl, successfun, failurefun, params, timeout) 
		createAjaxData("editWarehouseAjax",editUrl, successfun, null, parmas);
	}
}

function successfun(id,response, opts) {
	if("editWarehouseAjax"==id) {
		var josnObj = eval('(' + response.responseText + ')');
		var data = josnObj.data;
		if(josnObj.isok){
			//会员级别
			var userLevelDiscount = data.userLevelDiscount;
			var shtml='';
			
			var total = 0;
			//发货单
			var osList = data.osList;
			//所有商品
			var ogvoList = data.ogvoList;
			for(var i=0; i<osList.length; i++){
				var orderShip = osList[i];
				var minTotal = 0;
				var regionName = orderShip.regionName == null ? "" : orderShip.regionName;
				var depotCode = orderShip.depotCode==null || undefined == orderShip.depotCode ? "" : orderShip.depotCode;
				shtml+='<div style="margin-top:-1px; border:1px solid #c8c8c8; border-top:0; border-bottom:0;">'+
							'<table width="100%" style="border-spacing:0px;border-collapse: collapse;font-family: \'宋体\',\'Arial\';font-size: 12px;background: #fff;color: #000;">'+
								'<tbody id="">'+
									'<tr height="40">'+
										'<td class="td12" align="left" colspan="2" valign="bottom">'+
											'<b>发货单编号 '+orderShip.shipSn+'</b>'+
										'</td>'+
										'<td class="tdW20" align="right" valign="bottom">ERP发货单状态：</td>'+
											'<td class="tdW30 td12" align="left" valign="bottom">'+	
											'<a href="javascript:void(0);" onclick="window.open(\'http://order.bang-go.com.cn/BGOrderManager/manager/erpShipInfo?shipSn=FH1204275136860001\',\'ERP发货单状态查询\', height=280, width=680, top=45.5, left=334.5, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no);">'+
											'ERP发货单状态查询'+
											'</a>'+
										'</td>'+
									'</tr>'+ 
									'<tr class="tr25">'+
										'<td class="tdW20" align="right">快递单号：</td>'+
											'<td class="td12" align="left" colspan="1">'+orderShip.invoiceNo+'</td>'+
											'<td class="tdW20" align="right" valign="bottom">物流状态：</td>'+
											'<td class="tdW30 td12" align="left" valign="bottom">'+
												'<a href="javascript:void(0);" onclick="window.open(\'http://www.kuaidi100.com/\',\'物流状态查询\');">'+
												'物流状态查询'+
											'</a>'+
										'</td>'+
									'</tr>'+
									'<tr class="tr25">'+
										'<td class="tdW20" align="right">配送方式：</td>'+
										'<td class="tdW30 td12" align="left">' + orderShip.shippingName +'</td>'+
										'<td class="tdW20" align="right">配送状态：</td>'+
										'<td class="tdW30 td12" align="left">'+
										getShipStatus(orderShip.shippingStatus)+
										'</td>'+
			 						'</tr>'+ 
									'<tr class="tr25">'+
										'<td class="tdW20" align="right">配送仓库：</td>'+
										'<td class="tdW30 td12" align="left">'+
											regionName+'&nbsp' + depotCode+'</td>'+
										'<td class="tdW20" align="right">发货最后期限：</td>'+
										'<td class="tdW30 td12" align="left">'+
											format(orderShip.deliveryLasttime)+
										'</td>'+
									'</tr>'+
									'<tr class="tr25">'+
										'<td class="td12" align="left" colspan="4">商品信息</td>'+
									'</tr>'+
								'</tbody>'+
							'</table>'+
							'<table width="100%" style="border-spacing:0px;border-collapse: collapse;font-family: \'宋体\',\'Arial\';font-size: 12px;background: #fff;color: #000;">'+
								'<tbody>'+
									'<tr class="tr25" align="center">'+
										'<td>商品名称</td>'+
										'<td>商品属性 </td>'+
										'<td>货号</td>'+
										'<td>规格</td>'+
										'<td>系统SKU码</td>'+
										'<td>产品条形码</td>'+
										'<td>企业SKU码</td>'+
										'<td>商品价格</td>'+
										'<td>会员价格</td>'+
										'<td>成交价格</td>'+
										'<td>财务价格</td>'+
										'<td>分摊金额</td>'+
										'<td>商品促销</td>'+
										'<td>打折券</td>'+
										'<td>折让金额</td>'+
										'<td>数量</td>'+
										'<td>可用数量</td>'+
										'<td>小计</td>'+
									'</tr>';
									for(var j=0; j<ogvoList.length; j++){
										var orderGoods = ogvoList[j];
										if(orderShip.shipSn == orderGoods.shipSn) { 
											var trCss = "";
											if (orderGoods.mergeFrom >= '1') {
												trCss = 'style="background-color: #EB7153;"';
											}
											var goodsSn = orderGoods.goodsSn==null || undefined == orderGoods.goodsSn ? "" : orderGoods.goodsSn;
											var barcode = orderGoods.barcode==null || undefined == orderGoods.barcode ? "" : orderGoods.barcode;
											shtml+= '<tr class="tr25" align="center" ' + trCss + '>'+
														'<td><a href="" target="_blank">'+orderGoods.goodsName +'</a></td>'+
														'<td>'+ extensionCodeChange(orderGoods.extensionCode)+ '</td>'+
														'<td>'+ goodsSn +'</td>'+
														'<td>颜色：'+orderGoods.goodsColorName +'<br>尺寸：'+orderGoods.goodsSizeName+'</td>'+
														'<td>'+orderGoods.skuSn +'</td>'+
														'<td>'+ barcode +'</td>'+
														'<td>'+orderGoods.customCode +'</td>'+
														'<td>'+ "￥"+ numFixed(orderGoods.goodsPrice)+"元"+ '</td>'+
														'<td>';
															var goodsPrice = orderGoods.goodsPrice;
															if (userLevelDiscount == 0 || userLevelDiscount == -1) {
																shtml+= '--';
															} else {
																shtml+= '￥'+ numFixed((userLevelDiscount * goodsPrice) / 100)+'元';
															}
											shtml+= '</td>';
											shtml+= '<td>' + '￥'+ numFixed(orderGoods.transactionPrice)+'元'+ '</td>'+ // 财务价格
													'<td>' + '￥'+ numFixed(orderGoods.settlementPrice)+'元' + '</td>'+
													'<td>' + '￥'+ numFixed(orderGoods.shareBonus)+'元' + '</td>'+// 分摊金额
													'<td>';
														// 促销信息
														shtml+=  undefined  == orderGoods.promotionDesc || orderGoods.promotionDesc == null ? "" : orderGoods.promotionDesc;
														shtml+='</td>';
														shtml+='<td>';
														//打折券信息 
														if(null != orderGoods.cardCoupon) {
															var cardNo = orderGoods.cardCoupon.cardNo;
															var cardMoney = orderGoods.cardCoupon.cardMoney;
															shtml+= cardNo+'<font color="red">['+cardMoney+'折券]</font><br/>';
														}	
														shtml+='</td>';
														shtml+='<td>' + '￥' + numFixed(orderGoods.discount)+'元' + '</td>';
														
														var goodsNumber = orderGoods.goodsNumber; // 购买商品数量
														//var allStock = orderGoods.skuStock.allStock;   //库存数量
														var allStock = orderGoods.availableNumber == null || undefined == orderGoods.availableNumber ? "" : orderGoods.availableNumber;
														var returnRemainNum = orderGoods.returnRemainNum;
														var returnNum = orderGoods.returnNum;
														var shipStatus = orderShip.shippingStatus;
														var html = occupyStockEdit(allStock, occupyNumber, goodsNumber, returnRemainNum, returnNum, shipStatus);
														shtml+='<td>' + html + '</td>'+
															'<td>'+ allStock+'</td>'+
															'<td>'+'￥'+ numFixed(orderGoods.transactionPrice * orderGoods.goodsNumber)+'元'+'</td>';
														shtml+='</tr>';
													//累计商品合计;
													minTotal += orderGoods.transactionPrice * orderGoods.goodsNumber;
									}
							}
							//商品合计;
							shtml+='<tr class="tr25">'+
										'<td colspan="13" align="right">'+
											'合计：￥'+numFixed(minTotal)+'元'+
										'</td>';
									shtml+=
									'</tr>'+
								'</tbody>'+
							'</table>'+
						'</div>';
						total+=minTotal;
			}
			shtml+='<div style="margin-top:-1px; border:1px solid #c8c8c8; border-top:0; border-bottom:0;">';
				shtml+='<table width="100%" style="border-spacing:0px;border-collapse: collapse;font-family: \'宋体\',\'Arial\';font-size: 12px;background: #fff;color: #000;">'
					+'<tbody>'
						+ '<tr height="50" align="right">';
						shtml+='<td>' + '<b>总计：￥'+numFixed(total)+'元</b>'+ '</td>';
					shtml+= '</tr>'; 
						+ '</tbody>'
					+ '</table>'
			+ '</div>'; 
			$("#editWarehouseDiv").html(shtml);
		} else {
			alert(josnObj.message);
		}
	}
}

function add0(m) {
	return m < 10 ? '0'+m:m; 
} 

function format(shijianchuo) {
	if (undefined == shijianchuo || null == shijianchuo) {
		return '';
	}
	var time = new Date(shijianchuo); 
	var y = time.getFullYear(); 
	var m = time.getMonth()+1; 
	var d = time.getDate(); 
	var h = time.getHours(); 
	var mm = time.getMinutes(); 
	var s = time.getSeconds(); 
	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s); 
}

/**
 * 占用库存编辑
 * @param allStock
 * @param occupyNumber
 * @param goodsNumber
 * @param returnRemainNum
 * @param returnNum
 * @param shipStatus
 */
function occupyStockEdit(allStock, occupyNumber, goodsNumber, returnRemainNum, returnNum, shipStatus) {
	var html = "";
	var retnStr = '';
	if (allStock != '') {
		allStock = parseInt(allStock);
	} else {
		allStock = 0;
	}
	if (returnRemainNum > 0) {
		retnStr = '<font style="color:red">（待退货 ' + returnRemainNum + '）</font>';
	}
	if (returnNum > 0) {
		retnStr += '<font style="color:red">（已退货 ' + returnNum + '）</font>';
	}
	if (shipStatus == 0) { // 未发货
		var occStr = '';
		if(occupyNumber == goodsNumber){
			occStr = '(已占库存)';
		}
		if(occupyNumber != 0 && occupyNumber < goodsNumber){
			occStr = '(已占'+occupyNumber+'件)';
		}
		if(occupyNumber > 0){
			//占有过库存
			if(goodsNumber > allStock){
				//库存不够
				html += '<font style="color:red;font-weight:bold;">' + goodsNumber +'</font>' + retnStr
					+ '<font style="color:green;font-weight:bold;">'+ occStr +'</font>';
			}else{
				//库存足够
				html += goodsNumber + retnStr;
			}
		} else {
			//没有占库存
			if(goodsNumber > allStock){
				//库存不够
				html += '<font style="color:red;font-weight:bold;">' + goodsNumber +'</font>' + retnStr
					+ '<font style="color:green;font-weight:bold;">(已占库存0件)</font>';
			}else{
				html += goodsNumber + retnStr;
			}
		}
	} else { // 其他发货状态
		html += goodsNumber + retnStr;
	}
	return html;
}


/**
 * 编辑分仓发货状态
 */
var initTime = 0;
function editDepotStatus() {
	var opts = SELECT.depot_status;
	var html = '';
	var curnt = $('#depotStatusHidden').val();
	var curntObj = $('#curntStatus');
	for ( var i = 1; i < opts.length; i++) {
		var o = opts[i];
		html += '<option value="' + o.v + '" ' + (o.v == curnt ? 'selected="selected"' : '') + ' >' + o.n + '</option>';
	}
	$('#clickA').fadeIn(0);
	curntObj.fadeOut(0);
	$('#depotStatusSelect').html(html).fadeIn(0);
	if (initTime == 0) {
		$('#cancelDepot').bind('click', function() {
			$('#clickA').fadeOut(0);
			$('#depotStatusSelect').html('').fadeOut(0);
			curntObj.fadeIn(0);
		});
		$('#saveDepot').bind('click', function() {
			var vObj = $('#depotStatusSelect').find('option:selected');
			$('#depotStatusID').val(vObj.val());
			$('#depotStatusStr').val(vObj.html());
			changeDepotStatus(vObj.val(), vObj.html(), "更新分仓发货状态");
		});
		initTime++;
	}
}
/**
 * 详情页button 可操作状态
 * @param status
 * @returns {Boolean}
 */
function ButtonDis(status) {
	if (status == '0' || status == false) {
		return true;
	} else {
		return false;
	}
}

/**
 * ERP状态查询
 * @param sn
 */
function showERPStatusInfo(sn) {
	 var url = basePath+"/pages/erp/erpStatusInfo.jsp?orderSn=" + sn;
	 window.open(url, 'ERP状态查询');
}

function orderActionList(orderSn) {
	createAjaxData("orderActionList", orderActionListUrl, orderActionListSuccessfun, null, {"orderSn":orderSn}, 100000);
}

function orderActionListSuccessfun(id, response, opts) {
	var paging = Ext.JSON.decode(response.responseText);
	if(paging.totalProperty > 0){
		actionHtml = "<table width=\"100%\" style=\"border-spacing:0px;border-collapse: collapse;font-family: '宋体','Arial';font-size: 12px;background: #fff;color: #000;\">"
		+ '<tbody>'
		+ '<tr class="tr25" align="center" bgcolor="#C0D0DD">'
		+	'<td colspan="6">'
		+		'<h3>操作信息</h3>'
		+	'</td>'
		+ '</tr>'
		+ '<tr class="tr25" align="center" bgcolor="#C0D0DD" style="background-color: rgb(234, 236, 253);">'
		+	'<td width="130"><h3>操作者</h3></td>'
		+	'<td width="160"><h3>操作时间</h3></td>'
		+	'<td width="85"><h3>订单状态</h3></td>'
		+	'<td width="85"><h3>付款状态 </h3></td>'
		+	'<td width="125"><h3>发货状态</h3></td>'
		+	'<td><h3>备注</h3></td>'
		+ '</tr>';
		for (var i = 0; i<paging.root.length;i++) {
			var action = paging.root[i];
			actionHtml += '<tr class="tr25">'
				+ '<td>'
				+	'<span style="padding-left:10px;">'+action.actionUser+'</span>'
				+ '</td>'
				+ '<td align="center">'
				+	format(action.logTime)
				+ '</td>'
				+ '<td align="center">'
				+	getOrderStatus(action.orderStatus)
				+ '</td>'
				+ '<td align="center">'
				+	getPayStatus(action.payStatus)
				+ '</td>'
				+ '<td align="center">'
				+	getShipStatus(action.shippingStatus)
				+ '</td>'
				+ '<td>'
				+	'<div style="padding-left:10px;line-height:20px;">'+action.actionNote+'</div>'
				+ '</td>'
				+ '</tr>';
		}
		actionHtml += '</tbody>'
			+'</table>';
		$("#orderActions").html(actionHtml);
	}
}