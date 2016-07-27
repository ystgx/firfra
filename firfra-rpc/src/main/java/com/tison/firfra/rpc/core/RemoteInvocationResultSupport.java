package com.tison.firfra.rpc.core;

import java.io.Serializable;

public class RemoteInvocationResultSupport implements RemoteInvocationResult,Serializable {
	
	private static final long serialVersionUID = -5468261540984351766L;

	private Object value;
	
	private Throwable exceprion;
	
	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public Throwable getException() {
		return this.exceprion;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void setException(Throwable exception) {
		this.exceprion = exception;
	}

	@Override
	public boolean hasException() {
		return (this.exceprion != null);
	}

}
