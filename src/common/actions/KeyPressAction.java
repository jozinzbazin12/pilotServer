package common.actions;

import java.awt.event.KeyEvent;

public class KeyPressAction extends KeyAction {

	private static final long serialVersionUID = -6133657322117508464L;

	@Override
	public void doAction() {
		int key = KeyEvent.getExtendedKeyCodeForChar(c);
		robot.keyPress(key);
	}
}
