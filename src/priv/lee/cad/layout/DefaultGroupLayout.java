package priv.lee.cad.layout;

import java.awt.Container;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JComponent;

import priv.lee.cad.util.ClientAssert;

public class DefaultGroupLayout extends GroupLayout {

	private List<JComponent> components = new ArrayList<JComponent>();
	private int hGap;
	private int vGap;;

	public DefaultGroupLayout(Container container, int hGap, int vGap) {
		super(container);
		this.hGap = hGap;
		this.vGap = vGap;
		container.setLayout(this);
	}

	public DefaultGroupLayout addComponent(JComponent component) {
		this.components.add(component);
		return this;
	}

	public DefaultGroupLayout addComponent(List<? extends JComponent> components) {
		this.components.addAll(components);
		return this;
	}

	public void layout() {
		ClientAssert.notNull(components, "Layout components is required");

		GroupLayout.SequentialGroup hsGroup = createSequentialGroup().addGap(hGap);
		GroupLayout.SequentialGroup vsGroup = createSequentialGroup().addGap(vGap);
		ParallelGroup hpGroup = createParallelGroup();
		for (JComponent component : components) {
			// add to horizontal group
			hpGroup.addComponent(component);

			// add to vertical group
			vsGroup.addGroup(createParallelGroup().addComponent(component));
			vsGroup.addGap(vGap);
		}
		hsGroup.addGroup(hpGroup);
		hsGroup.addGap(hGap);
		setHorizontalGroup(hsGroup);
		setVerticalGroup(vsGroup);
	}

	public void layout(int quantityOfPerRow) {
		ClientAssert.isTrue(quantityOfPerRow > 0, "The quantity of per row must greater than 0");
		ClientAssert.notNull(components, "Layout components is required");

		// ~ handle horizontal and vertical group layout
		int quantityOfRow = new BigDecimal(components.size()).divide(new BigDecimal(quantityOfPerRow), RoundingMode.UP)
				.intValue();
		GroupLayout.SequentialGroup hsGroup = createSequentialGroup().addGap(hGap);
		GroupLayout.SequentialGroup vsGroup = createSequentialGroup().addGap(vGap);
		Map<Integer, ParallelGroup> map = new HashMap<Integer, ParallelGroup>();
		for (int i = 0; i < quantityOfPerRow; i++) {
			ParallelGroup hpGroup = null;
			for (int j = 0; j < quantityOfRow; j++) {
				int index = j * quantityOfPerRow + i;
				if (index >= components.size()) {
					break;
				}

				// ~ add to horizontal group
				JComponent component = components.get(index);
				if (hpGroup == null) {
					hpGroup = createParallelGroup();
				}
				hpGroup.addComponent(component);

				// ~ add to verticals group
				ParallelGroup vpGroup = map.get(j);
				if (vpGroup == null) {
					vpGroup = createParallelGroup();
					vsGroup.addGroup(vpGroup);
					vsGroup.addGap(vGap);
					map.put(j, vpGroup);
				}
				vpGroup.addComponent(component);
			}

			if (hpGroup == null) {
				break;
			}
			hsGroup.addGroup(hpGroup);
			hsGroup.addGap(hGap);
		}
		setHorizontalGroup(hsGroup);
		setVerticalGroup(vsGroup);
	}
}
