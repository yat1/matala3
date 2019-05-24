
/**
 * a class that checks if a number is prime or not
 * @author yehud
 *
 */
public class Ex3A {
	private boolean ans = false;
/**
 * gets a number to check if its prime and a max time to do so in
 * @param n the number were checking if its prime
 * @param maxTime the max amount of time we have to check if we pass the time the function throws an exception
 * @return true if yes prime or false if not
 * @throws RuntimeException if passed the max time 
 */
	public boolean isPrime(long n, double maxTime) throws RuntimeException {
		final long num = n;
		
		/**
		 * creates a thread which runs the function from Ex3A_tester, to check if the number is prime
		 */
		Thread startisPrime = new Thread() {
			@Override
			public void run() {
				ans = Ex3A_tester.isPrime(num);
			}
		};
		
		startisPrime.start();
		/**
		 * check if the thresd is still alive after the max time, if yes closes the thread and throws exception
		 */
		try {
			Thread.sleep((long)maxTime*1000);
			
			if (startisPrime.isAlive()) {
				startisPrime.interrupt();
				
				throw new RuntimeException("It has been more than " + maxTime + " milliseconds!");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**
		 * if the thread was not alive meaning the function managed to calculate if the number is prime under the time 
		 * limit so we return the answer
		 */
		return ans;
		
	}
	
	
}
