import com.alibaba.fastjson.JSON;
import com.work.shop.oms.webservice.ErpWebserviceResultBean;


public class WsTest {

	
	public static void main(String[] args) {
		String msg = "{\"code\":2,\"message\":\"aaaa\"}";
		ErpWebserviceResultBean resBean = JSON.parseObject(msg, ErpWebserviceResultBean.class);
		System.out.println(resBean);
	}
}
