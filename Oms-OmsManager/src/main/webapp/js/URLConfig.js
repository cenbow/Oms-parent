var URLConfig = new Object();

//81:8075     82:8095
//URLConfig.baseURL = "http://192.168.149.115:8095";
//URLConfig.baseURL = "http://10.8.21.15:8095"; (new)
//URLConfig.baseURL = "http://10.80.12.16";
//URLConfig.baseURL = "http://os.bang-go.com.cn";
//URLConfig.baseURL = "http://10.80.18.221";(clone)
//URLConfig.baseURL = "http://10.5.201.140";(taobao_clone)
URLConfig.baseURL = "http://os.bang-go.com.cn";
URLConfig.showGoodsDetailURL = URLConfig.baseURL + '/admin/chl_goods.php';
//URLConfig.showGoodsInventoryURL = URLConfig.baseURL + '/admin/chl_inventory_manage.php';
URLConfig.showGoodsInventoryURL =  '/manager/addOrderStockList';
URLConfig.printOrderBySnURL = URLConfig.baseURL + '/admin/order.php';
URLConfig.ERP_queryURL = URLConfig.baseURL + '/admin/erp_order_info.php';
//order.bang-go.com.cn online
//192.168.188.131:81 (8075)
//192.168.188.131:82 (8095)
//10.100.200.44:82 (8095)(new)
//10.80.18.219:82 (clone)
//10.5.201.202:8080 (taobao_clone)
URLConfig.ERP_shipQueryURL = "http://order.bang-go.com.cn/BGOrderManager/manager/erpShipInfo?";