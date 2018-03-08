package Helper;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class TimeHelper {

    // Returns time in the following manner:
    // 2 days, 12 hours, 5 minutes, 45 seconds
    public static String GetTimeToWaitLongIntervals(DateTime firstDate, DateTime secondDate, boolean showIfZero)
    {
        Period period = new Period(firstDate, secondDate);
        String result = "";

        if(showIfZero || period.getDays() > 0)
            result += period.getDays() + (period.getDays() == 1 ? " day, " : " days, ");
        if(showIfZero || period.getHours() > 0)
            result += period.getHours() + (period.getHours() == 1 ? " hour, " : " hours, ");
        if(showIfZero || period.getMinutes() > 0)
            result += period.getMinutes() + (period.getMinutes() == 1 ? " minute, " : " minutes, ");

        // Always show seconds regardless if showIfZero == false. This is due to milliseconds.
        result += period.getSeconds() + (period.getSeconds() == 1 ? " second" : " seconds");

        return result;
    }

    // Returns time in the following manner:
    // 2D, 12H, 5M, 45S
    public static String GetTimeToWaitShortIntervals(DateTime firstDate, DateTime secondDate, boolean showIfZero)
    {
        Period period = new Period(firstDate, secondDate);
        String result = "";

        if(showIfZero || period.getDays() > 0)
            result += period.getDays() + "D, ";

        if(showIfZero || period.getHours() > 0)
            result += period.getHours() + "H, ";

        if(showIfZero || period.getMinutes() > 0)
            result += period.getMinutes() + "M, ";

        // Always show seconds regardless if showIfZero == false. This is due to milliseconds.
        result += period.getSeconds() + "S";

        return result;
    }
}
