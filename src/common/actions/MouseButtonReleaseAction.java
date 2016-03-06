package common.actions;

public class MouseButtonReleaseAction extends MouseButtonAction {

	private static final long serialVersionUID = 3845444684666452438L;

	@Override
	public void doAction() {
		robot.mouseRelease(key);
	}

}
