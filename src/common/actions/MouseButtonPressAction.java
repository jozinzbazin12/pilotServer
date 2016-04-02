package common.actions;

public class MouseButtonPressAction extends MouseButtonAction {

	private static final long serialVersionUID = 3845444684666452438L;

	public MouseButtonPressAction(int key) {
		super(key);
	}

	@Override
	public void doAction() {
		robot.mousePress(key);
	}

}
