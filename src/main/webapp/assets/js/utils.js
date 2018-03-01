/** 工具类*/
var utils = {};
$(function(n){

	//创建一个form表单，主要用于js进行页面跳转的时候对参数进行隐藏
	n.creatForm = function(){
		var _obj = {};
		_obj.form = function(config){
	        config = config || {};
	        var url = config.url, method = config.method || 'GET', params = config.params || {};
	        
	        var form = document.createElement('form');
	        form.action = url;
	        form.method = method;
//	        form.target = "_blank";

	        for(var param in params){
	            var value = params[param], input = document.createElement('input');
	            input.type = 'hidden';
	            input.name = param;
	            input.value = value;
	            form.appendChild(input);
	        }
	        return form;
		}
		_obj.submit = function(e){
			appendToBody(e);
		    $(e).submit();
		    removeFromBody(e);
		}
		function appendToBody(e){
		    document.body.appendChild(e);
		}
		function removeFromBody(e){
		    document.body.removeChild(e);
		}
		return _obj;
	}

	//格式化时间
    n.formatDate = function(date, format) {   
        if (!date) return;   
        if (!format) format = "yyyy-MM-dd HH:mm:ss";   
        switch(typeof date) {   
            case "string":   
                date = new Date(date.replace(/-/, "/"));   
                break;   
            case "number":   
                date = new Date(date);   
                break;   
        }    
        if (!date instanceof Date) return;   
        var dict = {   
            "yyyy": date.getFullYear(),   
            "M": date.getMonth() + 1,   
            "d": date.getDate(),   
            "H": date.getHours(),   
            "m": date.getMinutes(),   
            "s": date.getSeconds(),   
            "MM": ("" + (date.getMonth() + 101)).substr(1),   
            "dd": ("" + (date.getDate() + 100)).substr(1),   
            "HH": ("" + (date.getHours() + 100)).substr(1),   
            "mm": ("" + (date.getMinutes() + 100)).substr(1),   
            "ss": ("" + (date.getSeconds() + 100)).substr(1)   
        };       
        return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {   
            return dict[arguments[0]];   
        });                   
    }   
	
})(utils)