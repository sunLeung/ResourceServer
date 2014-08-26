package test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Test {
	public static void main(String[] args) throws IOException {
//		System.out.println(MD5Util.getFileMD5String(new File("e:/abc.mp4")));
		System.out.println(MD5Util.getFileMD5String(new File("d:/fucklll")));
		System.out.println(MD5Util.getFileMD5String(new File("G:/test.rmvb")));
//		System.out.println(MD5Util.getFileMD5String(new File("e:/abc2.mp4")));
////		
//		try {
//			for(int i=0;i<5;i++){
//			File f=new File("e:/test.zip");
//			RandomAccessFile raf=new RandomAccessFile(f, "r");
//			long filesize=raf.length();
//			long begin=filesize/5*(i);
//			long end=filesize/5*(i+1)-1;
//			
//			if(i==4){
//				end=filesize;
//			}
//			System.out.println(i +"    "+begin+"-"+end);
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		for (int i = 0; i < 5; i++) {
//			new Thread(new T2(i)).start();
//		}
//		
//		new Thread(new T2(0)).start();
//		new T2(0).run();
//		new T2(1).run();
//		new T2(2).run();
//		new T2(3).run();
//		new T2(4).run();
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
