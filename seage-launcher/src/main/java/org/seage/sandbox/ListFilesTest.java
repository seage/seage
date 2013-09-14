package org.seage.sandbox;

import java.io.File;

public class ListFilesTest
{

	public static void main(String[] args)
	{
		File f = new File("/home/rick/Projects/seage/target/output/experiment-logs-test");
		File[] fl = f.listFiles();
		@SuppressWarnings("unused")
		int a = fl.length;
	}

}
