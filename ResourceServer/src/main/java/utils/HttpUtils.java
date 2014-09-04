package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {
	
	public static String doGet(String url) {
		return doGet(url,null);
	}
	
	public static String doGet(String url, Map<String,String> params) {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			url=linkParams(url, params);
			System.out.println(url);
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setReadTimeout(2000);
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String doPost(String url, Map<String,String> params) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(2000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(getParamsStr(params));
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}
	
	public static String doPost(String url, String body) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(2000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(body);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}
	
	public static String doPost(String url,long begin,long end,String body,String recvFile) {
		int status=-1;
		PrintWriter out = null;
		BufferedInputStream bis = null;
		RandomAccessFile writer=null;
		long contentLength=end-begin+1;
		contentLength=contentLength<0?0:contentLength;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Range", "bytes="+begin+"-"+end);
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(2000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(body);
			out.flush();
			status=conn.getResponseCode();
			if(status!=200&&status!=206){
				return status+"";
			}
			String contentRange=conn.getHeaderField("Content-Range");
			if(StringUtils.isNotBlank(contentRange)){
				System.out.println("contentRange:"+contentRange);
				String range=contentRange.substring(contentRange.indexOf("bytes ")+6, contentRange.indexOf("/"));
				String contentLengthStr=contentRange.substring(contentRange.indexOf("/")+1,contentRange.length());
				String[] ranges=range.split("-");
				begin=Long.valueOf(ranges[0].trim());
				end=Long.valueOf(ranges[1].trim());
				contentLength=Long.valueOf(contentLengthStr);
			}
			bis = new BufferedInputStream(conn.getInputStream(), 1024);
			writer=new RandomAccessFile(recvFile, "rw");
			writer.seek(begin);
			byte[] buffer = new byte[1024];
			int len=0;
			long total=0;
			while ((len=bis.read(buffer))!=-1) {
				writer.write(buffer, 0, len);
				total+=len;
				System.out.println(((float)total/(float)contentLength)*100);
			}
			System.out.println("total:"+total);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return status+"";
	}
	
	public static String doPost(String url,Map<String,String> requestProperty, String body) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(requestProperty!=null)
			for(Entry<String,String> entry:requestProperty.entrySet()){
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
//			conn.setReadTimeout(5000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(body);
			out.flush();
			if(conn.getResponseCode()==200){
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else{
				in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}
	
	public static String download(String url,Map<String,String> requestProperty, String body,File recvFile) {
		int status=-1;
		PrintWriter out = null;
		BufferedInputStream bis = null;
		RandomAccessFile writer=null;
		BufferedReader br=null;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if(requestProperty!=null)
			for(Entry<String,String> entry:requestProperty.entrySet()){
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(5000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(body);
			out.flush();
			status=conn.getResponseCode();
			if(status==200||status==206){
				bis=new BufferedInputStream(conn.getInputStream(),1024);
				writer=new RandomAccessFile(recvFile, "rw");
				byte[] b = new byte[1024];
				int len=0;
				long total=0;
				while((len=bis.read(b))!=-1){
					writer.write(b, 0, len);
					total+=len;
				}
				return "download finish total:"+total;
			}else{
				StringBuilder sb=new StringBuilder();
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
				if (bis != null)
					bis.close();
				if (out != null)
					out.close();
				if (br !=null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return status+"";
	}
	
	/**
	 * 创建带参数的URL
	 * @param url
	 * @param params
	 * @return
	 */
	public static String linkParams(String url,Map<String,String> params){
		StringBuffer sb=new StringBuffer();
		if(params==null)return url;
		if(params.size()<=0)return url;
		sb.append(url).append("?");
		for(Entry<String,String> entry:params.entrySet()){
			String value=entry.getValue();
			if(StringUtils.isEmpty(value))value="";
			sb.append(entry.getKey()).append("=").append(value).append("&");
		}
		return StringUtils.removeEnd(sb.toString(), "&");
	}
	
	/**
	 * 获取参数字符串
	 * @param params
	 * @return
	 */
	public static String getParamsStr(Map<String,String> params){
		StringBuffer sb=new StringBuffer();
		if(params==null)return "";
		if(params.size()<=0)return "";
		for(Entry<String,String> entry:params.entrySet()){
			String value=entry.getValue();
			if(StringUtils.isEmpty(value))value="";
			sb.append(entry.getKey()).append("=").append(value).append("&");
		}
		return StringUtils.removeEnd(sb.toString(), "&");
	}
}
