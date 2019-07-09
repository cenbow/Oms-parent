String.prototype.toJsonObject = function () {
	return Function("return " + this + ";").call({});
};

var SELECT = {};
//物流问题单原因
mylogistics_question_reason=[
	// {code:"-1" ,name:"请选择"},
	{code:"37" ,name:"短拣"},
	{code:"38" ,name:"短配"},
	{code:"887" ,name:"供应商缺货"},
	{code:"995" ,name:"全缺货问题单"},
	{code:"996" ,name:"部分缺货问题单"},
	{code:"9953" ,name:"分配缺货问题单"}
	/*{code:"40" ,name:"挂起"},
	{code:"53" ,name:"IT系统问题"}*/
];
SELECT.logistics_question_reason = [
	{v: '', n: '请选择...'},
//	{v: '订单数量大于交货数量', n: '订单数量大于交货数量'},
	{v: '短拣', n: '短拣'},
	{v: '短配', n: '短配'},
//	{v: '发货', n: '发货'},
	{v: '挂起', n: '挂起'},
	{v: 'IT系统问题', n: 'IT系统问题'}
//	{v: '无法确认分拣单', n: '无法确认分拣单'},
//	{v: '重打快递单', n: '重打快递单'}
];
//物流问题单处理状态
SELECT.logistics_question_process_status = [
	{v: '-1', n: '请选择...'},
	{v: '0', n: '待处理'},
	{v: '1', n: '已处理'},
	{v: '2', n: '无需处理'}
];


SELECT.export_type = [                         
             		{v : '0', n : '订单号'},
             		{v : '1', n : '交易号'},           	
];

//0:无须退款；1:需要退款'
SELECT.have_hefund = [                         
               		{v : '0', n : '无须退款'},
               		{v : '1', n : '需要退款'},           	
  ];

//问题单处理状态
SELECT.process_status=[
//	{v: '-1', n: '请选择...'},
	{v: '0', n: '待处理'},
	{v: '1', n: '已处理'}
];

//交易类型，订单类型
SELECT.trans_type = [
		{v : '-1', n : '请选择...'},
		{v : '1', n : '款到发货'},
		{v : '2', n : '货到付款'},
		{v : '3', n : '担保交易'}
];
//订单类型
SELECT.order_type = [
        {v : '-1', n : '请选择...'},
		{v : '0', n : '正常订单'},
		/*{v : '1', n : '补发订单'},*/
		{v : '2', n : '换货订单'}
];

//订单和退单分类
SELECT.order_sn_type = [
        {v : '-1', n : '请选择...'},
//		{v : '0', n : '正常订单'},
		{v : '1', n : '订单号'},
		{v : '2', n : '退单号'}
];

//订单状态
SELECT.order_status = [
		{v : '-1', n : '请选择...'},
		{v : '0', n : '未确认', c : '1'},
		{v : '1', n : '已确认', c : '1'},
		{v : '2', n : '已取消'},
		{v : '3', n : '完成'}
];

SELECT.resource_type = [
               		{v : '-1', n : '请选择...'},
               		{v : 'desktop_group', n : 'desktop_group'},
             		{v : 'group', n : 'group'},
               		{v : 'url', n : 'url'},
               		{v : 'act', n : 'act'}
];

SELECT.is_show = [
                   		{v : '-1', n : '请选择...'},
                 		{v : '1', n : '是'},
                   		{v : '0', n : '否'}
                   		  
    ];


SELECT.haveRefund = [
               		{v : '-1', n : '请选择...'},
             		{v : '1', n : '是'},
               		{v : '0', n : '否'}
               		  
];

SELECT.is_show_1 = [
             		{v : -1, n : '请选择...'},
             		{v : 1, n : '是'},
             		{v : 0, n : '否'}
             		  
];

//是否收到货
SELECT.is_show_2  = [
             		{v : -1, n : '请选择...'},
             		{v : 1, n : '收到货'},
             		{v : 0, n : '未收到货'},
             		{v : 2, n : '部分收到货'}
             		  
];

//质检状态
SELECT.qualityStatus = [
             		{v : -1, n : '请选择...'},
             		{v : 1, n : '质检通过'},
             		{v : 0, n : '质检不通过'},
             		{v : 2, n : '部分质检通过'},
             		  
];


