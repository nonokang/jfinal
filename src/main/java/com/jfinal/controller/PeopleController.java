package com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.model.People;
import com.jfinal.plugin.activerecord.Page;

public class PeopleController extends Controller{
	
	private static Logger logger = Logger.getLogger(LoginContoller.class);

	public void index(){
		renderJsp("/WEB-INF/views/people/peopleIndex.jsp");
	}
	
	public void list(){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = true;
		Integer pageNumber = getParaToInt("page");
		Integer pageSize = getParaToInt("limit");
		String searchName = getPara("searchName");
		try {
			Page<People> p = People.dao.list(pageNumber, pageSize, searchName);
			map.put("code", 0);
			map.put("count", p.getTotalRow());
			map.put("data", p.getList());
		} catch (Exception e) {
			flag = false;
			map.put("code", 0);
			map.put("count", 0);
			map.put("data", null);
			e.printStackTrace();
		}
		map.put("success", flag);
		renderJson(map);
	}
	
	public void detail(){
		Integer id = getParaToInt("id");
		if(null != id){
			People p = People.dao.findById(id);
			setAttr("bean", p);
		}
		renderJsp("/WEB-INF/views/people/peopleEdit.jsp");
	}
	
	public void save(){
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = true;
		People bean = getBean(People.class);
		try {
			if(bean.getId() != null){//修改
				People oldBean = People.dao.findById(bean.getId());
				oldBean.setAge(bean.getAge());
				oldBean.setName(bean.getName());
				oldBean.setOpera("0");
				oldBean.setRemark(bean.getRemark());
				flag = oldBean.update();
			}else{//新增
				flag = bean.save();
			}
		} catch (Exception e) {
			flag = false;
			map.put("msg", "操作异常："+e.getMessage());
		}
		map.put("success", flag);
		renderJson(map);
	}
	
	public void opera(){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = true;
		String type = getPara("type");
		String ids = getPara("ids");
		try {
			if(StrKit.notBlank(ids)){
				String[] _ids = ids.split(",");
				for(String id : _ids){
					if(null == id || "".equals(id)) continue;
					flag = flag && People.dao.opera(Integer.parseInt(id), type);
				}
			}else{
				throw new NullPointerException(String.format("参数id为空！"));
			}
		} catch (Exception e) {
			flag = false;
			map.put("msg", e.getMessage());
		}
		map.put("success", flag);
		renderJson(map);
	}
}
