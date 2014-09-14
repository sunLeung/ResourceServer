package service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Def;
import utils.FileUtils;
import utils.RespUtils;
import config.Config;

public class AdminService {
	
	public static void upload(String resourceid,HttpServletRequest req, HttpServletResponse resp){
		System.out.println("do upload method.");
		FileOutputStream fos=null;
		BufferedInputStream bis=null;
		try {
			File file=new File(Config.RESOURCE_DIR+resourceid);
			if(file.exists()){
				String isReplace=req.getHeader("isReplace");
				if(isReplace==null||!"true".equals(isReplace.trim())){
					RespUtils.responseFail(resp,500, Def.CODE_FAIL, "Resourceid is exists.");
					return;
				}
			}
			fos=new FileOutputStream(file);
			bis=new BufferedInputStream(req.getInputStream());
			byte[] b=new byte[1024];
			int len=0;
			while((len=bis.read(b))!=-1){
				fos.write(b, 0, len);
			}
			if(bis!=null){
				bis.close();
				bis=null;
			}
			if(fos!=null){
				fos.close();
				fos=null;
			}
			System.out.println("upload completed.");
			String filemd5=FileUtils.getFileMD5String(file);
			Map<String,Object> result=new HashMap<String, Object>();
			result.put("length", file.length());
			result.put("md5", filemd5);
			RespUtils.responseSuccess(resp, result);
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.responseFail(resp, 500, Def.CODE_EXCEPTION, "upload resource "+ resourceid +" fail.");
		}finally{
			try {
				if(bis!=null)
					bis.close();
				if(fos!=null)
					fos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
