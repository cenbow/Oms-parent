package com.work.shop.oms.dubbofilter;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;

@Activate
public class DubboReqFilter implements Filter{
	private static final Logger logger = Logger.getLogger(DubboReqFilter.class);
	
	private static final String MethodNames = "decode,decode_array,createOrder,createOrders";

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		// before filter ...
		// TODO 白黑名单管理
		if (StringUtil.isNotEmpty(invocation.getMethodName())) {
			if (MethodNames.indexOf(invocation.getMethodName()) != -1) {
				String clientIp = RpcContext.getContext().getRemoteHost();
				logger.info("clientIp=" + clientIp + ";time:" + TimeUtil.formatDate(new Date())
						+ ";method=" + invocation.getMethodName() + ";param=" + JSON.toJSONString(invocation.getArguments()));
			}
		}
		Result result = invoker.invoke(invocation);
		// after filter ...
		return result;
	}
	
	public static void main(String[] args) {
		String MethodNames = "decode,decode_array,createOrder";
		System.out.println(MethodNames.indexOf("decode") != -1);
	}
}
