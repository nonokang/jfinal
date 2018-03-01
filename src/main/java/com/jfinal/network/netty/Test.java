package com.jfinal.network.netty;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		/*if(a.equals(b)){
			System.out.println("相等1");
		}
		if(a == b){
			System.out.println("相等2");
		}*/
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				A a = A.me();
				a.setName("你好");
				a.setStr("fsdfs");
				System.out.println("线程1："+a.getName());
			}
		});
//		Thread.sleep(10);
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub

				A b = A.me();
//				b.setStr("anymore111");
				System.out.println("线程2："+b.getName()+"\t"+b.getStr());
			}
		});
		Thread t3 = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub

				A b = A.me();
//				b.setStr("anymore");
				System.out.println("线程3："+b.getName()+"\t"+b.getStr());
			}
		});
		t1.start();
		t2.start();
		t3.start();
	}

}
