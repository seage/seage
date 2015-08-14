package org.seage.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class LogFormatter extends Formatter
{

    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(LogRecord record)
    {
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        //String className = record.getSourceClassName();
        //className = className.length()>50 ? className.substring(0, 46)+"_" : className;
        //String methodName = className+"."+record.getSourceMethodName();        
        //methodName = methodName.length()>60 ? methodName.substring(0, 59) : methodName;
        Date date = new Date(record.getMillis());
        sb.append(String.format("%-9s", "[" + record.getLevel().getLocalizedName() + "]"))
                .append("  ")
                .append(sdf.format(date))
                //.append("  ")
                //.append(String.format("%-60s",methodName))
                .append("  ")
                .append(formatMessage(record))
                .append(LINE_SEPARATOR);

        if (record.getThrown() != null)
        {
            try
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            }
            catch (Exception ex)
            {
                // ignore
            }
        }

        return sb.toString();
    }
}
