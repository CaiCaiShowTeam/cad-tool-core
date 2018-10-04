package priv.lee.cad.model.impl;

import java.awt.Container;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.util.ClientAssert;
import priv.lee.cad.util.StringUtils;

public class DefaultResourceMap extends ResourceMap {

	private static final Logger logger = Logger.getLogger(DefaultResourceMap.class);
	private String prefix;

	public DefaultResourceMap() {

	}

	public DefaultResourceMap(Class<? extends Container> clazz) {
		super(clazz);
	}

	public DefaultResourceMap(String prefix, Class<? extends Container> clazz) {
		super(clazz);
		this.prefix = prefix;
	}

	private String find(String name) {
		name = handleName(name);
		return getResource(name);
	}

	@Override
	public Icon getIcon(String name) {
		String icon = find(name);
		logger.debug("name:" + name + ",icon:" + icon);
		ClientAssert.hasText(icon, "can not find name[" + name + "] configuration");
		return new ImageIcon(getClass().getResource(icon));
	}

	@Override
	public int getInt(String name) {
		String value = find(name);
		logger.debug("name:" + name + ",value:" + value);
		ClientAssert.isNumeric(value, "not a numreic value");
		return Integer.valueOf(value);
	}

	@Override
	public String getString(String name) {
		String value = find(name);
		logger.debug("name:" + name + ",value:" + value);
		return StringUtils.isEmpty(value) ? name : value;
	}

	private String handleName(String name) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		return StringUtils.isEmpty(prefix) ? name : prefix + SEPERATOR + name;
	}
}
