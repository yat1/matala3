import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

public class Ex3B {
	
	
	/**
	 * gets a number and creates that amount of files which each one has a random amount of lines
	 * each line only has : "Hello World"
	 * @param n
	 * @return an array with a list of names of all the files created
	 */
	public static String[] createFiles(int n) {
		
		String[] list = new String[n];
		String nameI = "File_";
		/**
		 * same random seed so we will the same amount of lines every time
		 */
		Random rnd = new Random(123);
		
		for (int i =1 ; i<n+1; i++) {
			nameI = nameI+i;
			 try {
				/**
				 * opening all the connections to create and right the file
				 */
				File statText = new File(nameI+".txt");
			    FileOutputStream is = new FileOutputStream(statText);
			    OutputStreamWriter osw = new OutputStreamWriter(is);    
			    PrintWriter w = new PrintWriter(osw);
			    /**
			     * writing a random amount of lines to the file
			     */
			    int length = rnd.nextInt(1000);
			    for (int j = 0; j<length; j++) {
			    	w.println("Hello World");	
			    }
			    /**
			     * closing the connections
			     */
			    w.close();
			    osw.close();
			    is.close();
			    /**
			     * adding the name of the file to the list
			     */
			    list[i-1] = nameI;
		        }
			 catch (IOException e) {
				 System.err.println("Problem writing to the file statsTest.txt");
			 }
			 nameI = "File_";
		}
		return list;
	}
	
	/**
	 * gets an array of strings which are names of files and deletes all the files
	 * @param fileNames
	 */
	public static void deleteFiles(String[] fileNames) { 
		for (int i = 0; i < fileNames.length; i++) {
			try {
				File file = new File(fileNames[i]+".txt");
				file.delete();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * creates numFiles amount of files using createFiles function, runs lineCounter as a thread for each one
	 * and prints at the end the amount of lines and how long it took to count the lines
	 * then deletes the files
	 * @param numFiles
	 */
	public static void countLinesThreads(int numFiles) {
		int lineNum = 0;
		String[] fileList = createFiles(numFiles);
		/**
		 * creates an array of threads and creates a new object of lineCounter, then starts all the threads
		 */
		LineCounter[] counters = new LineCounter[numFiles];
		long start = System.currentTimeMillis();
		for (int i = 0; i < fileList.length; i++) {
			counters[i] = new LineCounter(fileList[i]+".txt");
			counters[i].start();
		}
		try {
			for (LineCounter lc : counters) {
				lc.join();
			}
			}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**
		 * after they all finished iterating over the array we make a sum of the total amount of line
		 * getting each files sum with the getNum function from each object of thread
		 */
		for (LineCounter lc : counters) {
			lineNum += lc.getNum();
		}	
		System.out.println("line sum is "+lineNum);
		System.out.println("in regular threads Lapse = " + (System.currentTimeMillis() - start)+ " miliseconds");
		deleteFiles(fileList);
	}
	
	/**
	 * creates numFiles amount of files using createFiles function, using regular 
	 * one process system counts the amount of lines
	 * and prints at the end the amount of lines and how long it took to count the lines
	 * then deletes the files
	 * @param numFiles
	 */
	public static void countLinesOneProcess(int numFiles) {
		int lineNum = 0;
		String[] fileList = createFiles(numFiles);
		long start = System.currentTimeMillis();
		/**
		 * for each file we count the sum one by one and add to the general sum of lines
		 */
		for (int i = 0; i < fileList.length; i++) {
			FileReader fr = null;
			try {
				fr = new FileReader(fileList[i]+".txt");
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			if (fr != null) {
				LineNumberReader lnr = new LineNumberReader(fr);
				try {
					lnr.skip(Long.MAX_VALUE);
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
				lineNum = lineNum + lnr.getLineNumber();
			}
			try {
				fr.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("line sum is "+lineNum);
		System.out.println("in regular one by one no threads Lapse = " + (System.currentTimeMillis() - start)+ " miliseconds");
		deleteFiles(fileList);
		
	}

	/**
	 * creates numFiles amount of files using createFiles function, runs CounterLine as a thread for each one 
	 * using a thread pool, while waiting for the result from each one using a future
	 * and prints at the end the amount of lines and how long it took to count the lines
	 * then deletes the files
	 * @param numFiles
	 */
	public static void countLinesThreadPool(int numFiles) { 
		Integer lineNum = 0;
		String[] fileList = createFiles(numFiles);
		long start = System.currentTimeMillis();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        List<Future<Integer>> resultList = new ArrayList<>();
        for (int i = 0; i < numFiles; i++) {
        	CounterLine count  = new CounterLine(fileList[i]+".txt");
            Future<Integer> result = executor.submit(count);
            resultList.add(result);
        }
        executor.shutdown();
        /**
         * after all the results were added to the list we iterate over it and sum up the total amount of lines
         */
        Iterator<Future<Integer>> resultListIterator = resultList.iterator();
        while(resultListIterator.hasNext()) {
        	try {
        		lineNum = lineNum + resultListIterator.next().get();
        	} 
        	catch (InterruptedException e) {
        		e.printStackTrace();
        	} 
        	catch (ExecutionException e) {
        		e.printStackTrace();
        	}
        }
		System.out.println("line sum is "+lineNum);
		System.out.println("in thread pool Lapse = " + (System.currentTimeMillis() - start)+ " miliseconds");
		deleteFiles(fileList);
	}

	/*public static void main(String[] args) {
		
		countLinesOneProcess(1000);
		
	countLinesThreads(1000);
	
		countLinesThreadPool(1000);
		
	  }*/

}
