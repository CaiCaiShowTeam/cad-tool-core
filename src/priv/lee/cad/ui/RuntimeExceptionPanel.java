package priv.lee.cad.ui;

import javax.swing.JOptionPane;

import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.model.impl.DefaultResourceMap;
import priv.lee.cad.util.ClientAssert;
import priv.lee.cad.util.StringUtils;

public class RuntimeExceptionPanel extends JOptionPane {

    private static final long serialVersionUID = 8525997112187397078L;
    private static final String TITLE = "title";
    public static final String DELIM = "$$$";
    private RuntimeException exception;
    protected ResourceMap resourceMap = new DefaultResourceMap (RuntimeExceptionPanel.class);

    public RuntimeExceptionPanel(RuntimeException exception) {
	ClientAssert.notNull (exception,"IllegalArgumentException is required");

	this.exception = exception;
	init ();
    }

    private void init() {
	String message = exception.getMessage ();
	String localeMessage = "";
	if (StringUtils.contains (message,DELIM)) {
	    String suffix = StringUtils.substringBefore (message,DELIM);
	    localeMessage = suffix + resourceMap.getString (StringUtils.substringAfter (message,DELIM));
	} else {
	    localeMessage = resourceMap.getString (message);
	}
	showMessageDialog (null,localeMessage,resourceMap.getString (TITLE),ERROR_MESSAGE);
	throw new RuntimeException (localeMessage);
    }

    public void setResourceMap(ResourceMap resourceMap) {
	this.resourceMap = resourceMap;
    }
}
