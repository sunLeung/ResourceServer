package router;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.Logger;
import logger.LoggerManger;
import service.AdminService;
import service.ResourceService;
import utils.Def;
import utils.JsonUtils;
import utils.ReqUtils;
import utils.RespUtils;
import utils.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import config.Config;

/**
 * 资源下载路由器
 * @author liangyx
 *
 */
public class AdminRouter extends HttpServlet{
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
			System.out.println("received admin request.");
			String security=req.getHeader("security");
			String action=req.getHeader("action");
			String resourceid=req.getHeader("resourceid");
			
			if(!Config.SECURITY.equals(security)){
				return;
			}
			
			if(StringUtils.isBlank(resourceid)){
				RespUtils.responseFail(resp, 500,Def.CODE_FAIL, "Resourceid can not be null.");
				return;
			}
			
			if("upload".equals(action)){
				AdminService.upload(resourceid, req, resp);
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
	
}
