package cs131.pa2.filter.concurrent;

import java.io.PrintStream;
import java.io.FileNotFoundException;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;

/**
 * A filter that writes each line of input to a file. If the given file does not exist, the 
 * file will be created. This filter requires input and cannot have output.
 * 
 * Author: yunuskocaman@brandeis.edu
 *
 */
public class RedirectFilter extends ConcurrentFilter {
	
	private String fileName;
	
	/**
	 * Constructs a RedirectFilter to write input lines to the specified file.
	 * 
	 * @param fileName the name of the file where input lines will be written
	 */
	public RedirectFilter(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Writes each line of input to the file specified in construction.
	 */
	@Override
	public void process() {
		try {
			PrintStream outputFile = new PrintStream(ConcurrentREPL.currentWorkingDirectory + ConcurrentREPL.PATH_SEPARATOR + fileName);
			while(!Thread.currentThread().isInterrupted()) {
				String line = input.take();
				if(line.equals(POISON)) {
					outputFile.close();
					poisonStatus = true;
					Thread.currentThread().interrupt();
				}else {
					outputFile.println(line);
				}
			}
		} catch(FileNotFoundException e) {
			// Handle the exception as needed.
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}
	
	/**
	 * @throws IllegalArgumentException if the next filter is not null since
	 *                                  RedirectFilter cannot have output.
	 */
	@Override
	public void setNextFilter(Filter nextFilter) throws IllegalArgumentException {
		if (nextFilter != null) {
			throw new IllegalArgumentException(Message.CANNOT_HAVE_OUTPUT.with_parameter("> " + fileName));
		}
	}
}
