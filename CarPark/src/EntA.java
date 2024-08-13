import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EntA {
	public static void main(String[] args) throws IOException {

		Socket ClientSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		int SocketNumber = 4812;
		String CarParkServerName = "localhost";
		String ClientID = "EntA";

		try {
			ClientSocket = new Socket(CarParkServerName, SocketNumber);
			out = new PrintWriter(ClientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost ");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + SocketNumber);
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;
		String fromUser;

		System.out.println("Initialised " + ClientID + " client and IO connections");

		while (true) {

			fromUser = stdIn.readLine();
			if (fromUser != null) {
				System.out.println(ClientID + " sending " + fromUser + " to CarParkServer");
				out.println(fromUser);
			}
			fromServer = in.readLine();
			System.out.println(ClientID + " received " + fromServer + " from CarParkServer");
		}
	}
}
