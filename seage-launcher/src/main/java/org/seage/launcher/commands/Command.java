package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;

public abstract class Command {
  @Parameter(names = "--help", help = true)
  public boolean help;
  
  public abstract void performCommand() throws Exception;
}
