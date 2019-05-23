

public class Ex3A {
	private boolean ans = false;

	public boolean isPrime(long n, double maxTime) throws RuntimeException {
		final long num = n;
		
		Thread startisPrime = new Thread() {
			@Override
			public void run() {
				ans = Ex3A_tester.isPrime(num);
			}
		};
		
		startisPrime.start();
		
		try {
			Thread.sleep((long)maxTime*1000);
			
			if (startisPrime.isAlive()) {
				startisPrime.interrupt();
				
				throw new RuntimeException("It has been more than " + maxTime + " milliseconds!");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ans;
		
	}
	
	
}
