package common.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import common.Response;
import common.Status;
import common.actions.Action;

public class WebSocketServer {

	private static final Logger log = LogManager.getLogger();
	private int port;
	private static final int ERROR_THRESHOLD = 5;
	private static WebSocketServer instance;
	private ServerSocket server;
	private Socket socket;
	private int errorCount;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ServerState status = ServerState.SHUTDOWN;
	private ServerCommand command = ServerCommand.NOTHING;
	private Thread thread;
	private boolean restartOnError = false;

	public static synchronized WebSocketServer getInstance() {
		if (instance == null) {
			instance = new WebSocketServer();
		}
		return instance;
	}

	private WebSocketServer() {
		thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						if (getCommand() == ServerCommand.START && getStatus() == ServerState.WAITING) {
							listen();
						} else if (getCommand() == ServerCommand.START && getStatus() == ServerState.SHUTDOWN) {
							_start();
						} else if (getCommand() == ServerCommand.STOP) {
							_stop();
						}
						sleep(1000);
					} catch (InterruptedException e) {
						log.catching(e.fillInStackTrace());
					} catch (DisconnectException e) {
						log.info("Disconnected");
						log.catching(Level.DEBUG, e);
						onError();
					} catch (Exception e) {
						log.catching(e);
						onError();
					}
				}

			}

			private void onError() {
				_stop();
				setStatus(ServerState.SHUTDOWN);
				if (restartOnError) {
					setCommand(ServerCommand.START);
				}
			}
		};
		thread.start();
	}

	public void start(int port) {
		setCommand(ServerCommand.START);
		this.port = port;
	}

	public void stop() {
		setCommand(ServerCommand.STOP);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.catching(e);
		}
		thread.interrupt();
		if (getStatus() != ServerState.SHUTDOWN) {
			_stop();
		}
	}

	private void _start() {
		try {
			server = new ServerSocket(port);
			log.info("Waiting for connection on port " + port + "...");
			setStatus(ServerState.WAITING);
			socket = server.accept();
			socket.setReuseAddress(false);
			socket.setKeepAlive(true);
		} catch (IOException e) {
			log.catching(e);
		}
	}

	private void _stop() {
		if (!server.isClosed()) {
			try {
				server.close();
			} catch (IOException e) {
				log.catching(e);
			}
		}
		setStatus(ServerState.SHUTDOWN);
		setCommand(ServerCommand.STOP);
	}

	public void restart() {
		stop();
		start(port);
	}

	private void listen() throws InterruptedException, IOException {
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		while (!socket.isConnected() && inputStream.available() == 0) {
			Thread.sleep(1000);
		}
		log.info("Connected");
		input = new ObjectInputStream(inputStream);
		output = new ObjectOutputStream(outputStream);
		estabilish();
		Response res = null;
		errorCount = 0;
		setStatus(ServerState.CONNECTED);
		while (!socket.isClosed() && getCommand() == ServerCommand.START) {
			try {
				Action a = (Action) input.readObject();
				log.debug(a);
				a.doAction();
				res = new Response(Status.OK);
			} catch (SocketException | EOFException e) {
				Thread.sleep(1000);
				errorCount++;
				log.catching(e);
				isError();
				setStatus(ServerState.CONNECTION_ERROR);
				continue;
			} catch (DisconnectException e) {
				sendResponse(output, new Response(Status.OK));
				throw e;
			} catch (Exception e) {
				log.catching(e);
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
			log.catching(e);
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
				log.catching(e);
				error = true;
				errorCount++;
			}
		}
	}

	public synchronized ServerState getStatus() {
		return status;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private synchronized ServerCommand getCommand() {
		return command;
	}

	private synchronized void setCommand(ServerCommand command) {
		this.command = command;
	}

	private synchronized void setStatus(ServerState state) {
		this.status = state;
	}

	public void setRestartOnError(boolean restartOnError) {
		this.restartOnError = restartOnError;
	}

}
