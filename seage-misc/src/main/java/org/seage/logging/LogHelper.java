/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author rick
 */
public class LogHelper {
    
    public static void loadConfig(String path)
    {
        try {
            FileInputStream configFile = new FileInputStream(path);
            LogManager.getLogManager().readConfiguration(configFile);
            configFile.close();

        } catch (IOException ex){
            System.err.println("WARNING: Could not open configuration file");
            System.err.println("WARNING: Logging not configured (console output only)");
        }
    }
    
    public static void setConsoleFineLevel(Logger logger, Level level)
    {
        Logger tempLogger = logger;
        while(tempLogger != null) {
            System.out.println(tempLogger.getName());
           tempLogger.setLevel(Level.ALL);
           for(Handler handler : tempLogger.getHandlers())
               if(handler instanceof ConsoleHandler)
                    handler.setLevel(Level.ALL);
           tempLogger = tempLogger.getParent();
        }
    }
}
