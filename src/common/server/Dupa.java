package common.server;

public class Dupa {

	private volatile ServerCommand command = ServerCommand.NOTHING;
	private volatile ServerState state = ServerState.SHUTDOWN;

	public synchronized ServerCommand getCommand() {
		return command;
	}

	public synchronized void setCommand(ServerCommand command) {
		this.command = command;
	}

	public synchronized ServerState getState() {
		return state;
	}

	public synchronized void setState(ServerState state) {
		this.state = state;
	}

}
