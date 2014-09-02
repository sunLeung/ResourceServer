package boot;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import config.Config;
import logger.LoggerManger;
import utils.TimerManagerUtils;

public class Server implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//清理定时器
		TimerManagerUtils.destroyed();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//Init 
		LoggerManger.initLoggerConfig(Config.LOGGER_CONFIG);
	}

}
