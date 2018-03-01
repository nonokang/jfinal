package com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.core.Controller;

/**
 * <b>版权信息:</b> jfinal框架练习<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史: 0.0.1</b>
 * @author  wpk | 2018年2月10日 下午1:16:38 |创建
 */
public class LoginContoller extends Controller{

	public void index(){
		renderJsp("/WEB-INF/views/login.jsp");
	}
	
	public void login(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusCode", 200);
		renderJson(map);
	}
	
	public void loginSuccess(){
		renderJsp("/WEB-INF/views/index.jsp");
	}
	
}
