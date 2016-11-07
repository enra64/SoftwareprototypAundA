/**
 * Created by André on 03.11.2016.
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Logger that creates a file that contains all the information sent by the server
 */
class Logger {
    /**
     * Singleton instance
     */
    private static Logger log = null;

    /**
     * Writer used for saving to disk
     */
    private PrintWriter writer;

    static Logger getLogger(String filename) {
        if (log == null)
            log = new Logger(filename);

        return log;
    }

    private Logger(String fileName) {
        try {
            writer = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes data to the log file
     * @param textLine - String of data that is written to the file
     */
    void write(String textLine) {
        writer.println(textLine);
        writer.flush();
    }

    /**
     * closes the PrintWriter
     */
    void close() {
        writer.close();
    }
}
