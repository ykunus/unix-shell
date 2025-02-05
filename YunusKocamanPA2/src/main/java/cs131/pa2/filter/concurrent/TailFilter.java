package cs131.pa2.filter.concurrent;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A filter that outputs the last n lines of its input, where n is specified in
 * construction. This filter requires input.
 * 
 * Author: yunuskocaman@brandeis.edu
 *
 */
public class TailFilter extends ConcurrentFilter {
	
	private int numLines;
	private Queue<String> lastNumLines;

	/**
	 * Constructs a filter to return the last given number of lines of input.
	 * 
	 * @param numLines the number of lines that the filter will output
	 */
	public TailFilter(String numLines) {
		this.numLines = Integer.valueOf(numLines);
		this.lastNumLines = new LinkedList<>();
	}
	
	@Override 
	public void process() {
		try { 
			while(!Thread.currentThread().isInterrupted()) {
				String line = input.take();
				if (line.equals(POISON)){
					poisonStatus = true;
					// Output the last n lines before sending the poison pill
					for(String lastLines : lastNumLines) {
						output.put(lastLines);
					}
					output.put(line); // Send the poison pill
					Thread.currentThread().interrupt();
				} else { 
					processLine(line); // Process the line
				}
			}
		} catch(InterruptedException e) {
	        Thread.currentThread().interrupt();
		}
	}

	/**
	 * Returns the given line if it is within the last n lines of input, where
	 * n is the number of lines passed as a parameter to the constructor.
	 */
	@Override
	public String processLine(String line) {
		if (lastNumLines.size() == numLines) {
			lastNumLines.poll(); // Remove the oldest line if limit reached
		}
		lastNumLines.add(line); // Add the current line
		return null; // No output from this filter
	}
}
