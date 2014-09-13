package service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Def;
import utils.RespUtils;
import config.Config;

public class AdminService {
	
	public static void upload(String resourceid,HttpServletRequest req, HttpServletResponse resp){
		FileOutputStream fos=null;
		BufferedInputStream bis=null;
		try {
			File file=new File(Config.RESOURCE_DIR+resourceid);
			if(file.exists()){
				RespUtils.responseFail(resp, 500,Def.CODE_FAIL, "Resourceid is exists.");
				return;
			}
			fos=new FileOutputStream(file);
			bis=new BufferedInputStream(req.getInputStream());
			byte[] b=new byte[1024];
			int len=0;
			while((len=bis.read(b))!=-1){
				fos.write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
