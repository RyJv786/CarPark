import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CarParkServerThread extends Thread {

	private Socket carparkSocket = null;
	private String myCarParkServerThreadName;
	private SharedCarParkState mySharedCarParkStateObject;
	private int mySharedCarPark;

	public CarParkServerThread(Socket CarParkSocket, String CarParkServerThreadName, SharedCarParkState SharedObject) {

		this.carparkSocket = CarParkSocket;
		mySharedCarParkStateObject = SharedObject;
		myCarParkServerThreadName = CarParkServerThreadName;
	}

	public void run() {
		try {
			System.out.println(myCarParkServerThreadName + "initialising.");
			PrintWriter out = new PrintWriter(carparkSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(carparkSocket.getInputStream()));
			String inputLine, outputLine;

			while ((inputLine = in.readLine()) != null) {
				// Get a lock first
				try {
					mySharedCarParkStateObject.acquireLock();
					outputLine = mySharedCarParkStateObject.processInput(myCarParkServerThreadName, inputLine);
					out.println(outputLine);
					mySharedCarParkStateObject.releaseLock();
				} catch (InterruptedException e) {
					System.err.println("Failed to get lock when reading:" + e);
				}
			}
			out.close();
			in.close();
			carparkSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
