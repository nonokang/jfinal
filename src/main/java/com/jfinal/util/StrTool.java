package com.jfinal.util;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> 字符串处理工具类<br>
 * <b>版本历史:</b>
 * @author  wpk | 2017年12月6日 下午10:15:51 |创建
 */
public class StrTool {
	
	/**
	 * <b>描述：</b> 首字母变小写
	 * @author wpk | 2017年12月6日 下午10:17:29 |创建
	 * @param str
	 * @return String
	 */
	public static String firstToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * <b>描述：</b> 首字母变大写
	 * @author wpk | 2017年12月6日 下午10:18:00 |创建
	 * @param str
	 * @return String
	 */
	public static String firstToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * <b>描述：</b> 字符串为 null 或者内部字符全部为 ' ' '\t' '\n' '\r' 这四类字符时返回 true
	 * @author wpk | 2017年12月6日 下午10:18:15 |创建
	 * @param str
	 * @return boolean
	 */
	public static boolean isBlank(String str) {
		if (str == null) {
			return true;
		}
		int len = str.length();
		if (len == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			switch (str.charAt(i)) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
			// case '\b':
			// case '\f':
				break;
			default:
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <b>描述：</b> 判断字符不为空
	 * @author wpk | 2017年12月6日 下午10:18:46 |创建
	 * @param str
	 * @return boolean
	 */
	public static boolean notBlank(String str) {
		return !isBlank(str);
	}
	
	/**
	 * <b>描述：</b> 判断多个字符不为空
	 * @author wpk | 2017年12月6日 下午10:19:39 |创建
	 * @param strings
	 * @return boolean
	 */
	public static boolean notBlank(String... strings) {
		if (strings == null || strings.length == 0) {
			return false;
		}
		for (String str : strings) {
			if (isBlank(str)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * <b>描述：</b> 把字符传数组中的值通过分隔符拼接起来
	 * @author wpk | 2017年12月6日 下午10:22:43 |创建
	 * @param strs
	 * @param separator	分隔符
	 * @return String
	 */
	public static String join(String[] strs, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<strs.length; i++) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(strs[i]);
		}
		return sb.toString();
	}
	
	/**
	 * <b>描述：</b> 获取UUID
	 * @author wpk | 2017年12月6日 下午10:32:31 |创建
	 * @return String
	 */
	public static String getRandomUUID() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
	
}
