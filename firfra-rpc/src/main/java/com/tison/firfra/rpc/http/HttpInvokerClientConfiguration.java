package com.tison.firfra.rpc.http;

import com.tison.firfra.rpc.core.InvokerRequestExecutor;
import com.tison.firfra.rpc.core.UrlBasedInvokerClientConfiguration;


/**
 * 
 * @author Tian Guangxin(Tison)
 * @date 2016年7月26日
 * @version 1.0
 */
public class HttpInvokerClientConfiguration  extends UrlBasedInvokerClientConfiguration{
	 
	public HttpInvokerClientConfiguration(InvokerRequestExecutor invokerRequestExecutor, String serviceUrl) {
		super(invokerRequestExecutor, serviceUrl);
	}

	/** Http 请求方法  */
	private HttpMethod httpMethod = HttpMethod.GET;
	
	public  HttpMethod getHttpMethod(){
		return this.httpMethod;
	}
	
	public enum HttpMethod{
		GET,POST;
	}

}
