package org.seage;

import org.junit.Test;
import org.seage.launcher.Launcher;

public class LauncherTest
{

    @Test
    public void test()
    {
        Launcher.main(null);
        Launcher.main(new String[]{"asdf"});
        Launcher.main(new String[]{"--help"});
    }

}