//0未入库 1已入库 2待入库
SELECT.checkin_status = [
                 		{v : -1, n : '请选择...'},
                 		{v : 1, n : '已入库'},
                 		{v : 0, n : '未入库'},
                 		{v : 2, n : '待入库'},
                 		{v : 3, n : '部分入库'}
                 		  
];


//退单质检状态 （0质检不通过、1质检通过）',
SELECT.quality_status = [
                 		{v : -1, n : '请选择...'},
                 		{v : 1, n : '质检通过'},
                 		{v : 0, n : '质检不通过'},
                 		{v : 2, n : '部分质检通过'}
          		  
];


//0未收货 1 已收货
SELECT.is_Good_Received = [
                 		{v : -1, n : '请选择...'},
                 		{v : 0, n : '未收货'},
                 		{v : 1, n : '已收货'}
 ];

//订单支付状态
SELECT.pay_status = [
		{v : '-1', n : '请选择...'},
		{v : '0', n : '未付款', c : '1'},
		{v : '1', n : '部分付款'},
		{v : '2', n : '已付款', c : '1'},
		{v : '3', n : '已结算'}
];

//付款单支付状态 （0，未结算；1，已结算；2，待结算）
SELECT.pay_status_pay = [
        {v : '0', n : '未付款', c : '1'},
        {v : '1', n : '付款中'},
        {v : '2', n : '已付款', c : '1'},
        {v : '3', n : '已结算'}
 ];
 
//订单的发货状态-发货状态发 货总状态（0，未发货；1，备货中；2，部分发货；3，已发货；4，部分收货；5，客户已收货）
SELECT.ship_status = [
		{v : '-1', n : '请选择...'},
		{v : '0', n : '未发货', c : '1'},
		{v : '2', n : '部分发货'},
		{v : '3', n : '已发货'},
		{v : '4', n : '部分收货'},
		{v : '5', n : '已收货'}
];

//发货单的发货状态发 货总状态（商品配送情况（0，未发货；1，已发货；2，已收货；3，备货中；10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁））
SELECT.ship_status_ship = [
		{v : '0', n : '未发货', c : '1'},
		{v : '1', n : '已发货'},
		{v : '2', n : '已收货'},
		{v : '3', n : '备货中'},
		{v : '10', n : '快递取件'},
		{v : '11', n : '运输中'},
		{v : '12', n : '派件中'},
		{v : '13', n : '客户签收'},
		{v : '14', n : '客户拒签'},
		{v : '15', n : '货物遗失'},
		{v : '16', n : '货物损毁'}
];

//处理类型
//处理状态 （0无操作，1退货，2换货）
SELECT.process_type = [
        {v : '-1', n : '请选择...'},
        {v : '0', n : '无操作'},
        {v : '1', n : '退货'},
        {v : '2', n : '换货'}
];
//分仓发货状态（0，未分仓 1，已分仓未通知 2，已分仓已通知）
SELECT.depot_status = [
        {v : '-1', n : '请选择...'},
        {v : '0', n : '未分仓'},
        {v : '2', n : '已分仓'}
];

//问题单状态（0：正常单 1：问题单）
SELECT.question_status = [
        {v : '-1', n : '请选择...'},
        {v : '0', n : '正常单'},
        {v : '1', n : '问题单'}
];

//订单输入导出类型
SELECT.order_info_input_template_type = [
                       {v : '-1', n : '请选择...'},
                       { v: '0', n: '输入订单号'},
                       {v: '1', n: '输入外部交易号'}
                     
               ];


//回退客服
SELECT.back_to_cs = [
       {v : '-1', n : '请选择...'},
       {v : '0', n : '否'},
       {v : '1', n : '是'}
 ];
//退单类型  ：1退货单、2拒收入库单、3退款单、4额外退款单
SELECT.return_type = [
       {v : '-1', n : '请选择...'},
       {v : '1', n : '退货单'},
       {v : '2', n : '拒收入库单'},
       {v : '3', n : '退款单'},
       {v : '5', n : '承运商失货单'},
       {v : '4', n : '额外退款单'}
 ];
