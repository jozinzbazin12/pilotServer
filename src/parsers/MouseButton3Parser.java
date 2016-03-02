package parsers;

import java.awt.event.InputEvent;
import java.text.ParseException;

public class MouseButton3Parser extends Parser {

	@Override
	public void parseMethod(String... strs) throws ParseException {
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
	}

}
