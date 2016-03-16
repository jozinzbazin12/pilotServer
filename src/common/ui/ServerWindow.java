package common.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import common.server.ServerState;
import common.server.Settings;
import common.server.WebSocketServer;

public class ServerWindow extends JFrame implements ActionListener {

	private static final String CONTROLLER_SERVER = "Controller server";
	private static final Logger log = LogManager.getLogger();
	private static final int SLEEP = 1000;
	private static final long serialVersionUID = 2687314377956367316L;
	private WebSocketServer server;
	private JButton stop;
	private JButton start;
	private JLabel stateIndicator;
	private Thread monitorThread;
	private JTextField portInput;
	private JComboBox<Level> levels;
	private LoggerConfig loggerConfig;
	private LoggerContext ctx;
	private Settings settings;
	private boolean error = false;

	public ServerWindow() {
		settings = Settings.getSettings();
		ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		server = WebSocketServer.getInstance();
		setTitle(CONTROLLER_SERVER);
		setSize(400, 600);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			log.catching(e);
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		initComponents();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (server.getStatus() != ServerState.SHUTDOWN) {
					String ObjButtons[] = { "Yes", "No" };
					int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", CONTROLLER_SERVER,
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
					if (PromptResult == JOptionPane.YES_OPTION) {
						quit();
					}
				} else {
					quit();
				}
			}
		});
		revalidate();
	}

	private void quit() {
		Settings.saveSettings();
		System.exit(0);
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		add(createSettingsPanel(), BorderLayout.NORTH);
		add(createLogPanel());
		monitorThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					ServerState s = server.getStatus();
					if (s == ServerState.SHUTDOWN) {
						setServerRunnig(false);
					} else {
						setServerRunnig(true);
					}
					stateIndicator.setText(s.getMsg());
					revalidate();
					try {
						sleep(SLEEP);
					} catch (InterruptedException e) {
						log.catching(e);
					}

				}
			}
		};
		monitorThread.start();
	}

	private JPanel createLogPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Logs"));
		Level[] values = Level.values();
		Arrays.sort(values);
		levels = new JComboBox<>(values);
		levels.addActionListener(this);
		levels.setSelectedItem(settings.getLevel());
		levels.setMaximumSize(new Dimension(50, 20));
		panel.add(levels, BorderLayout.NORTH);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setFont(new Font("Arial", 0, 11));
		JScrollPane scrollPanel = new JScrollPane(textArea);
		JTextAreaAppender.addTextArea(textArea);
		panel.add(scrollPanel);

		return panel;
	}

	private JPanel createSettingsPanel() {
		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.setBorder(BorderFactory.createTitledBorder("Server settings"));
		JLabel ip = new JLabel("IP");
		String hostAddress = "Error";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		JTextField ipInput = new JTextField(hostAddress);
		ipInput.setEditable(false);
		panel.add(ip);
		panel.add(ipInput);

		JLabel port = new JLabel("Port");
		portInput = new JTextField(String.valueOf(settings.getPort()));
		portInput.addActionListener(this);

		panel.add(port);
		panel.add(portInput);

		JLabel state = new JLabel("Server state");
		stateIndicator = new JLabel();
		panel.add(state);
		panel.add(stateIndicator);

		stop = new JButton("Stop");
		stop.addActionListener(this);
		start = new JButton("Start");
		start.addActionListener(this);
		panel.add(stop);
		panel.add(start);
		return panel;
	}

	private void setServerRunnig(boolean b) {
		if (!error) {
			start.setEnabled(!b);
		}
		portInput.setEnabled(!b);
		stop.setEnabled(b);
	}

	private void invalidValue() {
		JOptionPane.showMessageDialog(new JFrame(), "Invalid port!", "Error", JOptionPane.ERROR_MESSAGE);
		error = true;
		start.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(portInput)) {
			String text = portInput.getText();
			if (!text.isEmpty()) {
				try {
					int port = Integer.valueOf(text);
					settings.setPort(port);
					start.setEnabled(true);
					error = false;
				} catch (Exception ex) {
					invalidValue();
				}
			} else {
				invalidValue();
			}
		} else if (e.getSource().equals(start)) {
			server.start(settings.getPort());
		} else if (e.getSource().equals(stop)) {
			server.stop();
		} else if (e.getSource().equals(levels)) {
			Level level = (Level) levels.getSelectedItem();
			settings.setLevel(level);
			loggerConfig.setLevel(Level.INFO);
			ctx.updateLoggers();
			log.info("Logger level changed to: " + level);
			loggerConfig.setLevel(level);
			ctx.updateLoggers();
		}
	}

}
