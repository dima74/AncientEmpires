package ru.ancientempires.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import ru.ancientempires.framework.MyAssert;

public class AssetsHelper {

	private ClassLoader classLoader = getClass().getClassLoader();

	public InputStream openIS(String name) throws IOException {
		MyAssert.a(false);
		return classLoader.getResourceAsStream(name);
	}

	public boolean exists(String name) {
		if (true)
			return false;
		try {
			InputStream is = openIS(name);
			if (is == null)
				return false;
			is.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void print(Enumeration<URL> resources) {
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			System.out.println(url);
		}
	}

	public String[] list(String prefix) {
		return null;
	}
}
