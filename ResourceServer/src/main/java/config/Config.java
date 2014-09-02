package config;

import java.io.File;

/**
 * 
 * @Description 项目配置
 * @author liangyx
 * @date 2013-7-1
 * @version V1.0
 */
public class Config {
	public static String ROOT_DIR=System.getProperty("user.dir");
	/**配置文件根目录*/
	public static String CONFIG_DIR=System.getProperty("user.dir")+File.separator+"config"+File.separator;
	/**守护线程运行间隔*/
	public static int WATCH_SECOND=10;
	/**日志配置文件*/
	public static String LOGGER_CONFIG="logger.xx";
//	public static String CONFIG_DIR="D:\\Git\\Payserver\\PayServer\\WebRoot\\WEB-INF\\config";
	
}
