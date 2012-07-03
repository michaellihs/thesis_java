package de.mknoll.thesis.tests.testclasses;

import org.picocontainer.annotations.Inject;

public class D {

	@Inject private A a;
	
	private B b;
	
	
	private C c;
	
	
	public D(B b) {
		this.b = b;
	}
	
	
	
	public void setC(C c) {
		this.c = c;
	}
	
	
	
	public A getA() {
		return this.a;
	}
	
	
	
	public B getB() {
		return this.b;
	}
	
	
	
	public C getC() {
		return this.c;
	}
	
}
