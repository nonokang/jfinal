package com.jfinal.network.nio;

import java.util.Scanner;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> 测试<br>
 * <b>版本历史:</b>
 * @author  wpk | 2017年12月7日 下午10:26:00 |创建
 */
public class Test {
	//测试主方法  
    @SuppressWarnings("resource")  
    public static void main(String[] args) throws Exception{  
        //运行服务器  
        Server.start();
        //避免客户端先于服务器启动前执行代码  
        Thread.sleep(100);  
        //运行客户端   
        Client.start();  
        while(Client.sendMsg(new Scanner(System.in).nextLine()));  
        /*for(int i=0;i<5;i++){
        	Thread.sleep(2000);
        	Client.sendMsg("1+"+i);
        }*/
    } 
}
