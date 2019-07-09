package com.work.shop.oms.common.bean;

/**
 * 平台币占用返回状态码
 * @author
 *
 */
public enum OcpbStatus {
	/**
	 * 占用成功
	 */
	success {
		@Override
		public int getValue() {
			return 0;
		}
	},
	/**
	 * 占用失败原因是由于平台币账户被锁定
	 */
	lock {
		@Override
		public int getValue() {
			return 1;
		}
	},
	/**
	 * 忽略
	 */
	ignore {
		@Override
		public int getValue() {
			return 2;
		}
	};
	
	public abstract int getValue();
	
}
