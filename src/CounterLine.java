import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.concurrent.Callable;

/**
 * class that implements callable interface inorder to use in a thread pool, the class counts the amount of lines in a file
 * @author yehud
 *
 */
public class CounterLine implements Callable<Integer> {

	private String file;
	private Integer sum;
	
	/**
	 * constructor that gets the name of the file
	 * @param name
	 */
	public CounterLine (String name) {
		super();
		this.file = name;
	}

	/**
	 * opens a connection to the file and checks how many lines there are, the sum is returned to a future that is waiting for it in the thread pool
	 */
	@Override
	public Integer call() throws Exception {
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
			sum = lnr.getLineNumber();
		}
		fr.close();
		return sum;
	}

}
