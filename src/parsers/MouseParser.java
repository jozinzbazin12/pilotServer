package parsers;

import java.awt.MouseInfo;
import java.awt.Point;
import java.text.ParseException;

public class MouseParser extends Parser {

	@Override
	public void parseMethod(String... strs) throws ParseException {
		Point p = MouseInfo.getPointerInfo().getLocation();
		double dx = Double.parseDouble(strs[1]);
		double dy = Double.parseDouble(strs[2]);
		robot.mouseMove((int) (p.x + dx), (int) (p.y + dy));
	}

}
