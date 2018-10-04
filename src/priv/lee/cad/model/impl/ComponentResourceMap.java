package priv.lee.cad.model.impl;

import javax.swing.JComponent;

public class ComponentResourceMap extends DefaultResourceMap {

	public ComponentResourceMap(String prefix, Class<? extends JComponent> clazz) {
		super(prefix, clazz);
	}
}
