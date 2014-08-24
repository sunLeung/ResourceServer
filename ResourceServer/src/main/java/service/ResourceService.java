package service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.HttpUtils;

import com.fasterxml.jackson.databind.JsonNode;


public class ResourceService {
	private static String url="http://127.0.0.1";
	
	public static void downloadResource(HttpServletRequest req, HttpServletResponse resp,JsonNode reqData){
		//获取用户信息
		long uuid=reqData.get("uuid").asLong();
		int resourceid=reqData.get("resourceid").asInt();
		//验证用户是否有权限下载改资源
		Map<String,String> params=new HashMap<String, String>();
		params.put("uuid", String.valueOf(uuid));
		params.put("resourceid", String.valueOf(resourceid));
		String result=HttpUtils.doGet(url, params);
		
	}
}
