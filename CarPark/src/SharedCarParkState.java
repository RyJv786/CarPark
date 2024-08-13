public class SharedCarParkState {

	private SharedCarParkState mySharedObj;
	private String myThreadName;
	private int mySharedCarPark;
	private boolean accessing = false; // true a thread has a lock, false otherwise
	private int threadsWaiting = 0; // number of waiting writers
	private int EntQueueA = 0;
	private int EntQueueB = 0;
	private int CarPark_Space = 0;

	SharedCarParkState(int CarPark_Num) {
		mySharedCarPark = CarPark_Num;
	}

	public synchronized void acquireLock() throws InterruptedException {
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName() + " is attempting to acquire a lock!");
		++threadsWaiting;
		while (accessing) { // while someone else is accessing or threadsWaiting > 0
			System.out.println(me.getName() + " waiting to get a lock as someone else is accessing...");
			// wait for the lock to be released - see releaseLock() below
			wait();
		}
		// nobody has got a lock so get one
		--threadsWaiting;
		accessing = true;
		System.out.println(me.getName() + " got a lock!");
	}

	// Releases a lock to when a thread is finished

	public synchronized void releaseLock() {
		// release the lock and tell everyone
		accessing = false;
		notifyAll();
		Thread me = Thread.currentThread(); // get a ref to the current thread
		System.out.println(me.getName() + " released a lock!");
	}

	/* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput) {
		System.out.println(myThreadName + " received " + theInput);
		String theOutput = null;
		// Check what the client said
		if (theInput.equalsIgnoreCase("Add_car")) {
			if (myThreadName.equals("EntAServerThread1")) {

				if (mySharedCarPark < 5 && EntQueueA == 0) {

					++mySharedCarPark;

					theOutput = "A car has been added to the car park";
				}

				else if (mySharedCarPark >= 5) {

					++EntQueueA;

					theOutput = "The car park is full, the queue at entrance A is " + EntQueueA;
				}

				else if (mySharedCarPark < 5 && EntQueueA > 0) {

					--EntQueueA;
					++mySharedCarPark;

					theOutput = "A car has been added car park, the queue at entrance A is " + EntQueueA;
				}

			} else if (myThreadName.equals("EntBServerThread2")) {

				if (mySharedCarPark < 5 && EntQueueB == 0) {

					++mySharedCarPark;

					theOutput = "A car has been added to the car park";
				}

				else if (mySharedCarPark >= 5) {

					++EntQueueB;

					theOutput = "The car park is full, the queue at entrance B is " + EntQueueB;
				}

				else if (mySharedCarPark < 5 && EntQueueB > 0) {

					--EntQueueB;
					++mySharedCarPark;

					theOutput = "A car has been added car park, the queue at entrance B is " + EntQueueB;
				}

				else {
					System.out.println("Error - thread call not recognised.");
				}

			} else if (myThreadName.equals("ExtAServerThread3") || myThreadName.equals("ExtBServerThread4")) {

				theOutput = "Invalid input, please use \"Remove_car\"";
			}
		} else if (theInput.equalsIgnoreCase("Check_space")) {
			if (myThreadName.equals("EntAServerThread1") || myThreadName.equals("EntBServerThread2")) {
				if (mySharedCarPark < 5 && mySharedCarPark > 0) {
					CarPark_Space = 5 - mySharedCarPark;
					theOutput = "There is currently " + CarPark_Space + " space(s) left";

				} else if (mySharedCarPark == 5) {
					theOutput = "The car park has reached its capacity";
				}

				else if (mySharedCarPark == 0) {
					theOutput = "There are no cars in the car park";
				}

			}

			else if (myThreadName.equals("ExtAServerThread3") || myThreadName.equals("ExtBServerThread4")) {

				theOutput = "Invalid input, please use \"Remove_car\"";
			}
		}

		else if (theInput.equalsIgnoreCase("Remove_car")) {
			if (myThreadName.equals("ExtAServerThread3")) {
				if (mySharedCarPark <= 5 && mySharedCarPark > 0) {

					--mySharedCarPark;
					theOutput = "A car has left the car park through exit A, there are " + mySharedCarPark
							+ " car(s) currently in the car park";
				} else if (mySharedCarPark == 0) {
					theOutput = "There are currently no cars in the car park to exit from";
				}
			}

			else if (myThreadName.equals("ExtBServerThread4")) {

				if (mySharedCarPark <= 5 && mySharedCarPark > 0) {

					--mySharedCarPark;
					theOutput = "A car has left the car park through exit B, there are " + mySharedCarPark
							+ " car(s) currently in the car park";
				} else if (mySharedCarPark == 0) {
					theOutput = "There are currently no cars in the car park to exit from";
				}
			}

			else if (myThreadName.equals("EntAServerThread1") || myThreadName.equals("EntBServerThread2")) {

				theOutput = "Invalid input, please use \"Add_car\" or \"Check_space\"";
			}
		}

		else if (!theInput.equalsIgnoreCase("Add_car") || !theInput.equalsIgnoreCase("Check_space")
				|| !theInput.equalsIgnoreCase("Remove_car")) {
			if (myThreadName.equals("EntAServerThread1") || myThreadName.equals("EntBServerThread2")) {

				theOutput = "Invalid input, please use \"Add_car\" or \"Check_space\"";

			} else {
				theOutput = "Invalid input, please use \"Remove_car\"";
			}
		}

		System.out.println(theOutput);
		return theOutput;
	}
}
