package common.actions;

public class MouseScrollAction extends Action {

	private static final long serialVersionUID = 5858471459649534833L;
	protected int lines;

	@Override
	public void doAction() {
		robot.mouseWheel(lines);
	}

	public MouseScrollAction(int lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		str.append(", ").append(lines);
		return str.toString();
	}

}
