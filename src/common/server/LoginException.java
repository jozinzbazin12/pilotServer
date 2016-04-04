package common.server;

public class LoginException extends RuntimeException {

	private static final long serialVersionUID = -1433240660595657573L;

	private String who;
	private String where;

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public LoginException(String who, String where) {
		this.who = who;
		this.where = where;
	}

}
