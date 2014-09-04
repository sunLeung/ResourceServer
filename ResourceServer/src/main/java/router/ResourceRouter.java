package router;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.Logger;
import logger.LoggerManger;
import service.ResourceService;
import utils.Def;
import utils.JsonUtils;
import utils.ReqUtils;
import utils.RespUtils;
import utils.StringUtils;

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
			String protocal=req.getHeader("protocal");
			String deviceid=req.getHeader("deviceid");
			String token=req.getHeader("token");
			int playerid=Integer.valueOf(req.getHeader("playerid"));
			
			String postData=ReqUtils.getPostString(req);
			JsonNode json=JsonUtils.decode(postData);
			int resourceid=JsonUtils.getInt("resourceid",json);
			
			if(StringUtils.isBlank(protocal)||StringUtils.isBlank(token)||playerid<=0||resourceid==-1){
				RespUtils.responseFail(resp, 500,Def.CODE_FAIL, "Parse request data fail.");
				return;
			}
			
			if("0x00".equals(protocal.trim())){//下载整体文件（不支持断线重连）
				ResourceService.fullDownloadResource(token,deviceid,playerid,resourceid,req,resp);
				return;
			}else if("0x01".equals(protocal.trim())){//下载部分文件（支持断线重连）
				ResourceService.randomDownloadResource(token,deviceid,playerid,resourceid,req,resp);
				return;
			}else if("0x02".equals(protocal.trim())){//获取文件信息（文件大小，文件md5值）
				ResourceService.getFileInfo(token,deviceid, playerid, resourceid, resp);
				return;
			}
			RespUtils.responseFail(resp, 500,Def.CODE_FAIL, "Protocal not found.");
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
	
}
