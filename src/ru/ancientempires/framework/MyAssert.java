package ru.ancientempires.framework;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class MyAssert
{
	
	public static DataOutputStream	output;
	public static PrintWriter		outputText;
	
	public static void a(boolean booleanTrue)
	{
		if (!booleanTrue)
		{
			MyLog.l("exception");
			if (false)
				try
				{
					if (MyAssert.output != null)
						MyAssert.output.close();
					if (MyAssert.outputText != null)
						MyAssert.outputText.close();
					System.exit(0);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			// MyLog.l(new Exception().getStackTrace());
		}
	}
	
	public static void a(Object one, Object two)
	{
		MyAssert.a(one.equals(two));
	}
	
}
