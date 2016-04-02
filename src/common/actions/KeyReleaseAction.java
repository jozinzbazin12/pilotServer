package common.actions;

import java.awt.event.KeyEvent;

public class KeyReleaseAction extends KeyAction {

	private static final long serialVersionUID = -6133657322117508464L;

	public KeyReleaseAction(char c) {
		super(c);
	}

	@Override
	public void doAction() {
		int key = KeyEvent.getExtendedKeyCodeForChar(c);
		robot.keyRelease(key);
	}

}
