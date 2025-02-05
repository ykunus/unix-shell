package cs131.pa2.filter.concurrent;

import java.util.HashSet;

/**
 * A filter that only outputs unique lines of input. This filter requires input.
 * 
 * @author cs131a
 *
 */
public class UniqFilter extends ConcurrentFilter {
	
	private HashSet<String> prevLines;
	
	public UniqFilter() {
		prevLines = new HashSet<>();
	}
	
	/**
	 * Returns null if the given line has already been output by this filter.
	 */
	@Override
	protected String processLine(String line) {
		if (prevLines.contains(line)) {
			return null;
		}
		prevLines.add(line);
		return line;
	}

}
