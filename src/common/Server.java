package common;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import parsers.KeyParser;
import parsers.MouseButton2Parser;
import parsers.MouseButton3Parser;
import parsers.MouseButtonParser;
import parsers.MouseParser;

public class Server {

	private static final Map<Command, ParserService> parsers = new HashMap<>();

	public static void main(String... args) throws ParseException {
		parsers.put(Command.MOUSE, new ParserService(new MouseParser()));
		parsers.put(Command.MOUSE1, new ParserService(new MouseButtonParser()));
		parsers.put(Command.MOUSE2, new ParserService(new MouseButton2Parser()));
		parsers.put(Command.MOUSE3, new ParserService(new MouseButton3Parser()));
		parsers.put(Command.KEY, new ParserService(new KeyParser()));

		listen();
		ParserService.finish();
	}

	private static void listen() {
		while (true) {
			String[] incoming = { Command.KEY.getCommand(), "\t" };
			Command c = Command.fromString(incoming[0]);
			ParserService parser = parsers.get(c);
			Exception e = parser.invoke(incoming);
			if (e != null) {
				System.out.println("Exception occured:\n");
				e.printStackTrace();
			}
			break;
		}

	}
}
