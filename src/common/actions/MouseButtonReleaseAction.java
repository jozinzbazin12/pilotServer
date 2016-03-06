package common.actions;

public class MouseButtonReleaseAction extends Action {

	private static final long serialVersionUID = 3845444684666452438L;
	private int key;

	@Override
	public void doAction() {
		robot.mouseRelease(key);
	}

	public void setKey(int key) {
		this.key = key;
	}

}
