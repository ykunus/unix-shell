package cs131.pa2.filter.concurrent;

import cs131.pa2.filter.Message;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main implementation of the REPL (read-eval-print-loop). It reads
 * commands from the user, parses them, executes them, and displays the result.
 * 
 * Author: yunuskocaman@brandeis.edu
 */
public class ConcurrentREPL {
    
    /**
     * The path of the current working directory.
     */
    public static String currentWorkingDirectory;
    public static final String PATH_SEPARATOR = System.getProperty("file.separator");

    /**
     * Stores each running background command into this HashMap. 
     * Key is the command, value is the threads.
     */
    public static HashMap<String, List<Thread>> backgroundCommands = new HashMap<>();
    
    /**
     * Stores each running background command into this HashMap. 
     * Key is the command, value is the threads. 
     */
    public static ArrayList<String> commandsOrder = new ArrayList<>();
    
    /**
     * Number of background commands running.
     */
    public static int backgroundCount = 0;

    /**
     * The main method that will execute the REPL loop.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        currentWorkingDirectory = System.getProperty("user.dir");        
        Scanner consoleReader = new Scanner(System.in);
        System.out.print(Message.WELCOME);
    
        while (true) {
            Boolean background = false;
            System.out.print(Message.NEWCOMMAND);
            
            // Read user command. If it's just whitespace, skip to the next command
            String userInput = consoleReader.nextLine().trim();
            
            if (userInput.isEmpty()) {
                continue;
            }
            // Exit the REPL if the command is "exit".
            if (userInput.equals("exit")) {
                backgroundCommands.clear();
                commandsOrder.clear();
                backgroundCount = 0;
                break;
            }
            if (userInput.equals("repl_jobs")) {
                synchronized(commandsOrder) {
                    if (backgroundCount > 0) {
                        printJobs();
                    }
                }
                continue;
            }
            // Checks if input = kill
            if (userInput.length() >= 4 && userInput.substring(0, 4).equals("kill")) {
                int num = Character.getNumericValue(userInput.charAt(userInput.length() - 1)); // turns char digit into integer
                if (num >= 0 && num <= backgroundCount) {
                    kill(num);
                }
                continue;
            }
            // Check if it's a background command
            if (userInput.charAt(userInput.length() - 1) == '&') {
                background = true;
                userInput = userInput.replace("&", "");
            }
            
            List<ConcurrentFilter> filters = ConcurrentCommandBuilder.createFiltersFromCommand(userInput);
            
            ArrayList<Thread> threads = new ArrayList<>();
            int count = 1; // count to track thread number
            if (filters != null) {
                try {
                    for (ConcurrentFilter filter : filters) {
                        Thread thread = new Thread(filter, "thread " + count);
                        threads.add(thread);
                        thread.start(); 
                        count++;
                    } 
                    if (!background) {
                        threads.get(threads.size() - 1).join();
                    }
                } catch (Exception e) {
                    // One of the filters threw an exception. Print error and loop to wait for a new command.
                    System.out.print(e.getMessage());
                }
                // Add background task to List of background commands 
                if (background) {
                    Boolean duplicate = false; 
                    // checks for duplicate 
                    if (backgroundCommands.containsKey(userInput)) {
                        duplicate = true; 
                        continue;
                    }

                    if (!duplicate) {
                        backgroundCount++; 
                        backgroundCommands.put(userInput, threads);
                        commandsOrder.add(userInput);
                    }
                }
            }
        }
        System.out.print(Message.GOODBYE);
        consoleReader.close();
    }

    /**
     * Prints the currently running background jobs to the console.
     */
    public static void printJobs() { 
        StringBuilder sb = new StringBuilder(); 
        for (int i = 0; i < commandsOrder.size(); i++) {
            if (commandsOrder.get(i) != null) {
                sb.append("\t");
                sb.append(i + 1 + ". " + commandsOrder.get(i) + "&" + "\n");
            }
        }
        String result = sb.toString();
        System.out.print(result); 
    }
    
    /**
     * Terminates a background job specified by its job number.
     * 
     * @param num The job number of the command to be killed.
     */
    public static void kill(int num) { 
        String victim = commandsOrder.get(num - 1); // command that is going to be killed
        if (backgroundCommands.containsKey(victim)) { 
            List<Thread> tmp = backgroundCommands.get(victim);
            for (Thread thread : tmp) {
                if (thread.isAlive()) {
                    thread.interrupt();
                }
            }
            backgroundCommands.remove(victim, tmp);
            backgroundCount--;
            commandsOrder.set(num - 1, null);
        }
    }
}
