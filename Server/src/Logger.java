/**
 * Created by André on 03.11.2016.
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Logger that creates a log.txt file that contains all the information sent by the server
 *
 * @author PC
 */

public class Logger {

    private static Logger log = null;

    private PrintWriter writer;
    private final String fileName = "log.txt";

    public static Logger getLogger()
    {

        if(log == null)
            log = new Logger();

        return log;
    }


    private Logger()
    {
        try
        {
            writer = new PrintWriter(fileName);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Writes data to the log file
     * <\br>
     * @param textLine - String of data that is written to the file
     */
    public void write(String textLine)
    {
        writer.println(textLine);
        writer.flush();
    }

    /**
     * closes the PrintWriter
     */
    public void close()
    {
        writer.close();
    }
}
