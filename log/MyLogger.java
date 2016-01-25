package log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MyLogger {
	private static final Logger log = Logger.getLogger(MyLogger.class);

	public static Logger Log() {
		PropertyConfigurator.configure("log4j.properties");
		return log;
	}
}