package common;

public enum Command {
	MOUSE("mouseMove"), MOUSE1("mouse1"), MOUSE2("mouse2"), MOUSE3("mouse3"), KEY("key"), CONSOLE("console"), SHUTDOWN(
			"shutdown"), RESTART("restart"), PLAY("play"), PAUSE("pause");
	private String command;

	private Command(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	public static Command fromString(String text) {
		if (text != null) {
			for (Command b : Command.values()) {
				if (text.equalsIgnoreCase(b.command)) {
					return b;
				}
			}
		}
		return null;
	}

}
