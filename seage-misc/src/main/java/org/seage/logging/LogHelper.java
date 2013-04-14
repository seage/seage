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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * 
 * @author rick
 */
public class LogHelper
{

    public static void loadConfig(String path)
    {
        try
        {
            FileInputStream configFile = new FileInputStream(path);
            LogManager.getLogManager().readConfiguration(configFile);
            configFile.close();

        }
        catch (IOException ex)
        {
            System.err.println("WARNING: Could not open configuration file");
            System.err.println("WARNING: Logging not configured (console output only)");

            setConsoleDefaultFormatter(Logger.getLogger("org.seage"), Level.ALL);
        }
    }

    public static void setConsoleLevel(Logger logger, Level level)
    {
        Logger tempLogger = logger;
        while (tempLogger != null)
        {
            System.out.println(tempLogger.getName());
            tempLogger.setLevel(level);
            for (Handler handler : tempLogger.getHandlers())
                if (handler instanceof ConsoleHandler)
                    handler.setLevel(level);
            tempLogger = tempLogger.getParent();
        }
    }
    
//    public static void setConsoleDefaultFormatter()
//    {
//    	Logger logger = Logger.getLogger("org.seage");
//    	setConsoleDefaultFormatter(logger);
//        
//    }
    public static void setConsoleDefaultFormatter(Logger logger, Level level)
    {
    	Formatter formatter = new LogFormatter();
    	Logger tempLogger = logger;
        while (tempLogger != null)
        {
            System.out.println(tempLogger.getName());
            tempLogger.setLevel(level);
            for (Handler handler : tempLogger.getHandlers())
                if (handler instanceof ConsoleHandler){
                    handler.setLevel(level);
                    handler.setFormatter(formatter);
                }
            tempLogger = tempLogger.getParent();
        }
    }

    public static class StdoutConsoleHandler extends ConsoleHandler
    {
        protected void setOutputStream(OutputStream out) throws SecurityException
        {
            super.setOutputStream(System.out); // kitten killed here :-(
        }
    }

}
