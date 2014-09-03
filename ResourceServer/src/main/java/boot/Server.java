package boot;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import config.Config;
import logger.LoggerManger;
import utils.TimerManagerUtils;

public class Server implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//回写日志
		LoggerManger.stopFileWriter();
		//清理定时器
		TimerManagerUtils.destroyed();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//初始化配置
		initConfig(arg0);
		LoggerManger.initLoggerConfig(Config.LOGGER_CONFIG);
	}

	private void initConfig(ServletContextEvent sce){
		Config.RESOURCE_DIR=sce.getServletContext().getRealPath("")+File.separator+"WEB-INF"+File.separator+Config.RESOURCE_DIR+File.separator;
		Config.CONFIG_DIR=sce.getServletContext().getRealPath("")+File.separator+"WEB-INF"+File.separator+Config.CONFIG_DIR+File.separator;
		Config.LOGGER_CONFIG=Config.CONFIG_DIR+Config.LOGGER_CONFIG;
	}
}
