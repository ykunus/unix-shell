package cs131.pa2.filter.concurrent;

/**
 * A filter that outputs the first n lines of its input, where n is specified in
 * construction. This filter requires input.
 * 
 * @author cs131a
 *
 */
public class HeadFilter extends ConcurrentFilter {

	private int lineCount;
	private int numLines;

	/**
	 * Constructs a filter to return the first given number of lines of input.
	 * 
	 * @param numLines the number of lines that the filter will output
	 */
	public HeadFilter(String numLines) {
		this.numLines = Integer.valueOf(numLines);
	}

	/**
	 * Returns the given line if it is within the first n lines of input, where n is
	 * the number of lines passed as a parameter to the constructor.
	 */
	@Override
	public String processLine(String line) {
		if (++lineCount > numLines) {
			return null;
		}
		return line;
	}

	
}
