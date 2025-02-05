package cs131.pa2.filter.concurrent;

import java.util.List;

import cs131.pa2.filter.Message;

import java.util.ArrayList;

/**
 * This class manages the parsing and execution of a command. It splits the raw
 * input into separated subcommands, creates subcommand filters, and links them
 * into a list.
 * 
 * @author cs131a
 *
 */
public class ConcurrentCommandBuilder {
	
	private static final String PIPE = "|";
	private static final String REDIRECT = ">";
	
	/**
	 * Creates and returns a list of filters from the specified command
	 * 
	 * @param command the command to create filters from
	 * @return the list of ConcurrentFilter that represent the specified command
	 */
	public static List<ConcurrentFilter> createFiltersFromCommand(String command) {
		command = command.trim();
		// This makes it very easy to deal with redirect filter since there is now a pipe in front of it.
		command = command.replace(REDIRECT, PIPE + REDIRECT);
		// Get subcommands by splitting command around the pipes.
		String[] subcommands = command.split("\\s*\\" + PIPE + "\\s*");
		List<ConcurrentFilter> filters = new ArrayList<ConcurrentFilter>();	
		try {
			for (String subcommand : subcommands) {
				// If there is a pipe at the beginning of the command, then the first subcommmands[0] will
				// be blank. This will occur if the command begins with ">" due to the replacement above.
				if (!subcommand.isBlank()) {
					filters.add(constructFilterFromSubCommand(subcommand));
					if(filters.size() == 1) {
						ConcurrentFilter first = filters.get(0);
						if(first instanceof HeadFilter || first instanceof TailFilter 
								|| first instanceof GrepFilter || first instanceof WordCountFilter
								|| first instanceof UniqFilter || first instanceof RedirectFilter) {
							throw new IllegalArgumentException(Message.REQUIRES_INPUT.with_parameter(subcommand));
						}
					}
				}
			}	
		} catch (Exception e ){
			// A filter could not be created for one of the subcommands. 
			// Print the error message and return null.
			System.out.print(e.getMessage());
			return null;
		}	
		// Add print filter to end pipeline if the pipeline does not end in ChangeDirectoryFilter or RedirectFilter.
		ConcurrentFilter last = filters.get(filters.size()-1);
		if(!(last instanceof ChangeDirectoryFilter || last instanceof RedirectFilter)) { 
			filters.add(new PrintFilter());
		}
		if(linkFilters(filters)) {
			return filters;
		}
		return null;
	}
	
	/**
	 * Creates a single filter from the specified subcommand
	 * 
	 * @param subcommand the command to create a filter from
	 * @return the ConcurrentFilter created from the given subcommand
	 * @throws IllegalArgumentException if a filter cannot be created from the given subcommand
	 */
	private static ConcurrentFilter constructFilterFromSubCommand(String subcommand) throws Exception {
		// Split the command into tokens separated by whitespace.
		String[] tokens = subcommand.split("\\s+");
		ConcurrentFilter filter = null;;
		switch(tokens[0]) {
			case "pwd":
				filter = new PrintWorkingDirectoryFilter();
				break;
			case "ls":
				filter = new ListFilesFilter();
				break;
			case "wc":
				filter = new WordCountFilter();
				break;
			case "uniq":
				filter = new UniqFilter();
				break;
			case "head":
				filter = tokens.length > 1 ? new HeadFilter(tokens[1]) : null;
				break;
			case "tail":
				filter = tokens.length > 1 ? new TailFilter(tokens[1]) : null;
				break;
			case "cd":
				filter = tokens.length > 1 ? new ChangeDirectoryFilter(tokens[1]) : null;
				break;
			case "cat":
				filter = tokens.length > 1 ? new CatFilter(tokens[1]) : null;
				break;
			case "grep":
				filter = tokens.length > 1 ? new GrepFilter(tokens[1]) : null;
				break;
			case ">":
				filter = tokens.length > 1 ? new RedirectFilter(tokens[1]) : null;
				break;
			default:
				// Subcommand does not match any filters.
				throw new IllegalArgumentException(Message.COMMAND_NOT_FOUND.with_parameter(subcommand));
		}
		// If filter was not able to be constructed, only possibility is that there was no parameter when one was required.
		if (filter == null) {
			throw new IllegalArgumentException(Message.REQUIRES_PARAMETER.with_parameter(subcommand));
		}
		return filter;
	}

	/**
	 * links the given filters with the order they appear in the list
	 * 
	 * @param filters the given filters to link
	 * @return true if the link was successful, false if there were errors
	 *         encountered. Any error should be displayed by using the Message enum.
	 */
	private static boolean linkFilters(List<ConcurrentFilter> filters) {
		try {
			for (int i = 1; i < filters.size(); i++) {
				filters.get(i).setPrevFilter(filters.get(i-1));
			}
		} catch (Exception e) {
			// If setPrevFilter() results in an exception, then the pipeline is invalid 
			// due to incorrect inputs or outputs.
			System.out.print(e.getMessage());
			return false;
		}
		return true;
	}
}
