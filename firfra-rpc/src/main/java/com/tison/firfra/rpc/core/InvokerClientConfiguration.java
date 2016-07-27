package com.tison.firfra.rpc.core;

/**
 * 远程调用客户端发起请求的配置接口
 * 
 * @author Tian Guangxin(Tison)
 * @date 2016年7月26日
 * @version 1.0
 */
public interface InvokerClientConfiguration {
	
	InvokerRequestExecutor getInvokerRequestExecutor();
	
	void setInvokerRequestExecutor(InvokerRequestExecutor ire);
	
	/**
	 * 可以不继承父类来添加附加的属性
	 * @param key
	 * @param attribute
	 */
	void addAttribute(String key,Object attribute);
	
	Object getAttribute(String key);
}
