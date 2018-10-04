package priv.lee.cad.util;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import priv.lee.cad.model.ClientTemporary;

public class XmlUtils {

	public static String CSN = "UTF-8";
	private static final Logger logger = Logger.getLogger(XmlUtils.class);
	public static XmlMapper mapper = new XmlMapper();
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDefaultUseWrapper(false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
		mapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
	}

	public static Field findField(Class<?> clazz, String name) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(name);
		} catch (Exception e) {
			logger.warn("Class:" + clazz + " found no field named:" + name);
		} finally {
			if (field != null) {
				field.setAccessible(true);
				return field;
			}

			Class<?> superClass = clazz.getSuperclass();
			logger.debug("Loop superClass:" + superClass + " field...");
			if (!Object.class.equals(superClass)) {
				field = findField(superClass, name);
			}
		}
		return field;
	}

	public static <T> T read(Class<T> clatt) {
		File file = ClientInstanceUtils.getTemporaryFile();
		return read(file, clatt);
	}

	public static <T> T read(File file, Class<T> clatt) {
		ClientAssert.notNull(file, "File is requiret");
		ClientAssert.notNull(clatt, "Type of " + ClientTemporary.class + " is requiret");
		try {
			if (!file.exists()) {
				return null;
			}

			return mapper.readValue(file, clatt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void store(ClientTemporary temporary) {
		ClientAssert.notNull(temporary, "Temporary is required");
		try {
			// find temporary file
			File file = ClientInstanceUtils.getTemporaryFile();
			logger.debug("file:" + file);

			// ~ create xml file and reflect to write content
			if (!file.exists()) {
				file.createNewFile();
			}

			PrintWriter writer = new PrintWriter(file, CSN);
			writer.println(mapper.writeValueAsString(temporary));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
