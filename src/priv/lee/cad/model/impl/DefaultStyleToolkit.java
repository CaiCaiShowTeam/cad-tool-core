package priv.lee.cad.model.impl;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.model.StyleToolkit;
import priv.lee.cad.util.ClientAssert;
import priv.lee.cad.util.ObjectUtils;

public class DefaultStyleToolkit implements StyleToolkit {

	class QuitActionListenner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	public static final String CHECK_IN_MENU_ITEM = "menu.option.item1";
	public static final String CHECK_OUT_MENU_ITEM = "menu.option.item2";
	public static final String FILE_MENU = "menu.file";
	public static final String FONT_NAME = "font.name";
	public static final String FONT_SIZE = "font.size";
	public static final String FONT_STYLE = "font.style";
	public static final String OPTION_MENU = "menu.option";
	public static final String QUIT_MENU_ITEM = "menu.file.item1";
	public static ResourceMap resourceMap;
	{
		resourceMap = new GlobalResourceMap();
	}

	@Override
	public Font getFont() {
		String name = resourceMap.getString(FONT_NAME);
		String style = resourceMap.getString(FONT_STYLE);
		String size = resourceMap.getString(FONT_SIZE);
		ClientAssert.hasText(name, "Could not found default font name in client-instance.properties");
		ClientAssert.isNumeric(style, "Default font style must be numeric");
		ClientAssert.isNumeric(size, "Default font size must be numeric");
		return new Font(name, Integer.parseInt(style), Integer.parseInt(size));
	}

	@Override
	public Rectangle getScreenSize(GraphicsConfiguration config) {
		ClientAssert.notNull(config, "Config must not be null");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(config);
		return new Rectangle(screenInsets.left, screenInsets.top,
				screenSize.width - screenInsets.left - screenInsets.right,
				screenSize.height - screenInsets.top - screenInsets.bottom);
	}

	@Override
	public JMenuBar getStandardMenuBar(ActionListener checkinListener, ActionListener checkoutListener) {
		// ~ add menu bar
		JMenu file = new JMenu(resourceMap.getString(FILE_MENU));
		file.getPopupMenu().setLightWeightPopupEnabled(false);

		JMenuItem quit = new JMenuItem(resourceMap.getString(QUIT_MENU_ITEM));
		quit.addActionListener(new QuitActionListenner());

		file.add(quit);

		JMenu option = new JMenu(resourceMap.getString(OPTION_MENU));
		option.getPopupMenu().setLightWeightPopupEnabled(false);

		JMenuItem checkin = new JMenuItem(resourceMap.getString(CHECK_IN_MENU_ITEM));
		if (!ObjectUtils.isEmpty(checkinListener)) {
			checkin.addActionListener(checkinListener);
		}

		JMenuItem checkout = new JMenuItem(resourceMap.getString(CHECK_OUT_MENU_ITEM));
		if (!ObjectUtils.isEmpty(checkoutListener)) {
			checkout.addActionListener(checkoutListener);

		}

		option.add(checkin);
		option.addSeparator();
		option.add(checkout);

		JMenuBar bar = new JMenuBar();
		bar.add(file);
		bar.add(option);
		return bar;
	}
}
