package priv.lee.cad.ui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Window;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.model.ResourceMapper;
import priv.lee.cad.model.SelfAdaptionPanel;
import priv.lee.cad.model.StyleToolkit;
import priv.lee.cad.model.TieContainer;
import priv.lee.cad.model.impl.DefaultStyleToolkit;
import priv.lee.cad.model.impl.GlobalResourceMap;

public abstract class AbstractPanel extends JPanel implements SelfAdaptionPanel, ResourceMapper, TieContainer {

	private static final Logger logger = Logger.getLogger(AbstractPanel.class);
	private static final long serialVersionUID = 1508737322646907563L;
	private boolean doSelfAdaption = true;
	private LayoutManager layout = new FlowLayout(FlowLayout.CENTER);
	protected String PREFIX;
	private ResourceMap resourceMap;
	protected StyleToolkit toolkit = new DefaultStyleToolkit();
	{
		PREFIX = getClass().getSimpleName();
	}

	@Override
	public void activate() {
		this.resourceMap = initWindowResourceMap(getParent());
		initComponents();
	}

	@Override
	public ResourceMap getResourceMap() {
		return resourceMap;
	}

	protected void initComponents() {
		logger.info("initialize  " + getClass() + " ...");
		setLayout(layout);

		if (doSelfAdaption) {
			logger.info("do " + getClass() + " self adpaption...");
			// ~ do panel self adaption
			doSelfAdaption(getParent().getPreferredSize(), this);
		}

		logger.info("initialize " + getClass() + " custom content...");
		initialize();
	}

	protected ResourceMap initWindowResourceMap(Container container) {
		if (container instanceof Window) {
			Window window = (Window) container;
			logger.info("find resource window:" + window.getClass());
			return new GlobalResourceMap(PREFIX, window.getClass());
		}
		return initWindowResourceMap(container.getParent());
	}

	public void setDoSelfAdaption(boolean doSelfAdaption) {
		this.doSelfAdaption = doSelfAdaption;
	}

	@Override
	public void setResourceMap(ResourceMap resourceMap) {
		this.resourceMap = resourceMap;
	}

	public void setToolkit(StyleToolkit toolkit) {
		this.toolkit = toolkit;
	}
}
