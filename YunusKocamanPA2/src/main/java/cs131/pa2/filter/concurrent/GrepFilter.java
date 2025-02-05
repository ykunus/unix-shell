package cs131.pa2.filter.concurrent;

/**
 * A filter that outputs all lines of input that contain a given String query.
 * This filter requires input.
 * 
 * @author cs131a
 *
 */
public class GrepFilter extends ConcurrentFilter {

	private String query;

	/**
	 * Constructs the filter with the given query.
	 * 
	 * @param query the String that will be searched for in each line of input.
	 */
	public GrepFilter(String query) {
		this.query = query;
	}

	/**
	 * Returns the given line if it contains the String query specified in
	 * construction.
	 */
	@Override
	public String processLine(String line) {
		
		if (line.contains(query)) {
			return line;
		}
		return null;
	}

	

}