//预付款/保证金
SELECT.return_settlement_type = [
                      {v : '-1', n : '请选择...'},
                      {v : '1', n : '预退款'},
                      {v : '2', n : '保证金'}
];
//退款方式
SELECT.return_pay = [
                                 {v : '-1', n : '请选择...'},
                                 {v : '1', n : '支付宝'},
                                 {v : '2', n : '网银在线'},
                                 {v : '3', n : '全部余额支付'},
                                 {v : '4', n : '货到付款'},
                                 {v : '5', n : '银联在线支付'},
                                 {v : '6', n : '财付通'},
                                 {v : '7', n : '快钱人民币网关'},
                                 {v : '8', n : '银行汇款/转帐'}, 
                                 {v : '9', n : '邮局汇款'},
                                 {v : '10', n : '财付通中介担保接口'},
                                 {v : '11', n : '贝宝支付'},
                                 {v : '12', n : '招商银行'},
                                 {v : '13', n : '一号店'},
                                 {v : '14', n : '汇付天下'},
                                 {v : '15', n : '手机银联支付'},
                                 {v : '16', n : '京东支付'},
                                 {v : '17', n : '苏宁支付'},
                                 {v : '18', n : 'LC风格网'},
                                 {v : '19', n : '微信支付'},
                                 {v : '20', n : '1号店'},
                                 {v : '21', n : '门店支付'},
                                 {v : '22', n : '当当支付'},
                                 {v : '23', n : '聚美支付'},
                                 {v : '24', n : '爱奇艺支付'},
                                 {v : '25', n : '京东优惠'},
                                 {v : '26', n : '退单转入款'}
        
 ];
//退单状态：0未确认、1已确认、2质检通过、3质检不通过、4无效、5已提交处理意见、6未提交处理意见、7终审通过、8终审不通过、9已修补、10已完成
SELECT.return_order_status = [
                     {v : '-1', n : '请选择...'},
                     {v : '0', n : '未确认'},
                     {v : '1', n : '已确认'},
                   /*  {v : '2', n : '质检通过'},
                     {v : '3', n : '质检不通过'},*/
                     {v : '4', n : '无效'},
                   /*  {v : '5', n : '已提交处理意见'},*/
                     {v : '10', n : '已完成'}
];
//财务状态 （0，未结算；1，已结算；2，待结算）
SELECT.pay_status_2 = [
                                 {v : '-1', n : '请选择...'},
                                 {v : '0', n : '未结算'},
                                 {v : '1', n : '已结算'},
                                 {v : '2', n : '待结算'}
 ];
//物流状态 （0，未收货；1，已收货,未入库；2，已入库；3，可入库；4，快递收货,仓库未收货）
SELECT.ship_status_2 = [
                     {v : '-1', n : '请选择...'},
                     {v : '0', n : '未收货'},
                     {v : '1', n : '已入库'},
                     {v : '2', n : '待入库'}//,
                    // {v : '3', n : '已入库'}
                //     {v : '3', n : '可入库'},
                //     {v : '4', n : '快递收货,仓库未收货'}
];

/*SELECT.checkin_status = [
                        {v : '-1', n : '请选择...'},
                        {v : '0', n : '未收货'},
                        {v : '1', n : '已入库'},
                        {v : '2', n : '待入库'}//,
                       // {v : '3', n : '已入库'}
                   //     {v : '3', n : '可入库'},
                   //     {v : '4', n : '快递收货,仓库未收货'}
   ];*/


//退单原因
SELECT.return_reason = [
                     {v : '-1', n : '请选择...'},
                     {v : '21', n : '质量问题'},
                     {v : '22', n : '尺寸不合适'},
                     {v : '23', n : '颜色不满意'},
                     {v : '24', n : '款式、设计不满意'},
                     {v : '25', n : '色差'},
                     {v : '26', n : '材质不满意'},
                     {v : '27', n : '实物与描述不符'},
                     {v : '28', n : '吊牌错'},
                     {v : '29', n : '网站信息（图片等）出错'},
                     {v : '30', n : '价格问题（其他渠道便宜、降价）'},
                     {v : '31', n : '快递问题（服务态度、送货慢等）'},
                     {v : '32', n : '市场活动不满'},
                     {v : '33', n : '其他（无理由退货等）'},
                     {v : '34', n : '错漏发货'}
];

//退换类型，1：退货；2：换货
SELECT.goods_return_change_return_type = [
					{v : '-1', n : '请选择...'},
					{v : '1', n : '退货'},
					{v : '2', n : '换货'}
];

