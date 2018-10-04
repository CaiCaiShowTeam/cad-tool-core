package priv.lee.cad.resource;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import priv.lee.cad.util.ClientAssert;
import priv.lee.cad.util.PropertiesUtils;

public class ResourceBundle {

	private Class<?> clazz;
	private Logger logger = Logger.getLogger(ResourceBundle.class);
	private Properties properties = new Properties();
	private String repository;
	private final String RESOURCE = "instance.resource";
	private String standard;
	private final String STANDARD_RESOURCE = "standard.resource";
	{
		repository = PropertiesUtils.readProperty(RESOURCE);
		standard = PropertiesUtils.readProperty(STANDARD_RESOURCE);
	}

	public ResourceBundle(Class<?> clazz) {
		this.clazz = clazz;
		initResource();
	}

	private void initResource() {
		try {
			String resource = repository + (clazz == null ? standard : (clazz.getSimpleName() + ".properties"));
			logger.debug("resource:" + resource);
			InputStream stream = ResourceBundle.class.getClassLoader().getResourceAsStream(resource);
			ClientAssert.notNull(stream, "Resource[" + resource + "] does not exsit,check the repository");
			properties.load(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readResource(String resource) {
		ClientAssert.hasText(resource, "resource is required");
		return (String) properties.get(resource);
	}
}
