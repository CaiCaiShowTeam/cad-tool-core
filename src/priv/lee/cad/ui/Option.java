package priv.lee.cad.ui;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.model.ResourceMapper;
import priv.lee.cad.model.impl.ComponentResourceMap;
import priv.lee.cad.util.ClientAssert;
import priv.lee.cad.util.StringUtils;

public class Option extends JButton implements ResourceMapper {

	static class CancelOption implements ActionListener {

		private static final String CANCEL = "cancel";

		public static Option newInstance(Window window) {
			ClientAssert.notNull(window, "Window(parent) must not be null");
			return new Option(CANCEL, null, new CancelOption(window), null);
		}

		public static Option newInstance(Window window, String name) {
			ClientAssert.notNull(window, "Window(parent) must not be null");
			return new Option(name, null, new CancelOption(window), null);
		}

		private Window window;

		private CancelOption(Window window) {
			this.window = window;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			window.dispose();
		}
	}
	
	public static final String BROWSE_BUTTON = "browse";
	public static final String CONFIRM_BUTTON = "confirm";
	public static final String EXPORT_BUTTON = "export";
	private static final String PREFIX = Option.class.getSimpleName().toLowerCase();
	protected static ResourceMap resourceMap = new ComponentResourceMap(PREFIX, Option.class);
	private static final long serialVersionUID = -7593230479709858017L;

	public static Option newCancelOption(Window window) {
		return CancelOption.newInstance(window);
	}

	public static Option newCancelOption(Window window, String name) {
		return CancelOption.newInstance(window, name);
	}

	public Option(String name, String icon, ActionListener action) {
		this(name, icon, action, null);
	}

	public Option(String name, String icon, ActionListener action, Dimension preferredSize) {
		super(StringUtils.isEmpty(name) ? null : resourceMap.getString(name),
				StringUtils.isEmpty(icon) ? null : resourceMap.getIcon(icon));
		ClientAssert.notNull(action, "Option action is required");
		addActionListener(action);

		if (preferredSize != null) {
			setPreferredSize(preferredSize);
		}
	}

	@Override
	public ResourceMap getResourceMap() {
		return resourceMap;
	}

	@Override
	public void setResourceMap(ResourceMap resourceMap) {

	}
}
