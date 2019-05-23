import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

public class Ex3B {
	
	public static int result = 0;
	public static String[] createFiles(int n) {
		
		String[] list = new String[n];
		String nameI = "File_";
		Random rnd = new Random(123);
		
		for (int i =1 ; i<n+1; i++) {
			nameI = nameI+i;
			 try {
		            //Whatever the file path is.
		            File statText = new File("C:/Users/yehud/eclipse-workspace/muncheAtzamin3/src/"+nameI+".txt");
		            FileOutputStream is = new FileOutputStream(statText);
		            OutputStreamWriter osw = new OutputStreamWriter(is);    
		            PrintWriter w = new PrintWriter(osw);
		            int length = rnd.nextInt(1000);
		            //System.out.println(length);
		            for (int j = 0; j<length; j++) {
		            	 w.println("Hello World");	
		            }
		            w.close();
		            list[i-1] = nameI;
		        } catch (IOException e) {
		            System.err.println("Problem writing to the file statsTest.txt");
		        }
			 nameI = "File_";
		}
		return list;
	}
	
	public static void deleteFiles(String[] fileNames) { 
		for (int i = 0; i < fileNames.length; i++) {
			try {
				File file = new File("C:/Users/yehud/eclipse-workspace/muncheAtzamin3/src/"+fileNames[i]+".txt");
				file.delete();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void countLinesThreads(int numFiles) {
		//int lineNum = 0;
		String[] fileList = createFiles(numFiles);
		long start = System.currentTimeMillis();
		for (int i = 0; i < fileList.length; i++) {
			Thread t = new Thread(new LineCounter("C:/Users/yehud/eclipse-workspace/muncheAtzamin3/src/"+fileList[i]+".txt"));
			t.start();
			try {
				t.join();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//lineNum = lineNum+t.getSum();
		}
		System.out.println("line sum is "+result);
		System.out.println("in regular threads Lapse = " + (System.currentTimeMillis() - start)+ " miliseconds");
		deleteFiles(fileList);
	}
	
	public static void countLinesOneProcess(int numFiles) {
		int lineNum = 0;
		String[] fileList = createFiles(numFiles);
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < fileList.length; i++) {
			FileReader fr = null;
			try {
				fr = new FileReader("C:/Users/yehud/eclipse-workspace/muncheAtzamin3/src/"+fileList[i]+".txt");
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
				lineNum = lineNum + lnr.getLineNumber();
			}
			
		}
			
		System.out.println("line sum is "+lineNum);
		System.out.println("in regular one by one no threads Lapse = " + (System.currentTimeMillis() - start)+ " miliseconds");
		deleteFiles(fileList);
		
	}

	public static void countLinesThreadPool(int numFiles) { 
		Integer lineNum = 0;
		String[] fileList = createFiles(numFiles);
		long start = System.currentTimeMillis();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        List<Future<Integer>> resultList = new ArrayList<>();
        for (int i = 0; i < numFiles; i++) {
        	CounterLine count  = new CounterLine("C:/Users/yehud/eclipse-workspace/muncheAtzamin3/src/"+fileList[i]+".txt");
            Future<Integer> result = executor.submit(count);
            resultList.add(result);
        }
        executor.shutdown();
        Iterator<Future<Integer>> resultListIterator = resultList.iterator();
        while(resultListIterator.hasNext()) {
           try {
			lineNum = lineNum + resultListIterator.next().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
         }
		System.out.println("line sum is "+lineNum);
		System.out.println("in thread pool Lapse = " + (System.currentTimeMillis() - start)+ " miliseconds");
		deleteFiles(fileList);
	}

	public static void main(String[] args) {
		countLinesThreads(100);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countLinesOneProcess(100);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countLinesThreadPool(100);
		
	  }

}
