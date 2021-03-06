package common.actions;

import common.server.LoginException;

public class LoginAction extends Action {

	private static final long serialVersionUID = -876809589176858345L;
	private String password;
	private String info;
	private String ip;
	private transient String serverPassword;

	@Override
	public void doAction() {
		if (password == null || !password.equals(serverPassword)) {
			throw new LoginException(info, ip);
		}
	}

	public LoginAction(String password, String info, String ip) {
		this.password = password;
		this.info = info;
		this.ip = ip;
	}

	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

	public String getInfo() {
		return info;
	}

	public String getIp() {
		return ip;
	}

}
