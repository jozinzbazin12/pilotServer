package common.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import common.server.ServerState;
import common.server.WebSocketServer;

public class ServerWindow extends JFrame implements ActionListener {

	private static final int SLEEP = 1000;
	private static final long serialVersionUID = 2687314377956367316L;
	private WebSocketServer server;
	private JButton stop;
	private JButton start;
	private JLabel stateIndicator;
	private Thread monitorThread;
	private JPanel mainPanel;

	public ServerWindow() {
		server = WebSocketServer.getInstance();
		setSize(400, 600);
		setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
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
					int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?",
							"Online Examination System", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
							ObjButtons, ObjButtons[1]);
					if (PromptResult == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} else {
					System.exit(0);
				}
			}
		});
		revalidate();
	}

	private void initComponents() {
		setLayout(new GridLayout(3, 1));
		mainPanel = new JPanel(new GridLayout(3, 4));
		JLabel ip = new JLabel("IP");
		String hostAddress = "Error";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		JTextField ipInput = new JTextField(hostAddress);
		ipInput.setEditable(false);
		mainPanel.add(ip);
		mainPanel.add(ipInput);

		JLabel port = new JLabel("Port");
		JTextField portInput = new JTextField("5555");
		mainPanel.add(port);
		mainPanel.add(portInput);
		add(mainPanel);

		JLabel state = new JLabel("Server state");
		stateIndicator = new JLabel();
		mainPanel.add(state);
		mainPanel.add(stateIndicator);

		stop = new JButton("Stop");
		stop.addActionListener(this);
		start = new JButton("Start");
		start.addActionListener(this);
		mainPanel.add(stop);
		mainPanel.add(start);
		add(mainPanel);
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
					mainPanel.revalidate();
					try {
						sleep(SLEEP);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};
		monitorThread.start();
	}

	private void setServerRunnig(boolean b) {
		start.setEnabled(!b);
		stop.setEnabled(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(start)) {
			server.start();
		} else if (e.getSource().equals(stop)) {
			server.stop();
		}
	}

}
