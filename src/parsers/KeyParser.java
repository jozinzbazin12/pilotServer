package parsers;

import java.awt.event.KeyEvent;
import java.text.ParseException;

public class KeyParser extends Parser {

	@Override
	public void parseMethod(String... strs) throws ParseException {
		int key = KeyEvent.getExtendedKeyCodeForChar(strs[1].charAt(0));
		robot.keyPress(key);
		robot.keyRelease(key);
	}

}
