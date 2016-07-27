package com.tison.firfra.rpc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/**
 * Encapsulates information about a handler method consisting of a
 * {@linkplain #getMethod() method} and a {@linkplain #getBean() bean}.
 * Provides convenient access to method parameters, method return value, method annotations.
 *
 * <p>The class may be created with a bean instance or with a bean name (e.g. lazy-init bean,
 * prototype bean). Use {@link #createWithResolvedBean()} to obtain a {@link HandlerMethod}
 * instance with a bean instance resolved through the associated {@link BeanFactory}.
 * 
 * 移植于org.springframework.web.method.HandlerMethod
 */
public class InvokerMethod {
	
	private final Class<?> beanType;

	private final Method method;
	
	/** 如果本方法是一个桥接方法，则找到被桥接的原始方法保存起来 */
	private final Method bridgedMethod;

	private final MethodParameter[] parameters;
	
	
	public InvokerMethod(Class<?> beanType, Method method) {
		Assert.notNull(method, "Method is required");
		this.beanType = beanType;
		this.method = method;
		this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
		this.parameters = initMethodParameters();
	}
	
	
	public InvokerMethod(Class<?> beanType, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
		Assert.notNull(methodName, "Method name is required");
		this.beanType = beanType;
		this.method = beanType.getMethod(methodName, parameterTypes);
		this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
		this.parameters = initMethodParameters();
	}
	
	
	/**
	 * Copy constructor for use in subclasses.
	 */
	protected InvokerMethod(InvokerMethod invokerMethod) {
		Assert.notNull(invokerMethod, "HandlerMethod is required");
		this.beanType = invokerMethod.beanType;
		this.method = invokerMethod.method;
		this.bridgedMethod = invokerMethod.bridgedMethod;
		this.parameters = invokerMethod.parameters;
	}
	
	

	
	private MethodParameter[] initMethodParameters() {
		int count = this.bridgedMethod.getParameterTypes().length;
		MethodParameter[] result = new MethodParameter[count];
		for (int i = 0; i < count; i++) {
			result[i] = new HandlerMethodParameter(i);
		}
		return result;
	}

	/**
	 * Returns the method for this handler method.
	 */
	public Method getMethod() {
		return this.method;
	}

	/**
	 * This method returns the type of the handler for this handler method.
	 * <p>Note that if the bean type is a CGLIB-generated class, the original
	 * user-defined class is returned.
	 */
	public Class<?> getBeanType() {
		return this.beanType;
	}

	/**
	 * If the bean method is a bridge method, this method returns the bridged
	 * (user-defined) method. Otherwise it returns the same method as {@link #getMethod()}.
	 */
	protected Method getBridgedMethod() {
		return this.bridgedMethod;
	}

	/**
	 * Returns the method parameters for this handler method.
	 */
	public MethodParameter[] getMethodParameters() {
		return this.parameters;
	}

	/**
	 * Return the HandlerMethod return type.
	 */
	public MethodParameter getReturnType() {
		return new HandlerMethodParameter(-1);
	}
	

	/**
	 * Return the actual return value type.
	 */
	public MethodParameter getReturnValueType(Object returnValue) {
		return new ReturnValueMethodParameter(returnValue);
	}

	/**
	 * Returns {@code true} if the method return type is void, {@code false} otherwise.
	 */
	public boolean isVoid() {
		return Void.TYPE.equals(getReturnType().getParameterType());
	}

	/**
	 * Returns a single annotation on the underlying method traversing its super methods
	 * if no annotation can be found on the given method itself.
	 * @param annotationType the type of annotation to introspect the method for.
	 * @return the annotation, or {@code null} if none found
	 */
	public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
		return AnnotationUtils.findAnnotation(this.method, annotationType);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InvokerMethod)) {
			return false;
		}
		InvokerMethod otherMethod = (InvokerMethod) other;
		return (this.beanType.equals(otherMethod.beanType) && this.method.equals(otherMethod.method));
	}

	@Override
	public int hashCode() {
		return (this.beanType.hashCode() * 31 + this.method.hashCode());
	}

	@Override
	public String toString() {
		return this.method.toGenericString();
	}

	
	/**
	 * A MethodParameter with HandlerMethod-specific behavior.
	 */
	protected class HandlerMethodParameter extends MethodParameter {

		public HandlerMethodParameter(int index) {
			super(InvokerMethod.this.bridgedMethod, index);
		}

		@Override
		public Class<?> getContainingClass() {
			return InvokerMethod.this.getBeanType();
		}

		@Override
		public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType) {
			return InvokerMethod.this.getMethodAnnotation(annotationType);
		}
	}
	
	
	/**
	 * A MethodParameter for a HandlerMethod return type based on an actual return value.
	 */
	private class ReturnValueMethodParameter extends HandlerMethodParameter {

		private final Object returnValue;

		public ReturnValueMethodParameter(Object returnValue) {
			super(-1);
			this.returnValue = returnValue;
		}

		@Override
		public Class<?> getParameterType() {
			return (this.returnValue != null ? this.returnValue.getClass() : super.getParameterType());
		}
	}

}
