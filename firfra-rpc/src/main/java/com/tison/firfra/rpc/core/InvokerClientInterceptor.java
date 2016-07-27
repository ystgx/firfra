package com.tison.firfra.rpc.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.remoting.support.RemoteAccessor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.tison.firfra.rpc.core.annotation.InvokerConfig;

/**
 * Rpc
 * @author Tian Guangxin(Tison)
 * @date 2016年7月27日
 * @version 1.0
 */
public class InvokerClientInterceptor extends RemoteAccessor
		implements InitializingBean, MethodInterceptor, ApplicationContextAware{
	
	protected Map<Method,InvokerMethodConfigHolder> methodConfigMapping = new HashMap<Method,InvokerMethodConfigHolder>(32);
	
	protected ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	private void resolveConfig(){
		Class<?> serviceInterface = getServiceInterface();
		Assert.notNull(serviceInterface, "serviceInterface can not be null");
		InvokerConfig globalConfig = serviceInterface.getAnnotation(InvokerConfig.class);
		
	}
	
	private void resulveInvokerMethodConfig(Class<?> serviceInterface,InvokerConfig globalConfig){
		final Set<Method> invokerMethods = new LinkedHashSet<Method>();
		
		String globalExcutorRef = globalConfig.excutorRef();
		String globalServiceUrl = globalConfig.value();
		
		Method [] methods =  ReflectionUtils.getAllDeclaredMethods(serviceInterface);
		for(Method method : methods){
			InvokerConfig config = AnnotationUtils.findAnnotation(method, InvokerConfig.class);
			if(config != null){
				String excutorRef =  config.excutorRef();
				String serviceUrl = combineServiceUrl(globalServiceUrl,config.value());
			}
		}
	}
	
	private String combineServiceUrl(String globalServiceUrl,String serviceUrl){
		String typeValue = StringUtils.hasText(globalServiceUrl) ? globalServiceUrl : "";
		String methodValue = StringUtils.hasText(serviceUrl) ? serviceUrl : "";
		typeValue = applicationContext.getEnvironment().resolveRequiredPlaceholders(typeValue);
		methodValue	= applicationContext.getEnvironment().resolveRequiredPlaceholders(methodValue);	
		if(typeValue.endsWith("\\")){
			typeValue = typeValue.substring(0, typeValue.length()-1);
		}
		if(methodValue.startsWith("\\")){
			methodValue = methodValue.substring(1, typeValue.length());
		}
		return typeValue.concat(methodValue);
	}
	
	public class InvokerMethodConfigHolder{
		public InvokerClientConfiguration icc;
		
		public InvokerMethod im;
	}

}
