package priv.lee.cad.model.impl;

import java.awt.Window;

public class GlobalResourceMap extends DefaultResourceMap {

	public GlobalResourceMap() {

	}

	public GlobalResourceMap(Class<? extends Window> clazz) {
		super(clazz);
	}

	public GlobalResourceMap(String prefix, Class<? extends Window> clazz) {
		super(prefix, clazz);
	}
}