//退换原因，1：商品质量不过关；2：商品在配送中损坏；3：商品与描述不符；4：尚未收到商品；5：其他（请具体说明）
SELECT.goods_return_change_reason = [
					{v : '-1', n : '请选择...'},
					{v : '1', n : '商品质量不过关'},
					{v : '2', n : '商品在配送中损坏'},
					{v : '3', n : '商品与描述不符'},
					{v : '4', n : '尚未收到商品'},
					{v : '5', n : '其他（请具体说明）'}
];

//换购选择类型，1：换购本商品其他尺码或颜色；2：换购其他商品
SELECT.goods_return_change_redemption = [
					{v : '-1', n : '请选择...'},
					{v : '1', n : '换购本商品其他尺码或颜色'},
					{v : '2', n : '换购其他商品'}
];

//吊牌情况，1：吊牌完好；2：吊牌破损；3：无吊牌
SELECT.goods_return_change_tag_type = [
					{v : '-1', n : '请选择...'},
					{v : '1', n : '吊牌完好'},
					{v : '2', n : '吊牌破损'},
					{v : '3', n : '无吊牌'}
];

//外观情况，1：外观完好；2：外观有破损；3：外观有污渍
SELECT.goods_return_change_exterior_type = [
					{v : '-1', n : '请选择...'},
					{v : '1', n : '外观完好'},
					{v : '2', n : '外观有破损'},
					{v : '3', n : '外观有污渍'}
];

//赠品情况，1：赠品完好；2：赠品破损；3：赠品不全；4：未收到赠品
SELECT.goods_return_change_gift_type = [
					{v : '-1', n : '请选择...'},
					{v : '1', n : '赠品完好'},
					{v : '2', n : '赠品破损'},
					{v : '3', n : '赠品不全'},
					{v : '4', n : '未收到赠品'}
];

//状态：0：已取消；1：待沟通；2：已完成；3：待处理
SELECT.goods_return_change_status = [
					{v : '-1', n : '请选择...'},
					{v : '0', n : '已取消', c : '1'},
					{v : '1', n : '待沟通', c : '1'},
					{v : '2', n : '已完成', c : '1'},
					{v : '3', n : '待处理', c : '1'}
];

//订单种类：1：零售；2：物资领用；3：其它出库
SELECT.order_category = [
	{v : '-1', n : '请选择...'},
	{v : '1', n : '零售'},
	{v : '2', n : '物资领用'},
	{v : '3', n : '其它出库'}/*,
	{v : '4', n : 'C2b定制'}*/
];

//单据状态: 1.未审核   ; 2.已审核  ;3.已移除   ; 4.已执行   ;
SELECT.documents_state = [
                     	{v : '-1', n : '请选择...'},
                     	{v : '1', n : '未审核'},
                     	{v : '2', n : '已审核 '},
                     	{v : '3', n : '已移除'},
                     	{v : '4', n : '已执行'}
];

//'1,预付款，2保证金',
SELECT.return_settlement_type = [
                       	{v : '-1', n : '请选择...'},
                       
                       	{v : '1', n : '预付款 '},
                    	{v : '2', n : '保证金'}
                   
  ];

//单据状态:  '1已经同步  0未同步  2作废',
SELECT.documents_settlement_state = [
                       	{v : '-1', n : '请选择...'},
                       	{v : '0', n : '未同步 '},
                       	{v : '1', n : '已经同步  '},
                       	{v : '2', n : '作废'}, 
                     	{v : '3', n : '部分同步'},  
                    	{v : '4', n : '同步失败'}, 
                    	{v : '9', n : '同步中'} 
];

//单据状态:  '1已经同步  0未同步  2作废',
SELECT.documents_settlement_bill_type = [
                       	{v : '-1', n : '请选择...'},
                //      {v : '0', n : '未定义 '},
                       	{v : '1', n : '订单结算  '},
                       	{v : '2', n : '订单货到付款结算'}// ,   
                //    	{v : '3', n : '退单退款方式结算'}       
];

//订单原因：S：零售；J：赠送（对外关系维护）；T：承运商失货
SELECT.reason = [
	{v : 'S', n : '零售'},
	{v : 'J', n : '赠送（对外关系维护）'},
	{v : 'T', n : '承运商失货'}
];

