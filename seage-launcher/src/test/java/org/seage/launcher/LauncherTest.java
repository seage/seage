package org.seage.launcher;

import org.junit.jupiter.api.Test;

public class LauncherTest {

  @Test
  public void test() {
    Launcher.main(new String[] {});
    Launcher.main(new String[] { "asdf" });
    Launcher.main(new String[] { "--help" });
  }

}
