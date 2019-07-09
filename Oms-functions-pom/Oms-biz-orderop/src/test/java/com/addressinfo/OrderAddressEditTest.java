package com.addressinfo;
import java.io.IOException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.service.OrderDistributeEditService;

public class OrderAddressEditTest {
	 public static void main(String[] args) throws InterruptedException, IOException {  
		ApplicationConfig application = new ApplicationConfig();  
		application.setName("OMS");  
		ReferenceConfig<OrderDistributeEditService> reference = new ReferenceConfig<OrderDistributeEditService>();  
//		reference.setUrl("dubbo://127.0.0.1:8081/com.work.shop.oms.orderop.service.OrderAddressInfoEditService");
		reference.setUrl("json://10.8.39.91:8081/Oms/dubbo/com.work.shop.oms.orderop.service.OrderDistributeEditService");

		
		reference.setTimeout(500);
		reference.setConnections(10);
		reference.setApplication(application);
		reference.setInterface(OrderDistributeEditService.class);
		reference.setVersion("1.0.0");  
		final OrderDistributeEditService blogQueryService = reference.get();
		long begin = System.currentTimeMillis();
		// 服务调用
		ConsigneeModifyInfo consignInfo = new ConsigneeModifyInfo();
		consignInfo.setCountry("1");
		consignInfo.setProvince("310000");
		consignInfo.setCity("310100");
		consignInfo.setDistrict("310115");
		consignInfo.setStreet("");
		consignInfo.setAddress("康桥东路700号");
		consignInfo.setMobile("18917519266");
		consignInfo.setTel("8362");
		consignInfo.setConsignee("测试671");
		consignInfo.setEmail("1");
		consignInfo.setBestTime("1");
		consignInfo.setSignBuilding("1");
		consignInfo.setZipcode("20000");
		consignInfo.setActionUser("HQ01UC771");
//		ReturnInfo info = blogQueryService.editConsigneeInfoByOrderSn("1605021411510081s01", consignInfo, "HQ01UC771");
		
		ReturnInfo info = blogQueryService.editConsigneeInfoByMasterSn("1605021411510081", consignInfo);

		long end = System.currentTimeMillis();
		System.out.println(" cost:isOk=" + info.getIsOk() + ";message=" + info.getMessage());  

		System.out.println(" cost:" + (end - begin));  
		/*ExecutorService es = Executors.newFixedThreadPool(50, new NamedThreadFactory("my test"));  
		List<Callable<String>> tasks = new ArrayList<Callable<String>>();  
		for (int i = 0; i < 100000; ++i) {  
			tasks.add(new Callable<String>() {  
				@Override  
				public String call() throws Exception {  
					System.out.println("run");  
					System.out.println(blogQueryService.test());  
					System.out.println("run success");  
					return null;  
				}  
			});  
		}  
		List<Future<String>> futurelist = es.invokeAll(tasks);  
		for (Future<String> future : futurelist) {
			try {
				String result = future.get();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------\r\n");  
		}
		es.shutdown();  */
		System.out.println("end");  
		System.in.read();  
	}
	static class NamedThreadFactory implements ThreadFactory {
		private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

		private final AtomicInteger mThreadNum = new AtomicInteger(1);

		private final String mPrefix;

		private final boolean mDaemo;

		private final ThreadGroup mGroup;

		public NamedThreadFactory() {
			this("pool-" + POOL_SEQ.getAndIncrement(), false);
		}

		public NamedThreadFactory(String prefix) {
			this(prefix, false);
		}

		public NamedThreadFactory(String prefix, boolean daemo) {
			mPrefix = prefix + "-thread-";
			mDaemo = daemo;
			SecurityManager s = System.getSecurityManager();
			mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s
					.getThreadGroup();
		}

		public Thread newThread(Runnable runnable) {
			String name = mPrefix + mThreadNum.getAndIncrement();
			Thread ret = new Thread(mGroup, runnable, name, 0);
			ret.setDaemon(mDaemo);
			return ret;
		}

		public ThreadGroup getThreadGroup() {
			return mGroup;
		}

	}
}
