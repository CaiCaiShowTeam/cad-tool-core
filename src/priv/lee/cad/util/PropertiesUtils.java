package priv.lee.cad.util;

import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtils {

	private static final Logger logger = Logger.getLogger(PropertiesUtils.class);
	private static Properties properties = new Properties();
	static {
		try {
			properties.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("./client-instance.properties"));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static String readProperty(String property) {
		logger.debug("property:" + property);
		return (String) properties.get(property);
	}
}
