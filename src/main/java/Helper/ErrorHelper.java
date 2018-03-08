package Helper;


import org.nwnx.nwnx2.jvm.NWScript;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHelper {
    public static void HandleException(Exception ex, String customMessage)
    {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();

        System.out.println(customMessage);
        System.out.println("Exception: ");
        System.out.println(exceptionAsString);

        NWScript.writeTimestampedLogEntry(customMessage);
        NWScript.writeTimestampedLogEntry("Exception:");
        NWScript.writeTimestampedLogEntry(exceptionAsString);
    }
}
