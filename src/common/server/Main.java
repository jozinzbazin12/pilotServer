package common.server;

import common.ui.ServerWindow;

public class Main {

	public static void main(String... args) {
		new ServerWindow();
//		while (true) {
//			try {
//				server.listen();
//				server.setState(ServerState.SHUTDOWN);
//			} catch (Exception e) {
//				server.restart();
//			}
//		}
	}
}
