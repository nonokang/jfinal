package com.jfinal.bean;

import com.jfinal.model.People;

public class Emp {
    /*public static void main(String[] args) {
    	People p = new People();
    	p.setName("张三");

        change(p);

        System.out.println(p.getName());
    }

    public static void change(People p) {
    	People person = new People();
    	person.setName("李四");
        p = person; 
        System.out.println(p.getName());
    }*/
    
    public static void main(String[] args) {
        String s1 = "Programming";//sdsdsds
        String s2 = new String("Programming");
        String s3 = "Program" + "ming";
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s1.intern());
    }
    
}