//退货仓库信息
SELECT.depot_code = [
					{v : 'HQ01W503', n : 'MB上海区域处理仓'},
					{v : 'HQ01W703', n : 'MC上海区域处理仓'},
					{v : 'HQ01W572', n : 'MB天津区域处理仓'},
					{v : 'HQ01W523', n : 'MB成都区域处理仓'},
					{v : 'HQ01W533', n : 'MB沈阳区域处理仓'},
					{v : 'HQ01W542', n : 'MB东莞区域处理仓'},
					{v : 'HQ01W553', n : 'MB西安区域处理仓'},
					{v : 'HQ01W565', n : 'MB重庆区域处理仓'},
					{v : 'HQ01W723', n : 'MC成都区域处理仓'},
					{v : 'HQ01W733', n : 'MC沈阳区域处理仓'},
					{v : 'HQ01W754', n : 'MC西安区域处理仓'},
					{v : 'HQ01W763', n : 'MC重庆区域处理仓'},
					{v : 'HQ01W773', n : 'MC天津区域处理仓'},
					{v : 'HQ06W002', n : 'MB温州公司处理仓'}
];


// 团购订单：0：非团购订单；1：团购订单
SELECT.group_order = [
                    {v : '-1', n : '请选择...', c : '1'},
					{v : '0', n : '否', c : '1'},
					{v : '1', n : '是', c : '1'}
];

// 预售商品团购订单：0：非预售团购订单；1：预售团购订单
SELECT.pre_group_order = [
                    {v : '-1', n : '请选择...', c : '1'},
					{v : '0', n : '否', c : '1'},
					{v : '1', n : '是', c : '1'}
];


SELECT.split_status = [
	{v : '-1', n : '请选择...', c : '1'},
	{v : '0', n : '未拆单', c : '1'},
	{v : '2', n : '已拆单', c : '1'}
];




//店铺渠道类型："1":"线上直营渠道","2":"线上加盟渠道","3":"线下直营渠道","4":"线下加盟渠道"
SELECT.channel_type = [
	{v : '-1', n : '请选择...', c : '1'},
	{v : '1', n : '线上直营渠道', c : '1'},
	{v : '2', n : '线上加盟渠道', c : '1'},
	{v : '3', n : '线下直营渠道', c : '1'},
	{v : '4', n : '线下加盟渠道', c : '1'}
];
SELECT.resourceType=[
{v : '-1', n : '请选择...'},
{v : 'code', n : 'code'},
{v : 'group', n : 'group'},
{v : 'url', n : 'url'},
{v : 'action', n : 'action'}
                    ];
//订单来源媒体：'wap': 'WAP', '手机': '手机','淘宝品牌特卖':'淘宝品牌特卖','iPad移动商城':'iPad移动商城','全流通':'全流通'
SELECT.referer_type = [
	{v : '-1', n : '请选择...', c : '1'},
	{v : 'wap', n : 'wap', c : '1'},
	{v : '手机应用渠道', n : '手机', c : '1'},
	{v : 'iPad移动商城', n : 'iPad移动商城', c : '1'},
	{v : '移动ANDROID应用渠道', n : '移动ANDROID应用渠道', c : '1'},
	{v : '移动IPAD应用渠道', n : '移动IPAD应用渠道', c : '1'},
	{v : '移动IOS应用渠道', n : '移动IOS应用渠道', c : '1'}
];
//列表页每页显示条数列表： 20 50 100 500 1000

SELECT.query_pagesize_type = [
	{v: '20', n: '20条'},
	{v: '50', n: '50条'},
	{v: '100', n: '100条'},
	{v: '500', n: '500条'},
	{v: '1000', n: '1000条'}
];

//是否选项
SELECT.is_or_not = [
    {v:'0',n:'否'},
    {v:'1',n:'是'}
];

//查询条件中的是否选项
SELECT.is_or_not_for_query = [
	{v:'',n:'请选择'},
    {v:'0',n:'否'},
    {v:'1',n:'是'}
];

//查询条件：同步状态
SELECT.is_syn_state = [
                       	{v : '', n : '请选择'},
                       	{v : '0', n : '未同步 '},
                       	{v : '1', n : '已经同步  '},
                       	{v : '2', n : '作废'}, 
                     	{v : '3', n : '部分同步'},  
                    	{v : '4', n : '同步失败'}, 
                    	{v : '9', n : '同步中'} 
];

//订单列表查询条件：source
SELECT.source = [
	              	{v:'',n:'请选择'},
	        //        {v:'0',n:'未处理'},
	                {v:'1',n:'全流通'},
	                {v:'2',n:'云货架'},
	                {v:'3',n:'B2C线上订单'},
	                {v:'4',n:'C2M'},
	                {v:'5',n:'C2B'}
	            ];

