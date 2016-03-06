package common.actions;

import java.awt.event.KeyEvent;

public class KeyPressAction extends Action {

	private static final long serialVersionUID = -6133657322117508464L;

	private char c;

	@Override
	public void doAction() {
		int key = KeyEvent.getExtendedKeyCodeForChar(c);
		robot.keyPress(key);
	}

	public void setC(char c) {
		this.c = c;
	}

}
