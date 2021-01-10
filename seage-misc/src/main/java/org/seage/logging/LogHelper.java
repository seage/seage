/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.logging;

import java.io.InputStream;

import org.slf4j.Logger;
//import org.apache.logging.log4j.core.Appender;
//import org.apache.logging.log4j.core.Layout;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.appender.FileAppender;
//import org.apache.logging.log4j.core.config.AppenderRef;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.apache.logging.log4j.core.layout.PatternLayout;
//import org.apache.logging.log4j.Level;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * 
 * @author Richard Malek
 */
public class LogHelper {

  public static void createFileAppenderConfig(Logger logger) {
//        final LoggerContext ctx = (LoggerContext) LogManager.getContext(true);
//        final Configuration config = ctx.getConfiguration();
//        Layout layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN,null, config, null,
//                null,false, false, "", "");
//        Appender appender = FileAppender.createAppender("logs/test2.log", "true", "false", "File", "false",
//                "false", "true", "4000", layout, null, "false", null, config);
//            
//        appender.start();
//        config.addAppender(appender);
//        AppenderRef ref = AppenderRef.createAppenderRef("File", Level.ALL, null);
//        AppenderRef[] refs = new AppenderRef[] {ref};
//        LoggerConfig loggerConfig = LoggerConfig.createLogger("true", Level.ALL, "org.apache.logging.log4j",
//            "true", refs, null, config, null );
//        loggerConfig.addAppender(appender, Level.ALL, null);
//        config.addLogger("org.apache.logging.log4j", loggerConfig);
//        ctx.updateLoggers();
  }

  public static void loadConfig(String path) {
//        try
//        {
//            FileInputStream configFile = new FileInputStream(path);
//            LogManager..getLogManager().readConfiguration(configFile);
//            configFile.close();
//
//        }
//        catch (IOException ex)
//        {
//            System.err.println("WARNING: Could not open configuration file");
//            System.err.println("WARNING: Logging not configured (console output only)");
//
//            setConsoleDefaultFormatter(LoggerFactory.getLogger("org.seage"), Level.ALL);
//        }
  }

  public static void configure(InputStream config) {
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    try {
      JoranConfigurator configurator = new JoranConfigurator();
      configurator.setContext(context);
      // Call context.reset() to clear any previous configuration, e.g. default
      // configuration. For multi-step configuration, omit calling context.reset().
      context.reset();
      configurator.doConfigure(config);
    } catch (JoranException je) {
      // StatusPrinter will handle this
    }

  }

//    public static void setConsoleLevel(Logger logger, Level level)
//    {
//        Logger tempLogger = logger;
//        while (tempLogger != null)
//        {
//            System.out.println(tempLogManager.getName());
//            tempLogManager.setLevel(level);
//            for (Handler handler : tempLogManager.getHandlers())
//                if (handler instanceof ConsoleHandler)
//                    handler.setLevel(level);
//            tempLogger = tempLogManager.getParent();
//        }
//    }

  // public static void setConsoleDefaultFormatter()
  // {
  // Logger logger = LoggerFactory.getLogger("org.seage");
  // setConsoleDefaultFormatter(logger);
  //
  // }
//    public static void setConsoleDefaultFormatter(Logger logger, Level level)
//    {
//        Formatter formatter = new LogFormatter();
//        Logger tempLogger = logger;
//        while (tempLogger != null)
//        {
//            System.out.println(tempLogManager.getName());
//            tempLogManager.setLevel(level);
//            for (Handler handler : tempLogManager.getHandlers())
//                if (handler instanceof ConsoleHandler)
//                {
//                    handler.setLevel(level);
//                    handler.setFormatter(formatter);
//                }
//            tempLogger = tempLogManager.getParent();
//        }
//    }

//    public static class StdoutConsoleHandler extends ConsoleHandler
//    {
//        @Override
//        protected void setOutputStream(OutputStream out) throws SecurityException
//        {
//            super.setOutputStream(System.out); // kitten killed here :-(
//        }
//    }

}
