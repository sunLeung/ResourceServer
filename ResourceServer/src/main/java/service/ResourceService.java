package service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.ContentUtils;
import utils.Def;
import utils.FileUtils;
import utils.HttpUtils;
import utils.JsonUtils;
import utils.RespUtils;
import utils.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import config.Config;

public class ResourceService {
	/**
	 * 加/解密文件
	 * @param src
	 * @param key
	 * @return
	 */
	public static byte[] doMix(byte[] src,byte[] key){
		for(int i=0;i<src.length;i++){
			src[i]^=key[i%32];
		}
		return src;
	}
	
	/**
	 * 获取资源验证
	 * @param token
	 * @param deviceid
	 * @param playerid
	 * @param resourceid
	 * @param resp
	 * @return
	 */
	public static String getResourceAuth(String token,String deviceid,int playerid,int resourceid,HttpServletResponse resp){
		String result="{\"code\":-1,\"msg\":\"Connect game server to auth time out.\"}";
		try {
			Map<String,String> requestProperty=new HashMap<String, String>();
			requestProperty.put("deviceid", deviceid);
			requestProperty.put("protocol", "0x0b");
			requestProperty.put("playerid", playerid+"");
			requestProperty.put("token", token);
			
			Map<String,Object> body = new HashMap<String, Object>();
			body.put("resourceid", resourceid);
			String data=JsonUtils.encode2Str(body);
			String r=HttpUtils.doPost(Config.GAMESERVER_URL, requestProperty,data);
			if(StringUtils.isNotBlank(r)){
				result=r;
				return result;
			}
		} catch (Exception e) {
			RespUtils.responseFail(resp, 500, Def.CODE_EXCEPTION, "Check player resource catch exception.");
			return result;
		}
		RespUtils.responseFail(resp, 500, result);
		return result;
	}

	/**
	 * 一次下载（小文件推荐）
	 * @param deviceid
	 * @param playerid
	 * @param resourceid
	 * @param req
	 * @param resp
	 */
	public static void fullDownloadResource(String token,String deviceid,int playerid,int resourceid,HttpServletRequest req, HttpServletResponse resp){
		try {
			String authDate=getResourceAuth(token,deviceid,playerid, resourceid,resp);
			String secret="";
			if(StringUtils.isBlank(authDate)){
				return;
			}else{
				JsonNode node=JsonUtils.decode(authDate);
				int code=JsonUtils.getInt("code", node);
				if(code==0){
					secret=node.get("data").get("secret").asText();
				}else{
					RespUtils.responseFail(resp, 500, authDate);
					return;
				}
			}
			if(StringUtils.isBlank(secret)){
				RespUtils.responseFail(resp, 500, Def.CODE_FAIL, "Can not get player secret.");
				return;
			}
			
			
			File file=new File(Config.RESOURCE_DIR+resourceid);
			RandomAccessFile readFile =new RandomAccessFile(file, "r");
			resp.setContentType(ContentUtils.getContentType(file.getName()));
			byte[] b=new byte[1024];
			int len=0;
			byte[] key=secret.getBytes();
			OutputStream os = resp.getOutputStream();
			OutputStream out = new BufferedOutputStream(os);
			while((len=readFile.read(b))!=-1){
				out.write(doMix(b, key),0,len);
			}
			if(out!=null){
				out.flush();
				out.close();
			}
			if(os!=null)
				os.close();
			if(readFile!=null)
				readFile.close();
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.responseFail(resp, 500, Def.CODE_EXCEPTION, "Download resource catch exception.");
		}
	}
	
