package parsers;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.ParseException;

public abstract class Parser {

	protected Robot robot;

	protected abstract void parseMethod(String... strs) throws ParseException;

	public Exception parse(String... strs) {
		try {
			parseMethod(strs);
		} catch (ParseException e) {
			return e;
		}
		return null;
	}

	public Parser() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}
}
