package cs131.pa2.filter.concurrent;

/**
 * A filter that outputs the number of lines, words, and characters that are
 * inputted. This filter requires input.
 * 
 * Author: yunuskocaman@brandeis.edu
 *
 */
public class WordCountFilter extends ConcurrentFilter {

	private int lineCount;
	private int wordCount;
	private int charCount;

	/**
	 * Counts the number of lines, words, and characters from input and outputs
	 * those counts.
	 */
	@Override
	public void process() {
		try { 
			while(!Thread.currentThread().isInterrupted()) {
				String line = input.take();
				if (line.equals(POISON)){
					try {
						output.put(lineCount + " " + wordCount + " " + charCount); // Output counts
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					if (this.output != null) {
						output.put(line); // Send the poison pill
					}
					poisonStatus = true;
					Thread.currentThread().interrupt();
				} else { 
					processLine(line); // Process the line to update counts
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Increases the counts based on the input line and returns null.
	 */
	@Override
	protected String processLine(String line) {
		lineCount++; // Count the line
		wordCount += line.split("\\s+").length; // Count the words by splitting on whitespace
		charCount += line.length(); // Count the characters
		return null; // No output from this filter
	}

}
