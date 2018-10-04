package priv.lee.cad.model;

import java.awt.Component;
import java.awt.Dimension;

import org.apache.log4j.Logger;

import priv.lee.cad.ui.AbstractPanel;
import priv.lee.cad.util.ClientAssert;

public interface SelfAdaptionPanel extends SelfAdaptionComponent {

	static final Logger logger = Logger.getLogger(SelfAdaptionPanel.class);

	@Override
	default void doSelfAdaption(Cloneable cloneable, Component component) {
		ClientAssert.notNull(cloneable, "Cloneable must not be null");
		ClientAssert.notNull(component, "Component must not be null");
		ClientAssert.isInstanceOf(AbstractPanel.class, component, "Component must extends AbstractPanel");
		ClientAssert.isInstanceOf(Dimension.class, cloneable, "Cloneable must extends Dimension");

		double horizontalProportion = getHorizontalProportion();
		double verticalProportion = getVerticalProportion();
		Dimension dimension = (Dimension) cloneable;

		logger.debug("cloneable:" + dimension + ",horizontalProportion:" + horizontalProportion + ",verticalProportion:"
				+ verticalProportion);
		ClientAssert.isTrue(horizontalProportion > 0 && horizontalProportion <= 1,
				"Horizontal proportion must be greater than 0 less than 1 or equal to 1");
		ClientAssert.isTrue(verticalProportion > 0 && verticalProportion <= 1,
				"Vertical proportion must be greater than 0 less than 1 or equal to 1");

		AbstractPanel panel = (AbstractPanel) component;
		// performance by proportion
		Double width = dimension.width * getHorizontalProportion();
		Double height = dimension.height * getVerticalProportion();
		logger.debug(panel.getClass() + ",width:" + width + ",height:" + height);
		panel.setPreferredSize(new Dimension(width.intValue(), height.intValue()));
	}
}
