package priv.lee.cad.ui;

import javax.swing.JOptionPane;

import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.model.impl.DefaultResourceMap;
import priv.lee.cad.util.ClientAssert;

public class RuntimeExceptionPanel extends JOptionPane {

	private static final long serialVersionUID = 8525997112187397078L;
	private static final String TITLE = "title";
	private RuntimeException exception;
	protected ResourceMap resourceMap = new DefaultResourceMap(RuntimeExceptionPanel.class);

	public RuntimeExceptionPanel(RuntimeException exception) {
		ClientAssert.notNull(exception, "IllegalArgumentException is required");

		this.exception = exception;
		init();
	}

	private void init() {
		String localeMessage = resourceMap.getString(exception.getMessage());
		showMessageDialog(null, localeMessage, resourceMap.getString(TITLE), ERROR_MESSAGE);
		throw new RuntimeException(localeMessage);
	}

	public void setResourceMap(ResourceMap resourceMap) {
		this.resourceMap = resourceMap;
	}
}
