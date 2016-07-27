package com.tison.firfra.rpc.core;

/**
 * 远程调用返回结果
 * @author Tian Guangxin(Tison)
 * @date 2016年7月26日
 * @version 1.0
 */
public interface RemoteInvocationResult {
	
	void setValue(Object value);
	
	/**
	 * 获取远程调用返回的结果
	 */
	Object getValue();
	
	void setException(Throwable exception);
	
	/**
	 * 获取远程调用过程中出现的异常，没有异常则返回null
	 */
	Throwable getException();
	
	/**
	 * 判断远程调用过程中是否出现异常
	 */
	boolean hasException();
}