function getValue(arr, val, isColor){
	for(var i in arr){
		var o = arr[i];
		if(o.v == val){
			if(o.c && isColor && o.c == 1){
				return '<font style="color:red;">' + o.n + '</font>';
			}else{
				return o.n;
			}
		}
	}
	return '';
}
/**
* 获取订单状态
* @os 订单状态
*/
function getOrderStatus(os){
	var needRed_os = '[0][1]';
	var osStr = getValue(SELECT.order_status, os);
	if(needRed_os.indexOf(os) > -1){
		osStr = '<font style="color:red;">' + osStr + '</font>';
	}
	return osStr;
}

/**
* 获取订单付款状态
* @ps 订单付款状态
*/
function getPayStatus(ps){
	var needRed_ps = '[0][2]';
	var psStr = getValue(SELECT.pay_status, ps);

	if(needRed_ps.indexOf(ps) > -1){
		psStr = '<font style="color:red;">' + psStr + '</font>';
	}
	return psStr;
}

/**
* 获取订单支付状态
* @ss 订单支付状态
*/
function getShipStatus(ss){
	var needRed_ss = '[0]';
	var ssStr = getValue(SELECT.ship_status, ss);
	if(needRed_ss.indexOf(ss) > -1){
		ssStr = '<font style="color:red;">' + ssStr + '</font>';
	}
	return ssStr;
}

/**
* 获取订单组合状态
* @os 订单状态
* @ps 订单付款状态
* @ss 订单支付状态
*/
function getCombineStatus(os, ps, ss){
	var needRed_os = '[0][1]';
	var needRed_ps = '[0][2]';
	var needRed_ss = '[0]';
	var osStr = getValue(SELECT.order_status, os);
	var psStr = getValue(SELECT.pay_status, ps);
	var ssStr = getValue(SELECT.ship_status, ss);

	if(needRed_os.indexOf(os) > -1){
		osStr = '<font style="color:red;">' + osStr + '</font>';
	}
	if(needRed_ps.indexOf(ps) > -1){
		psStr = '<font style="color:red;">' + psStr + '</font>';
	}
	if(needRed_ss.indexOf(ss) > -1){
		ssStr = '<font style="color:red;">' + ssStr + '</font>';
	}
	return osStr + ' , ' + psStr + ' , ' + ssStr;
}
function initSelect(id, tar,selectedIndex, beginIndex){
	var tars = SELECT[tar];
	var html_ = '';
	beginIndex = beginIndex || 0;
	for(var i = beginIndex ; i < tars.length; i = i + 1){
		var o = tars[i];
		if(o.not && o.not == 1){
			continue;
		}
		if(selectedIndex == i){
			html_ += '<option value="'+o.v+'" selected >'+o.n+'</option>';
		}else{
			html_ += '<option value="'+o.v+'">'+o.n+'</option>';
		}
		
	}
	$(id).html(html_);
}
function mapTypes(id, type,selectedIndex){
	var typeStr = null;
	typeStr = type.toLowerCase();
	initSelect(id, typeStr,selectedIndex);
}

/**
 * 返回数组数据
 * @param datajson
 * @returns {Array}
 */
function getdata(datajson){
	var arrayObj = new Array();
	for(var i=0;i<datajson.length;i++){
		arrayObj. push([datajson[i].v,datajson[i].n]);
	}
	return arrayObj;
}
/**
* type :表示下拉框类型、 id:表示下拉框标签Id 、 selected：表示默认选择第几个Option
* @fields [{type:'',id:'#aaa',selected:'0'},{type:'',id:'#bbb',selected:'0'},{type:'',id:'#ccc',selected:'0'},{type:'',id:'#ddd',selected:'0'}......]
*/
function initAllSelects(param){
	var fields = '';
	for(var i = 0; i < param.length; i += 1){
		fields += param[i].type;
		if(i < param.length -1){
			fields += ',';
		}
	}

	var url = 'getCommonSelectValue?type=' + fields;
	$.ajax({
		type : 'GET',
		url : url,
		success : function(json){
			var obj = json.toJsonObject();
			for(var i = 0; i < param.length; i += 1){
				var type = param[i].type;
				var id = param[i].id;
				var selectedIndex = param[i].selected;
				if(isNaN(selectedIndex)){
					selectedIndex = 0;
				}
				if(obj[type]){
					var os = obj[type];
					var html_ = '';
					for(var j = 0; j < os.length; j += 1){
						var o = os[j];
						if(selectedIndex == j){
							html_ += '<option value="'+o.v+'" selected >'+o.n+'</option>';
						}else{
							html_ += '<option value="'+o.v+'">'+o.n+'</option>';
						}
						
					}
					$(id).html(html_);
				}else{
					mapTypes(id, type,selectedIndex);
				}
			}
		}
	});
}

