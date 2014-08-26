package service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.CommonUtil;
import utils.RespUtils;
import utils.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

public class ResourceService {
	private static String url = "http://127.0.0.1";

	public static void downloadResource(HttpServletRequest req,
			HttpServletResponse resp, JsonNode reqData) throws IOException {
		// //获取用户信息
		// long uuid=reqData.get("uuid").asLong();
		// int resourceid=reqData.get("resourceid").asInt();
		// //验证用户是否有权限下载改资源
		// Map<String,String> params=new HashMap<String, String>();
		// params.put("uuid", String.valueOf(uuid));
		// params.put("resourceid", String.valueOf(resourceid));
		// String result=HttpUtils.doGet(url, params);
		System.out.println("downloadResource request");
		String resourceid=reqData.get("resourceid").asText();
		File file=new File(resourceid);
		RandomAccessFile readFile =new RandomAccessFile(file, "r");
		resp.reset();
		resp.setHeader("Accept-Ranges", "bytes");
		resp.addHeader("Content-Disposition","attachment; filename=\"" + file.getName() + "\"");
		resp.setContentType(CommonUtil.setContentType(file.getName()));
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
		
	}
	
	public static void getFileLength(HttpServletRequest req,
			HttpServletResponse resp, JsonNode reqData){
		RandomAccessFile reader=null;
		try {
			String resourceid = reqData.get("resourceid").asText();
			if (StringUtils.isBlank(resourceid)) {
				RespUtils.commonResp(resp, RespUtils.CODE.FAIL,"resourceid can not empty.");
			} else {
				reader = new RandomAccessFile(resourceid, "r");
				long length = reader.length();
				Map<String, Long> result = new HashMap<String, Long>();
				result.put("length", length);
				RespUtils.jsonResp(resp, RespUtils.CODE.SUCCESS, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION,"can not find resource");
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
	
	public static void test(HttpServletRequest req,
			HttpServletResponse resp) throws IOException{File file = new File("g:/test.rmvb");
			RandomAccessFile readFile =new RandomAccessFile(file, "r");
			resp.reset();
			resp.setHeader("Accept-Ranges", "bytes");
			resp.addHeader("Content-Disposition","attachment; filename=\"" + file.getName() + "\"");
			resp.setContentType(CommonUtil.setContentType(file.getName()));
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
					end = Long.valueOf(range[1].trim())+1;
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
			out.flush();
			out.close();
			os.close();
	}

	public static void main(String[] args) throws FileNotFoundException {
		for (int i = 0; i < 5; i++) {
			new Thread(new T(i)).start();
		}
		
//		new Thread(new T(5)).start();
	}
	
}

class T implements Runnable{
	String[] s = new String[] { "0-337878829", "337878830-675757659",
			"675757660-1013636489", "1013636490-1351515319",
	"1351515320-1689394154","0-1689394154" };
	
	private int i=0;
	public T(int i){
		this.i=i;
	}
	@Override
	public void run() {
		try {
			File f = new File("G:/abc.mkv");
			RandomAccessFile reader = new RandomAccessFile(f, "r");
			long filesize = reader.length();
			long begin = Long.valueOf(s[i].split("-")[0]);
			long end = Long.valueOf(s[i].split("-")[1]);
			
			reader.seek(begin);
			int buflen = 1024;
			byte[] buffer = new byte[buflen];
			File f2 = new File("E:/b");
			RandomAccessFile writer = new RandomAccessFile(f2, "rw");
			writer.seek(begin);
			while (begin < end) {
				int len = 0;
				if (begin + buflen < end)// 如果可以装满一个缓冲区
				{
					len = reader.read(buffer);
				} else {
					len = reader.read(buffer, 0,(int) (end - begin));
				}
				writer.write(buffer, 0, len);
				begin += len;
				System.out.println("Thread-"+i+"  get "+len);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}