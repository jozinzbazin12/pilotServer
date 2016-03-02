package parsers;

import java.awt.event.InputEvent;
import java.text.ParseException;

public class MouseButton2Parser extends Parser {

	@Override
	public void parseMethod(String... strs) throws ParseException {
		robot.mousePress(InputEvent.BUTTON2_MASK);
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
	}

}
