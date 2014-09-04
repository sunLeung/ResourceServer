package utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import logger.Logger;
import logger.LoggerManger;

public class RespUtils {
	private static Logger log = LoggerManger.getLogger();
	private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

	public static void responseFail(HttpServletResponse resp,int status,int code, String msg) {
		try {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("code", code);
			map.put("msg", msg);
			String content=JsonUtils.encode2Str(map);
			resp.setHeader("content-type", DEFAULT_CONTENT_TYPE);
			resp.setStatus(status);
			resp.getWriter().write(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void responseFail(HttpServletResponse resp,int status,String content) {
		try {
			resp.setHeader("content-type", DEFAULT_CONTENT_TYPE);
			resp.setStatus(status);
			resp.getWriter().write(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void responseSuccess(HttpServletResponse resp,Object obj) {
		try {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("code", Def.CODE_SUCCESS);
			map.put("data", obj);
			String content=JsonUtils.encode2Str(map);
			resp.setHeader("content-type", DEFAULT_CONTENT_TYPE);
			resp.setStatus(200);
			resp.getWriter().write(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
