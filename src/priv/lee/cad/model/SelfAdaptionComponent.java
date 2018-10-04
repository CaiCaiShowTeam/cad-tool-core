package priv.lee.cad.model;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import org.apache.log4j.Logger;

import priv.lee.cad.util.ClientAssert;

public interface SelfAdaptionComponent {

	static final Logger logger = Logger.getLogger(SelfAdaptionComponent.class);

	default void doSelfAdaption(Cloneable cloneable, Component component) {
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

	public double getHorizontalProportion();

	public double getVerticalProportion();
}
