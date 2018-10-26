package priv.lee.cad.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import priv.lee.cad.constant.StandardPrompt;
import priv.lee.cad.layout.DefaultGroupLayout;
import priv.lee.cad.model.ServerClientTemporary;
import priv.lee.cad.util.ClientAssert;
import priv.lee.cad.util.ClientInstanceUtils;
import priv.lee.cad.util.ObjectUtils;
import priv.lee.cad.util.PropertiesUtils;
import priv.lee.cad.util.StringUtils;
import priv.lee.cad.util.XmlUtils;

public abstract class AbstractLoginFrame extends AbstractFrame implements ActionListener, Runnable {

	public class ServerPanel extends AbstractPanel {

		private static final long serialVersionUID = -5325225997433722990L;
		private PromptTextField.PromptTextFieldDimension dimension;
		private final double HEIGHT_PROPORTION = 0.1d;
		private int hGap;
		private final double HGAP_PROPORTION = 0.02d;
		public PromptTextField host;
		private final String HOST_LABEL_DISPLAY = "host";
		private final double LABEL_PROPORTION = 0.1d;
		public PromptTextField pwd;
		private final String PWD_LABEL_DISPLAY = "password";
		public JCheckBox remeberme;
		private final String REMEBERME_DISPLAY = "remeberme";
		private final String REMINDER_LABEL_DISPLAY = "reminder";
		private final double TEXT_PROPORTION = 0.8d;
		private final String TITLE = "title";
		public PromptTextField user;
		private final String USER_LABEL_DISPLAY = "user";
		private int vGap;
		private final double VGAP_PROPORTION = 0d;

		private String getCachePwd() {
			return ObjectUtils.isEmpty(temporary) ? "" : temporary.getUserPasswd();
		}

		private String getCacheUser() {
			return ObjectUtils.isEmpty(temporary) ? "" : temporary.getUserName();
		}

		@Override
		public double getHorizontalProportion() {
			return 0.9d;
		}

		private String getHost() {
			return (ObjectUtils.isEmpty(temporary) || StringUtils.isEmpty(temporary.getServer()))
					? PropertiesUtils.readProperty(HOST_URL)
					: temporary.getServer();
		}

		@Override
		public double getVerticalProportion() {
			return 0.7d;
		}

		@Override
		public void initialize() {
			// set panel border to be title and etched type
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
					getResourceMap().getString(TITLE), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, toolkit.getFont()));

			// ~ add components
			JLabel reminder = new JLabel(getResourceMap().getString(REMINDER_LABEL_DISPLAY));
			reminder.setForeground(Color.RED);

			dimension = PromptTextField.newDimension(getPreferredSize(), LABEL_PROPORTION, TEXT_PROPORTION,
					HEIGHT_PROPORTION);
			host = PromptTextField.newInstance(getResourceMap().getString(HOST_LABEL_DISPLAY), getHost(), dimension);
			host.setLabelAligment(SwingConstants.LEFT);
			host.setEditable(isHostEditable());

			user = PromptTextField.newInstance((getResourceMap().getString(USER_LABEL_DISPLAY)), getCacheUser(),
					dimension);
			user.setLabelAligment(SwingConstants.LEFT);

			pwd = PromptTextField.newInstance((new JLabel(getResourceMap().getString(PWD_LABEL_DISPLAY))),
					new JPasswordField(getCachePwd()), dimension);
			pwd.setLabelAligment(SwingConstants.LEFT);

			remeberme = new JCheckBox(getResourceMap().getString(REMEBERME_DISPLAY));
			remeberme.setSelected(isRemeberme());

			// ~ performance hGap and vGap
			hGap = ((Double) (getPreferredSize().width * HGAP_PROPORTION)).intValue();
			vGap = ((Double) (getPreferredSize().height * VGAP_PROPORTION)).intValue();
			logger.debug("hGap:" + hGap + ",vGap:" + vGap);

			logger.info("use default group layout...");
			// ~ use default group layout
			DefaultGroupLayout layout = new DefaultGroupLayout(this, hGap, vGap);
			layout.addComponent(reminder).addComponent(host).addComponent(user).addComponent(pwd)
					.addComponent(remeberme).layout();
		}

		private boolean isHostEditable() {
			return Boolean.parseBoolean(PropertiesUtils.readProperty(HOST_EDITABLE));
		}

		private boolean isRemeberme() {
			return ObjectUtils.isEmpty(temporary) ? false : temporary.isRememberMe();
		}
	}

	private final static String HOST_EDITABLE = "host.editable";
	private final static String HOST_URL = "host.url";
	private static final Logger logger = Logger.getLogger(AbstractLoginFrame.class);
	private static final long serialVersionUID = -8688157705470416228L;
	private final String LOGIN_BUTTON_DISPLAY = "login";
	protected ServerPanel server;
	private ServerClientTemporary temporary;

	public AbstractLoginFrame(Class<? extends AbstractLoginFrame> frameClass,
			Class<? extends ServerClientTemporary> tempClass) {
		super(frameClass);
		this.temporary = XmlUtils.read(tempClass);
		logger.debug("client temporary:" + temporary);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		temporary = toTemporary();

		boolean isConnect = ClientInstanceUtils.connect(temporary.getServer(), temporary.getUserName(),
				temporary.getUserPasswd());

		ClientAssert.isTrue(isConnect, StandardPrompt.USER_NAME_PWD_ERROR);

		logger.info("begin to write client temporay...");
		XmlUtils.store(temporary);

		logger.info("invoke next frame...");
		EventQueue.invokeLater(this);
	}

	@Override
	public double getHorizontalProportion() {
		return 0.35d;
	}

	@Override
	public double getVerticalProportion() {
		return 0.35d;
	}

	@Override
	public void initialize() {
		logger.info("initialize " + getClass() + "  server content...");
		server = new ServerPanel();
		add(server);

		logger.info("initialize " + getClass() + "  option content...");
		Option login = new Option(LOGIN_BUTTON_DISPLAY, null, this);
		add(new OptionPanel(Arrays.asList(login, Option.newCancelOption(this))));

		logger.info("initialize " + getClass() + "  completed...");
	}

	@Override
	public void run() {
		// set next frame activate
		startNextFrame();

		dispose();
	}

	public abstract void startNextFrame();

	public abstract ServerClientTemporary toTemporary();
}
