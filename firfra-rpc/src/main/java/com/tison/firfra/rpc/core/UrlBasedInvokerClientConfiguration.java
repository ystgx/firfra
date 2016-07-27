package com.tison.firfra.rpc.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * 通过URL发起远程调用的客户端请求配置接口
 * @author Tian Guangxin(Tison)
 * @date 2016年7月26日
 * @version 1.0
 */
public class UrlBasedInvokerClientConfiguration implements InvokerClientConfiguration{
	private InvokerRequestExecutor invokerRequestExecutor;
	
	private String serviceUrl;
	
	private Map<String,Object> attributes = new HashMap<String,Object>(10);
	
	public UrlBasedInvokerClientConfiguration(InvokerRequestExecutor invokerRequestExecutor,String serviceUrl){
		Assert.notNull(invokerRequestExecutor,"InvokerRequestExecutor can not be null");
		Assert.hasText(serviceUrl,"serviceUrl must have content");
		this.invokerRequestExecutor = invokerRequestExecutor;
		this.serviceUrl = serviceUrl;
	}
	
	/**
	 * 返回远程服务的URL
	 */
	public String getServiceUrl(){
		return this.serviceUrl;
	}
	
	public void setServiceUrl(String url){
		this.serviceUrl = url;
	}
	
	@Override
	public InvokerRequestExecutor getInvokerRequestExecutor() {
		return this.invokerRequestExecutor;
	}
	
	@Override
	public void setInvokerRequestExecutor(InvokerRequestExecutor ire){
		this.invokerRequestExecutor = ire;
	}
	
	@Override
	public void addAttribute(String key,Object attribute){
		Assert.notNull(key,"attribute key can not be null");
		Assert.notNull(attribute,"attribute can not be null");
		attributes.put(key, attribute);
	}
	
	@Override
	public Object getAttribute(String key){
		return attributes.get(key);
	}
}
