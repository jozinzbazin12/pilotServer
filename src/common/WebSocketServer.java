package common;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import common.actions.Action;

public class WebSocketServer {

	private static int port = 5555;
	private static final int ERROR_THRESHOLD = 5;
	private static WebSocketServer instance;
	private ServerSocket server;
	private Socket socket;
	private int errorCount;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	private static synchronized WebSocketServer getInstance() {
		if (instance == null) {
			instance = new WebSocketServer();
		}
		return instance;
	}

	private WebSocketServer() {
		init();
	}

	private void init() {
		try {
			server = new ServerSocket(port);
			System.out.println("Waiting for connection on port " + port + "...");
			socket = server.accept();
			socket.setReuseAddress(false);
			socket.setKeepAlive(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restart() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}

	public static void main(String... args) {
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		WebSocketServer server = getInstance();
		while (true) {
			try {
				server.listen();
			} catch (Exception e) {
				server.restart();
			}
		}
	}

	private void listen() throws InterruptedException, IOException {
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		while (!socket.isConnected() && inputStream.available() == 0) {
			Thread.sleep(1000);
		}
		System.out.println("connected");
		input = new ObjectInputStream(inputStream);
		output = new ObjectOutputStream(outputStream);
		estabilish();
		Response res = null;
		errorCount = 0;
		while (!socket.isClosed()) {
			try {
				Action a = (Action) input.readObject();
				System.out.println(a);
				a.doAction();
				res = new Response(Status.OK);
			} catch (SocketException | EOFException e) {
				Thread.sleep(1000);
				errorCount++;
				e.printStackTrace();
				isError();
				continue;
			} catch (Exception e) {
				System.out.println("Exception occured:\n");
				e.printStackTrace();
				res = new Response(Status.NOT_OK, e);
			}
			sendResponse(output, res);
			errorCount = 0;
		}
	}

	private void estabilish() throws IOException {
		Response r;
		try {
			r = (Response) input.readObject();
			if (r.getStatus() == Status.CLIENT_CONNECT) {
				r.setStatus(Status.SERVER_OK);
				output.writeObject(r);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private boolean isError() {
		if (errorCount <= ERROR_THRESHOLD) {
			return false;
		}
		// return true;
		throw new RuntimeException("Connection lost");
	}

	private void sendResponse(ObjectOutputStream output, Response res) {
		boolean error = false;
		while (!error && !isError()) {
			try {
				output.writeObject(res);
				output.flush();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				error = true;
				errorCount++;
			}
		}
	}

}
