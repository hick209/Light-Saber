package tang.helper.utils;

import java.util.Date;

public class Console {
	private static final int INFO = 0;
	private static final int WARN = 1;
	private static final int ERROR = 2;
	private static final int DEBUG = 3;

	private static final int LOG_LEVEL = DEBUG;
	
	private static void out(int level, Object msg) {		
		if(level <= LOG_LEVEL) {
			Date date = new Date();
			String levelString = null;
			switch(level) {
			case INFO:
				levelString = "INFO";
				break;
			case WARN:
				levelString = "WARN";
				break;
			case ERROR:
				levelString = "ERROR";
				break;
			case DEBUG:
				levelString = "DEBUG";
				break;
			}
			if(level == ERROR) {
				System.err.println(date.toString() + " " + levelString + ": " + msg.toString());
			} else {
				System.out.println(date.toString() + " " + levelString + ": " + msg.toString());
			}
		}
	}

	public static void info(Object s) {
		out(INFO, s);
	}
	public static void warn(Object s) {
		out(WARN, s);
	}
	public static void error(Object s) {
		out(ERROR, s);
	}
	public static void debug(Object s) {
		out(DEBUG, s);
	}
}
