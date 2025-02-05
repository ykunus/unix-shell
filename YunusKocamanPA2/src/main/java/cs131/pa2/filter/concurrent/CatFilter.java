package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Filter;
import cs131.pa2.filter.Message;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A filter that takes a file name as a parameter and outputs the lines of that
 * file. This filter cannot have input.
 * 
 * Author: yunuskocaman@brandeis.edu
 */
public class CatFilter extends ConcurrentFilter {

    private String fileName;

    /**
     * Constructs a filter to read and output the lines of the given file.
     * 
     * @param fileName the name of the file that will be read
     */
    public CatFilter(String fileName) {
        File f = new File(ConcurrentREPL.currentWorkingDirectory + ConcurrentREPL.PATH_SEPARATOR + fileName);
        if (!f.exists()) {
            throw new IllegalArgumentException(Message.FILE_NOT_FOUND.with_parameter("cat " + fileName));
        }
        this.fileName = fileName;
    }

    /**
     * Reads the lines of the file specified in construction and adds them to
     * output. This method will continue reading until the thread is interrupted.
     * 
     * @throws IllegalArgumentException if the file does not exist
     */
    @Override
    public void process() throws IllegalArgumentException {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Scanner fileReader = new Scanner(
                        new File(ConcurrentREPL.currentWorkingDirectory + ConcurrentREPL.PATH_SEPARATOR + fileName));
                // Read lines from file and add them to output.
                while (fileReader.hasNextLine()) {
                    try {
                        output.put(fileReader.nextLine());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                try {
                    output.put(POISON);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                poisonStatus = true;
                Thread.currentThread().interrupt();
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(Message.FILE_NOT_FOUND.with_parameter("cat " + fileName));
        }
    }

    /**
     * Processes a line from the input. This method returns null since the CatFilter 
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
     * because the CatFilter cannot have input.
     * 
     * @param prevFilter the previous filter
     * @throws IllegalArgumentException if the previous filter is not null since
     *                                  CatFilter cannot have input
     */
    @Override
    public void setPrevFilter(Filter prevFilter) throws IllegalArgumentException {
        if (prevFilter != null) {
            throw new IllegalArgumentException(Message.CANNOT_HAVE_INPUT.with_parameter("cat " + fileName));
        }
    }
}
