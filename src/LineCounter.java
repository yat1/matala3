import java.io.FileReader;
import java.io.LineNumberReader;

/**
 * a class that will run as a thread for each file and count how many lines it has
 * @author yehud
 *
 */
public class LineCounter extends Thread {	
	
	private String file;
	private int num;
	
	/**
	 * a constructor that gets the name of the file
	 * @param name
	 */
	public LineCounter (String name) {
		super();
		this.file = name;
	}
	
	/**
	 * opens a connection to the file and checks how many lines there are
	 */
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
			/**
			 * the result from each thread is saved in a variable in the thread then we will get it when all the threads finish
			 */
			num = lnr.getLineNumber();
			
		}
	}
	
	/**
	 * the number of lines the thread has counted for a specific file
	 * @return
	 */
	public int getNum() {
		return num;
	}

}


