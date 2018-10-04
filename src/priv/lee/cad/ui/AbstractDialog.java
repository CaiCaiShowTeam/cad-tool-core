package priv.lee.cad.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import priv.lee.cad.model.Callback;
import priv.lee.cad.model.ResourceMap;
import priv.lee.cad.model.ResourceMapper;
import priv.lee.cad.model.SelfAdaptionComponent;
import priv.lee.cad.model.StyleToolkit;
import priv.lee.cad.model.TieContainer;
import priv.lee.cad.model.impl.DefaultStyleToolkit;
import priv.lee.cad.model.impl.GlobalResourceMap;
import priv.lee.cad.util.ClientAssert;

public abstract class AbstractDialog extends JDialog
		implements SelfAdaptionComponent, ResourceMapper, TieContainer, ActionListener {

	private static final Logger logger = Logger.getLogger(AbstractDialog.class);
	private static final long serialVersionUID = -4286037044183123449L;
	private Callback callback;
	private LayoutManager layout = new FlowLayout(FlowLayout.CENTER);
	protected ResourceMap resourceMap;
	private String TITLE = "title";
	protected StyleToolkit toolkit = new DefaultStyleToolkit();

	public <T extends AbstractDialog> AbstractDialog(Class<T> clatt, Callback callback) {
		ClientAssert.notNull(clatt, "Class is requried");

		resourceMap = new GlobalResourceMap(clatt);
		this.callback = callback;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (callback != null) {
			callback.call(setCallbackObject());
		}
		dispose();
	}

	@Override
	public void activate() {
		logger.info("initialize " + getClass() + "  window...");
		// ~ initialize dialog
		setVisible(true);
		setTitle(getResourceMap().getString(TITLE));
		setLayout(layout);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		logger.info("do  " + getClass() + "  self adpaption...");
		doSelfAdaption();

		logger.info("initialize  " + getClass() + " custom content...");
		initialize();

		logger.info("activate " + getClass() + " custom content...");
		activateContent(getContentPane().getComponents());

		logger.info("validate " + getClass() + "  whole window...");
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

	public abstract Object setCallbackObject();

	@Override
	public void setResourceMap(ResourceMap resourceMap) {
		this.resourceMap = resourceMap;
	}

	public void setToolkit(StyleToolkit toolkit) {
		this.toolkit = toolkit;
	}
}
