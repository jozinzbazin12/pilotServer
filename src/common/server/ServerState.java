package common.server;

public enum ServerState {
	CONNECTED("Connected"), WAITING("Wating"), SHUTDOWN("Shutdown"), CONNECTION_ERROR("Connection error");

	private String msg;

	private ServerState(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
