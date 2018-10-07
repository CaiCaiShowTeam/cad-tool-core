package priv.lee.cad.ui;

import java.awt.Component;
import java.awt.Dimension;
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
import priv.lee.cad.util.ClientAssert;

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
	public void doSelfAdaption(Cloneable cloneable, Component component) {
		ClientAssert.notNull(cloneable, "Cloneable must not be null");
		ClientAssert.notNull(component, "Component must not be null");
		ClientAssert.isInstanceOf(Rectangle.class, cloneable, "Cloneable must extends Rectangle");

		double horizontalProportion = getHorizontalProportion();
		double verticalProportion = getVerticalProportion();
		Rectangle rec = (Rectangle) cloneable;

		logger.debug("cloneable:" + rec + ",horizontalProportion:" + horizontalProportion + ",verticalProportion:"
				+ verticalProportion);
		ClientAssert.isTrue(horizontalProportion > 0 && horizontalProportion <= 1,
				"Horizontal proportion must be greater than 0 less than 1 or equal to 1");
		ClientAssert.isTrue(verticalProportion > 0 && verticalProportion <= 1,
				"Vertical proportion must be greater than 0 less than 1 or equal to 1");

		// performance by proportion
		Double width = rec.width * horizontalProportion;
		Double height = rec.height * verticalProportion;
		Double x = (rec.width - width) / 2;
		Double y = (rec.height - height) / 2;
		logger.debug("x:" + x + ",y:" + y + ",width:" + width + ",height:" + height);
		component.setBounds(new Rectangle(x.intValue(), y.intValue(), width.intValue(), height.intValue()));
		component.setPreferredSize(new Dimension(width.intValue(), height.intValue()));
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
