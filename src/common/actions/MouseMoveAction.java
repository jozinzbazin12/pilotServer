package common.actions;

import java.awt.MouseInfo;
import java.awt.Point;

public class MouseMoveAction extends Action {

	private static final long serialVersionUID = -2550044285460186164L;
	private double dx;
	private double dy;

	public MouseMoveAction(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void doAction() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		robot.mouseMove((int) (p.x + dx), (int) (p.y + dy));
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		str.append(", dx:").append(dx).append(", dy:").append(dy);
		return str.toString();
	}
}
