package priv.lee.cad.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.model.ResourceMapper;
import priv.lee.cad.model.SelfAdaptionComponent;
import priv.lee.cad.model.StyleToolkit;
import priv.lee.cad.model.TieContainer;
import priv.lee.cad.model.impl.DefaultStyleToolkit;
import priv.lee.cad.model.impl.GlobalResourceMap;

public abstract class AbstractFrame extends JFrame implements SelfAdaptionComponent, ResourceMapper, TieContainer {

	private static final Logger logger = Logger.getLogger(AbstractFrame.class);
	private static final long serialVersionUID = 8333297262635054463L;
	private LayoutManager layout = new FlowLayout(FlowLayout.CENTER);
	protected ResourceMap resourceMap;
	private String TITLE = "title";
	protected StyleToolkit toolkit = new DefaultStyleToolkit();

	public <T extends Window> AbstractFrame(Class<T> clazz) {
		resourceMap = new GlobalResourceMap(clazz);
	}

	@Override
	public void activate() {
		logger.info("initialize " + getClass() + " frame...");
		// ~ initialize frame
		setVisible(true);
		setTitle(getResourceMap().getString(TITLE));
		setLayout(layout);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		logger.info("do " + getClass() + " self adpaption...");
		doSelfAdaption();

		logger.info("initialize " + getClass() + " custom content...");
		initialize();

		logger.info("activate " + getClass() + " custom content...");
		activateContent(getContentPane().getComponents());

		logger.info("validate " + getClass() + " whole frame...");
		validate();
	}

	private void activateContent(Component[] components) {
		for (Component component : components) {
			if (TieContainer.class.isAssignableFrom(component.getClass())) {
				TieContainer panel = (TieContainer) component;
				panel.activate();
				activateContent(panel.getComponents());
			}
		}
	}

	private void doSelfAdaption() {
		// get screen size
		Rectangle rec = toolkit.getScreenSize(this.getGraphicsConfiguration());
		logger.debug("rec:" + rec);

		doSelfAdaption(rec, this);

		getContentPane().setPreferredSize(getPreferredSize());
	}

	@Override
	public ResourceMap getResourceMap() {
		return resourceMap;
	}

	public void reload() {
		getContentPane().removeAll();

		activate();
	}

	@Override
	public void setResourceMap(ResourceMap resourceMap) {
		this.resourceMap = resourceMap;
	}

	public void setToolkit(StyleToolkit toolkit) {
		this.toolkit = toolkit;
	}
}
