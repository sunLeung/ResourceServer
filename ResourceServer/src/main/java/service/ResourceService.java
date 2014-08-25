package service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.CommonUtil;
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

		File file = null;
		resp.reset();
		resp.setHeader("Accept-Ranges", "bytes");
		int bufferSize = 1024;
		long begin = 0;
		long end = 0;
		long fileLength = 0;
		long contentLength = 0;
		String rangeBytes = req.getHeader("Range");
		// 判断是否从某个位置开始传输
		if (StringUtils.isNotBlank(rangeBytes)) {
			rangeBytes = rangeBytes.trim();
			rangeBytes = rangeBytes.replaceAll("bytes=", "");
			String[] range = rangeBytes.split("-");
			if (range.length == 1) {// bytes=969998336-
				begin = Long.valueOf(range[0].trim());
				end = fileLength - 1;
				contentLength = end - begin;
			} else if (range.length == 2) {
				begin = Long.valueOf(range[0].trim());
				end = Long.valueOf(range[1].trim());
				contentLength = end - begin;
			}
			String contentRange = new StringBuffer("bytes ").append(begin)
					.append("-").append(end).append("/").append(fileLength)
					.toString();
			resp.setHeader("Content-Range", contentRange);
			resp.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
		} else {// 从0开始传输
			contentLength = fileLength;
			end = fileLength - 1;
		}

		resp.addHeader("Content-Disposition",
				"attachment; filename=\"" + file.getName() + "\"");
		resp.setContentType(CommonUtil.setContentType(file.getName()));
		resp.addHeader("Content-Length", String.valueOf(contentLength));
		RandomAccessFile raf = null;// 负责读取数据
		OutputStream os = null;// 写出数据
		OutputStream out = null;// 缓冲
		os = resp.getOutputStream();
		out = new BufferedOutputStream(os);
		raf = new RandomAccessFile(file, "r");
		byte b[] = new byte[bufferSize];

		raf.seek(begin);
		int n = 0;
		long readLength = 0;// 记录已读字节数
		while (readLength <= contentLength - 1024) {// 大部分字节在这里读取
			n = raf.read(b, 0, 1024);
			readLength += 1024;
			out.write(b, 0, n);
		}
		if (readLength <= contentLength) {// 余下的不足 1024 个字节在这里读取
			n = raf.read(b, 0, (int) (contentLength - readLength));
			out.write(b, 0, n);
		}
		out.flush();
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