package common;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import parsers.Parser;

public class ParserService implements Callable<Exception> {

	private static ExecutorService executor = Executors.newCachedThreadPool();

	private Parser parser;

	private String[] args;

	@Override
	public Exception call() throws Exception {
		return parser.parse(args);
	}

	public ParserService(Parser parser) {
		this.parser = parser;
	}

	public Exception invoke(String... args) {
		this.args = args;
		Future<Exception> future = executor.submit(this);
		Exception exception = null;
		try {
			exception = future.get(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			exception = e;
		} finally {
			if (!future.isDone()) {
				future.cancel(true);
			}
		}
		return exception;
	}

	public static void finish() {
		executor.shutdown();
	}

}
