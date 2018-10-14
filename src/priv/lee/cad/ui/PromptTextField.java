package priv.lee.cad.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import priv.lee.cad.model.MiddleAlignGap;
import priv.lee.cad.util.ClientAssert;
import priv.lee.cad.util.ObjectUtils;

public class PromptTextField extends JComponent {

	public static class PromptTextFieldDimension {
		public int height;
		public int promptWidth;
		public int textWidth;

		public PromptTextFieldDimension(Dimension parentSize, double promptWidthProportion, double textWidthProportion,
				double heightProportion) {
			ClientAssert.notNull(parentSize, "Parent size must not be null");
			ClientAssert.isTrue(promptWidthProportion > 0 && promptWidthProportion <= 1,
					"Prompt width proportion must be greater than 0 less than 1 or equal to 1");
			ClientAssert.isTrue(textWidthProportion > 0 && textWidthProportion <= 1,
					"Text width proportion must be greater than 0 less than 1 or equal to 1");
			ClientAssert.isTrue(heightProportion > 0 && heightProportion <= 1,
					"Height proportion must be greater than 0 less than 1 or equal to 1");

			this.promptWidth = ((Double) (parentSize.width * promptWidthProportion)).intValue();
			this.textWidth = ((Double) (parentSize.width * textWidthProportion)).intValue();
			this.height = ((Double) (parentSize.height * heightProportion)).intValue();
		}

		public PromptTextFieldDimension(int promptWidth, int textWidth, int height) {
			ClientAssert.isTrue(promptWidth > 0, "Prompt width needs to be greater than 0");
			ClientAssert.isTrue(textWidth > 0, "Text width needs to be greater than 0");
			ClientAssert.isTrue(height > 0, "height needs to be greater than 0");

			this.promptWidth = promptWidth;
			this.textWidth = textWidth;
			this.height = height;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("PromptTextFieldDimension [promptWidth=");
			builder.append(promptWidth);
			builder.append(", textWidth=");
			builder.append(textWidth);
			builder.append(", height=");
			builder.append(height);
			builder.append("]");
			return builder.toString();
		}
	}

	private static final Logger logger = Logger.getLogger(PromptTextField.class);

	private static final long serialVersionUID = -6696586333097590589L;

	public static PromptTextFieldDimension newDimension(Dimension parentSize, double promptWidthProportion,
			double textWidthProportion, double heightProportion) {
		return new PromptTextFieldDimension(parentSize, promptWidthProportion, textWidthProportion, heightProportion);
	}

	public static PromptTextField newInstance(JLabel prompt, JTextField text) {
		return newInstance(prompt, text, null);
	}

	public static PromptTextField newInstance(JLabel prompt, JTextField text, PromptTextFieldDimension dimension) {
		return new PromptTextField(prompt, text, dimension);
	}

	public static PromptTextField newInstance(String prompt, String text) {
		return newInstance(new JLabel(prompt), new JTextField(text));
	}

	public static PromptTextField newInstance(String prompt, String text, PromptTextFieldDimension dimension) {
		return newInstance(new JLabel(prompt), new JTextField(text), dimension);
	}

	private PromptTextFieldDimension dimension;
	private MiddleAlignGap gap = new MiddleAlignGap(5, 5);
	private int labelAligment = SwingConstants.RIGHT;
	private LayoutManager layout = new FlowLayout(FlowLayout.LEFT, gap.hGap, gap.vGap);
	private JLabel prompt;

	private JTextField text;

	private PromptTextField(JLabel prompt, JTextField text, PromptTextFieldDimension dimension) {
		ClientAssert.notNull(prompt, "Prompt can not be null");
		ClientAssert.notNull(text, "Prompt can not be null");
		logger.debug("prompt:" + prompt + ",text:" + text + ",dimension:" + dimension);

		this.prompt = prompt;
		this.text = text;
		this.dimension = dimension;

		initialize();
	}

	public PromptTextFieldDimension getDimension() {
		return dimension;
	}

	@Override
	public LayoutManager getLayout() {
		return layout;
	}

	public JLabel getPrompt() {
		return prompt;
	}

	public JTextField getText() {
		return text;
	}

	public String getTextContent() {
		return ObjectUtils.isEmpty(text) ? null : text.getText();
	}

	private PromptTextField initialize() {
		setLayout(layout);

		if (!ObjectUtils.isEmpty(dimension)) {
			logger.debug("promptWidth:" + dimension.promptWidth + ",textWidth:" + dimension.textWidth + ",height:"
					+ dimension.height);
			prompt.setPreferredSize(new Dimension(dimension.promptWidth, dimension.height));
			text.setPreferredSize(new Dimension(dimension.textWidth, dimension.height));
		}

		if (logger.isDebugEnabled()) {
			prompt.setBorder(BorderFactory.createLineBorder(Color.RED));
			text.setBorder(BorderFactory.createLineBorder(Color.RED));
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		prompt.setHorizontalAlignment(labelAligment);

		add(prompt);
		add(text);

		return this;
	}

	public boolean isEditable() {
		return text.isEditable();
	}

	public void setDimension(PromptTextFieldDimension dimension) {
		this.dimension = dimension;
	}

	public void setEditable(boolean editable) {
		text.setEditable(editable);
	}

	public void setGap(MiddleAlignGap gap) {
		this.gap = gap;
	}

	public void setLabelAligment(int labelAligment) {
		prompt.setHorizontalAlignment(labelAligment);
	}

	public void setPrompt(JLabel prompt) {
		this.prompt = prompt;
	}

	public void setText(JTextField text) {
		this.text = text;
	}
}
