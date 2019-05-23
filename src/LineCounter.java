import java.io.FileReader;
import java.io.LineNumberReader;

//public class LineCounter extends Thread {
public class LineCounter implements Runnable {	
	
	private String file;
	private int sum;
	
	public LineCounter (String name) {
		super();
		this.file = name;
	}
	
	public void run(){
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fr != null) {
			LineNumberReader lnr = new LineNumberReader(fr);
			try {
				lnr.skip(Long.MAX_VALUE);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//System.out.println(file + " " + lnr.getLineNumber());
			//sum = lnr.getLineNumber();
			Ex3B.result = Ex3B.result + lnr.getLineNumber();
			
			
		}
	}
	
	public int getSum() {
		return sum;
	}

}