	/**
	 * 随机下载（大文件推荐）
	 * @param deviceid
	 * @param playerid
	 * @param resourceid
	 * @param req
	 * @param resp
	 */
	public static void randomDownloadResource(String token,String deviceid,int playerid,int resourceid,HttpServletRequest req, HttpServletResponse resp){
		try {
			String authDate=getResourceAuth(token,deviceid,playerid, resourceid,resp);
			String secret="";
			if(StringUtils.isBlank(authDate)){
				return;
			}else{
				JsonNode node=JsonUtils.decode(authDate);
				int code=JsonUtils.getInt("code", node);
				if(code==0){
					secret=JsonUtils.getString("secret", node);
				}else{
					RespUtils.responseFail(resp, 500, authDate);
					return;
				}
			}
			if(StringUtils.isBlank(secret)){
				RespUtils.responseFail(resp, 500, Def.CODE_FAIL, "Can not get player secret.");
				return;
			}
			
			
			File file=new File(Config.RESOURCE_DIR+resourceid);
			RandomAccessFile readFile =new RandomAccessFile(file, "r");
			resp.reset();
			resp.setHeader("Accept-Ranges", "bytes");
			resp.addHeader("Content-Disposition","attachment; filename=\"" + file.getName() + "\"");
			resp.setContentType(ContentUtils.getContentType(file.getName()));
			int bufferSize = 1024;
			long begin = 0;
			long end = 0;
			long fileLength = readFile.length();
			long contentLength = 0;
			String rangeBytes = req.getHeader("Range");
			if (StringUtils.isNotBlank(rangeBytes)) {
				rangeBytes = rangeBytes.trim();
				rangeBytes = rangeBytes.replaceAll("bytes=", "");
				String[] range = rangeBytes.split("-");
				if (range.length == 1) {// bytes=969998336-
					begin = Long.valueOf(range[0].trim());
					end = fileLength;
					contentLength = end - begin;
				} else if (range.length == 2) {
					begin = Long.valueOf(range[0].trim());
					end = Long.valueOf(range[1].trim());
					if(begin<end){
						contentLength = end - begin;
					}else{
						begin=0;
						end=fileLength;
						contentLength = end - begin;
					}
				}
			} else {
				begin=0;
				end=fileLength;
				contentLength = end - begin;
			}
			String contentRange = new StringBuffer("bytes ").append(begin)
					.append("-").append(end-1).append("/").append(fileLength)
					.toString();
			resp.setHeader("Content-Range", contentRange);
			resp.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
			resp.addHeader("Content-Length", String.valueOf(contentLength)); 
			
			OutputStream os = resp.getOutputStream();
			OutputStream out = new BufferedOutputStream(os);
			byte b[] = new byte[bufferSize];
			
			readFile.seek(begin);
			while (begin < end) {
				int len = 0;
				if (begin + bufferSize < end){
					len = readFile.read(b);
				} else {
					len = readFile.read(b, 0,(int) (end - begin));
				}
				out.write(b, 0, len);
				begin += len;
			}
			if(out!=null){
				out.flush();
				out.close();
			}
			if(os!=null)
				os.close();
			if(readFile!=null)
				readFile.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.responseFail(resp, 500, Def.CODE_EXCEPTION, "Download resource catch exception.");
		}
	}
	
	/**
	 * 获取文件信息
	 * @param deviceid
	 * @param playerid
	 * @param resourceid
	 * @param resp
	 */
	public static void getFileInfo(String token,String deviceid,int playerid,int resourceid, HttpServletResponse resp){
		RandomAccessFile reader=null;
		try {
			String authDate=getResourceAuth(token,deviceid,playerid, resourceid,resp);
			String secret="";
			if(StringUtils.isBlank(authDate)){
				return;
			}else{
				JsonNode node=JsonUtils.decode(authDate);
				int code=JsonUtils.getInt("code", node);
				if(code==0){
					secret=JsonUtils.getString("secret", node);
				}else{
					RespUtils.responseFail(resp, 500, authDate);
					return;
				}
			}
			if(StringUtils.isBlank(secret)){
				RespUtils.responseFail(resp, 500, Def.CODE_FAIL, "Can not get player secret.");
				return;
			}
			
			File file=new File(Config.RESOURCE_DIR+resourceid);
			reader = new RandomAccessFile(file, "r");
			long length = reader.length();
			String md5=FileUtils.getFileMD5String(file);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("length", length);
			result.put("md5", md5);
			RespUtils.responseSuccess(resp, result);
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.responseFail(resp, 500, Def.CODE_EXCEPTION, "Getting resource data catch exception.");
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}