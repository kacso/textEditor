package hr.fer.zemris.ooup.lab3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TextEditorFactory {
	public static Plugin newInstance(String name) {
		Class<Plugin> clazz = null;
		Plugin plugin = null;
		try {
			clazz = (Class<Plugin>) Class
					.forName("hr.fer.zemris.ooup.lab3.plugins." + name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Constructor<?> ctr;
		try {
			ctr = clazz.getConstructor();
			plugin = (Plugin) ctr.newInstance();

		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return plugin;
	}
}
