package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import common.actions.TextAction;

public class Client {

	public static void main(String... args) throws IOException, InterruptedException, ClassNotFoundException {
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("localhost", 5555));
		while (!socket.isConnected()) {
			Thread.sleep(1000);
		}
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		TextAction obj = new TextAction();
		obj.setText("dupa hahah 123");
		out.writeObject(obj);
		out.flush();
		System.out.println("ok");

		Response status = (Response) in.readObject();
		System.out.println(status);
		Thread.sleep(200);
		socket.close();
	}
}
