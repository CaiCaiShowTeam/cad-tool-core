package priv.lee.cad.model;

import java.awt.Component;

public interface SelfAdaptionComponent {

	public void doSelfAdaption(Cloneable cloneable, Component component);

	public double getHorizontalProportion();

	public double getVerticalProportion();
}
