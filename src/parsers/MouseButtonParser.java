package parsers;

import java.awt.event.InputEvent;
import java.text.ParseException;

public class MouseButtonParser extends Parser {

	@Override
	public void parseMethod(String... strs) throws ParseException {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

}
