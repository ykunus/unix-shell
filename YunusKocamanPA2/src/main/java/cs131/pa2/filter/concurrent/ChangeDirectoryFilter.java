package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;

import java.nio.file.Path;

/**
 * A filter that takes a directory name as a parameter and changes the shell's working directory
 * to that directory. This filter cannot have input or output.
 * 
 * Author: yunuskocaman@brandeis.edu
 */
public class ChangeDirectoryFilter extends ConcurrentFilter {

    private String directory;

    /**
     * Constructs the filter to change the shell's working directory to the given directory.
     * 
     * @param directory the relative path of the destination directory
     * @throws IllegalArgumentException if the directory does not exist
     */
    public ChangeDirectoryFilter(String directory) {
        // Throw error in constructor
        Path newPath = Path.of(ConcurrentREPL.currentWorkingDirectory).resolve(directory);
        if (!newPath.toFile().isDirectory()) {
            throw new IllegalArgumentException(Message.DIRECTORY_NOT_FOUND.with_parameter("cd " + directory));
        }
        this.directory = directory;
    }

    /**
     * Sets the shell's working directory to the given directory.
     * 
     * @throws IllegalArgumentException if the directory specified in construction does not exist
     */
    @Override
    public void process() throws IllegalArgumentException {
        Path newPath = Path.of(ConcurrentREPL.currentWorkingDirectory).resolve(directory);
        if (!newPath.toFile().isDirectory()) {
            throw new IllegalArgumentException(Message.DIRECTORY_NOT_FOUND.with_parameter("cd " + directory));
        }
        ConcurrentREPL.currentWorkingDirectory = newPath.normalize().toString();
        poisonStatus = true;
        Thread.currentThread().interrupt();
    }

    /**
     * Processes a line from the input. This method returns null since the ChangeDirectoryFilter 
     * does not process input lines.
     * 
     * @param line the input line to process
     * @return null
     */
    @Override
    protected String processLine(String line) {
        return null;
    }

    /**
     * Sets the previous filter in the chain. This method throws an exception 
     * because the ChangeDirectoryFilter cannot have input.
     * 
     * @param prevFilter the previous filter
     * @throws IllegalArgumentException if the previous filter is not null since
     *                                  ChangeDirectoryFilter cannot have input
     */
    @Override
    public void setPrevFilter(Filter prevFilter) throws IllegalArgumentException {
        if (prevFilter != null) {
            throw new IllegalArgumentException(Message.CANNOT_HAVE_INPUT.with_parameter("cd " + directory));
        }
    }

    /**
     * Sets the next filter in the chain. This method throws an exception 
     * because the ChangeDirectoryFilter cannot have output.
     * 
     * @param nextFilter the next filter
     * @throws IllegalArgumentException if the next filter is not null since
     *                                  ChangeDirectoryFilter cannot have output
     */
    @Override
    public void setNextFilter(Filter nextFilter) throws IllegalArgumentException {
        if (nextFilter != null) {
            throw new IllegalArgumentException(Message.CANNOT_HAVE_OUTPUT.with_parameter("cd " + directory));    
        }
    }
}
