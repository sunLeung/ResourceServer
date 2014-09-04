package test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import utils.FileUtils;
import utils.HttpUtils;
import utils.JsonUtils;

public class Test {
	private static String url="http://115.28.234.110:5000/res";
	
	public static void main(String[] args) throws IOException {
//		getFileInfo();
		fullDown();
//		System.out.println(FileUtils.getFileMD5String(new File("C:\\Users\\hacker\\git\\ResourceServer\\ResourceServer\\webapp\\WEB-INF\\resource\\1")));
//		System.out.println(FileUtils.getFileMD5String(new File("D:\\1")));
	}
	
	
	public static void getFileInfo(){
		Map<String,String> requestProperty=new HashMap<String, String>();
		requestProperty.put("protocal", "0x02");
		requestProperty.put("playerid", "1");
		requestProperty.put("token", "9cf4d052296847f5a6037ab43e803803");
		requestProperty.put("deviceid", "liangyuxin");
		
		Map<String,Object> body=new HashMap<String, Object>();
		body.put("resourceid", 1);
		String result=HttpUtils.doPost(url, requestProperty, JsonUtils.encode2Str(body));
		System.out.println(result);
	}
	
	public static void fullDown(){
		Map<String,String> requestProperty=new HashMap<String, String>();
		requestProperty.put("protocal", "0x00");
		requestProperty.put("playerid", "1");
		requestProperty.put("token", "9cf4d052296847f5a6037ab43e803803");
		requestProperty.put("deviceid", "liangyuxin");
		
		Map<String,Object> body=new HashMap<String, Object>();
		body.put("resourceid", 1);
		String result=HttpUtils.download(url, requestProperty, JsonUtils.encode2Str(body),new File("d:/1"));
		System.out.println(result);
	}
}

class T2 implements Runnable{
	String[] s = new String[] { "0-1153685", "1153683-2307369",
			"2307367-3461059", "3461051-4614736",
	"4614735-5768422","0-5768422" };
	
	private int i=0;
	public T2(int i){
		this.i=i;
	}
	@Override
	public void run() {
		try {
			File f = new File("e:/test.zip");
			RandomAccessFile reader = new RandomAccessFile(f, "r");
			long filesize = reader.length();
			long begin = Long.valueOf(s[i].split("-")[0]);
			long end = Long.valueOf(s[i].split("-")[1]);
			
			reader.seek(begin);
			int buflen = 1024;
			byte[] buffer = new byte[buflen];
			File f2 = new File("E:/b2");
			RandomAccessFile writer = new RandomAccessFile(f2, "rw");
			writer.seek(begin);
			while (begin < end) {
				System.out.println("begin:"+ begin +"      end:"+end);
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
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
