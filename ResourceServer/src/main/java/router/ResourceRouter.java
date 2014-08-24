package router;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.Logger;
import logger.LoggerManger;
import service.ResourceService;
import utils.JsonUtils;
import utils.ReqUtils;
import utils.RespUtils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 资源下载路由器
 * @author liangyx
 *
 */
public class ResourceRouter extends HttpServlet{
	private static final long serialVersionUID = -8663491905948880086L;
	private static Logger log = LoggerManger.getLogger();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		try {
			String postData=ReqUtils.getPostString(req);
			JsonNode json=JsonUtils.decode(postData);
			if(json!=null){
				ResourceService.downloadResource(req,resp,json);
			}else{
				RespUtils.commonResp(resp, RespUtils.CODE.FAIL, "Bad request params.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION, "Bad request.");
		}
	}
	
}
