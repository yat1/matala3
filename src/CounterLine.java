import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.concurrent.Callable;

public class CounterLine implements Callable<Integer> {

	private String file;
	private Integer sum;
	
	public CounterLine (String name) {
		super();
		this.file = name;
	}

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
		return sum;
	}

}
