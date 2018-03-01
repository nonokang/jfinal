package com.jfinal.network.netty;

public class A {

	private String name;
	private static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
		@Override  
        protected String initialValue() {  
            return "hello";  
        } 
	};
	private volatile static A a;
	
	public static A me(){
		if(a == null){
			synchronized (A.class) {
				if(a == null){
					System.out.println("创建对象");
					a = new A();
				}
			}
		}
		return a;
	}
	
	public void out(){
		System.out.println(threadLocal.get());
	}
	
	public void setStr(String value){
		System.out.println(threadLocal.get());
		threadLocal.set(value);
		out();
	}
	
	public String getStr(){
		return threadLocal.get();
	}

	public static A getA() {
		return a;
	}

	public static void setA(A a) {
		A.a = a;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
