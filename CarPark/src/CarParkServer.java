import java.io.IOException;
import java.net.ServerSocket;

public class CarParkServer {
	public static void main(String[] args) throws IOException {

		ServerSocket CarParkServerSocket = null;
		boolean listening = true;
		String CarParkServerName = "CarParkServer";
		int CarParkServerNumber = 4812;

		// Number of cars in the car park
		int CarPark_Num = 0;

		SharedCarParkState ourSharedCarParkStateObject = new SharedCarParkState(CarPark_Num);

		try {
			CarParkServerSocket = new ServerSocket(CarParkServerNumber);
		} catch (IOException e) {
			System.err.println("Could not start " + CarParkServerName + " specified port.");
			System.exit(-1);
		}
		System.out.println(CarParkServerName + " started");

		while (listening) {
			new CarParkServerThread(CarParkServerSocket.accept(), "EntAServerThread1", ourSharedCarParkStateObject)
					.start();
			new CarParkServerThread(CarParkServerSocket.accept(), "EntBServerThread2", ourSharedCarParkStateObject)
					.start();
			new CarParkServerThread(CarParkServerSocket.accept(), "ExtAServerThread3", ourSharedCarParkStateObject)
					.start();
			new CarParkServerThread(CarParkServerSocket.accept(), "ExtBServerThread4", ourSharedCarParkStateObject)
					.start();
			System.out.println("New " + CarParkServerName + " thread started.");
		}
	}
}
