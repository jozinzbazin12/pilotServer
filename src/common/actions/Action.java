package common.actions;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.Serializable;

public abstract class Action implements Serializable {

	private static final long serialVersionUID = 4977720464579612654L;

	protected static Robot robot;

	public abstract void doAction();

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

}