/**
 * 商品扩展属性转换
 * 满增活动 gift1 必赠活动 gift2 买赠活动 gift3 套装活动 gift4 集合赠活动 gift5 集合满增活动 gift6
 * 其中套装活动当作普通商品对待
**/
function extensionCodeChange(ec) {
	if (ec == 'group') {
		return '套装';
	} else if (ec == 'gift' || ec == 'gift1' || ec == 'gift2' || ec == 'gift3'
			|| ec == 'gift5' || ec == 'gift6') {
		return '赠品';
	} else {
		return '普通商品';
	}
}

/**
* 获取退单订单组合状态
* @os 退单状态
* @ps 退单支付状态
* @ss 退单发货状态
*/
function getCombineReturnStatus(os, ps, ss){
	var osStr = getValue(SELECT.return_order_status, os);
	var psStr = getValue(SELECT.pay_status_2, ps);
	var ssStr = getValue(SELECT.checkin_status, ss);
	if(os != 4 && os != 10){
		osStr = '<font style="color:red;">' + osStr + '</font>';
	}
	if(ps != 1){
		psStr = '<font style="color:red;">' + psStr + '</font>';
	}
	if(ss == 0 || ss == 1){
		ssStr = '<font style="color:red;">' + ssStr + '</font>';
	}
	return osStr + ' , ' + psStr + ' , ' + ssStr;
}

function getCombineReturnStatusNew(os, ps, is,cs,qs){
	var osStr = getValue(SELECT.return_order_status, os);
	var psStr = getValue(SELECT.pay_status_2, ps);
	var ssStr = getValue(SELECT.checkin_status, cs); 	//是否入库
	
	if(os != 4 && os != 10){
		osStr = '<font style="color:red;">' + osStr + '</font>';
	}
	if(ps != 1){
		psStr = '<font style="color:red;">' + psStr + '</font>';
	}
	return osStr + ' , ' + psStr + ' , ' +ssStr;
}

function getHaveRefundStatus(haveRefund){
	
	var isStr = getValue(SELECT.have_hefund, haveRefund);
	return isStr;
}


/**
 * 新窗口 - 打开
 * @param imageUrl
 */
function showBigPic(imageUrl){ 
    window.open(imageUrl.src,"image",'fullscreen=1,top=0,left=0,height=600,width=1000, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');  
}  

/**
 * 返回a标签
 * @param url
 * @param value
 * @returns {String}
 */
function returnALabel(url,value){
	return "<a href=" + url + " target='_blank' >" + value + "</a>";
}

//查看方式
SELECT.order_return_view = [
     //   {v : '-1', n : '请选择...'},
		{v : '0', n : '默认显示有效退单'},
		{v : '1', n : '显示全部退单'},
		{v : '2', n : '显示隐藏退单'}
];

SELECT.MainChild_type = [
                   	{v : '1', n : '主单'},
                   	{v : '2', n : '子单'}
                   ];

//查看方式
SELECT.order_view = [
     //   {v : '-1', n : '请选择...'},
		{v : '0', n : '默认显示有效订单'},
		{v : '1', n : '显示全部订单'},
		{v : '2', n : '显示隐藏订单'}
];


SELECT.hand_order_process_status=[
	{v: '-1', n: '请选择...'},
	{v: '0', n: '未审核'},
	{v: '1', n: '已审核'},
	{v: '2', n: '已打单'}
];

SELECT.create_order_status=[
	{v: '-1', n: '请选择...'},
	{v: '0', n: '未处理'},
	{v: '1', n: '全部成功'},
	{v: '2', n: '部分成功'},
	{v: '3', n: '全部失败'}
];
// 打单类型 4:首购赠品订单;5:一般赠品订单
SELECT.hand_order_source_type=[
	{v: '-1', n: '请选择...'},
	{v: '4', n: '首购赠品订单'},
	{v: '5', n: '一般赠品订单'}
];