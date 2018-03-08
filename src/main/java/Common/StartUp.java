package Common;

import Data.DataAccess;
import org.nwnx.nwnx2.jvm.*;

import java.io.File;

@SuppressWarnings("UnusedDeclaration")
public class StartUp {

	private static EventReceiver e = new EventReceiver();

	/**
	 * Called before any configured classes and methods
	 * are looked up in jni. You can use this to setup
	 * class pathes and load plugins/custom classes that
	 * are required to start up.
	*/
	@SuppressWarnings("unused")
	private static void setup() {
		File[] jarFiles = new File("/nwn/home/jvm").listFiles();

        assert jarFiles != null;
        for(File file : jarFiles)
		{
			try
			{
				if(file.getName().equals("CZS.jar") ||
						file.getName().equals("org.nwnx.nwnx2.jvm.jar"))
					continue;

				System.out.println("Loading jar: " + file.getName());
				loadLibrary(file);
			}
			catch (Exception ex)
			{
				System.out.println("Could not load jar file: " + file.getName());
			}
		}
	}

	/**
	 * Called just before continuing startup inside NWNX.
	 * Use this to do your usual initialisation - do not
	 * use setup() for that.
	*/
	@SuppressWarnings("unused")
	private static void init() {
		Scheduler.addSchedulerListener(e);
		DataAccess.Initialize();

		/* Add some default handlers that don't do any
		 * custom wrapping at all.
		 */
		NWObject.registerObjectHandler((obj, valid, objectType, resRef, tag) -> obj);
		NWEffect.registerEffectHandler(eff -> eff);
		NWItemProperty.registerItemPropertyHandler(prp -> prp);

	}

	/**
	 * Called just before terminating the JVM. No NWN context
	 * is available. Not called on errors or crashes.
	 */
	@SuppressWarnings("unused")
	private static void shutdown() {

	}



	/*
     * Adds the supplied Java Archive library to java.class.path. This is benign
     * if the library is already loaded.
     * Reference: https://stackoverflow.com/questions/27187566/load-jar-dynamically-at-runtime
     */
	private static synchronized void loadLibrary(java.io.File jar) throws Exception
	{
		try {
            /*We are using reflection here to circumvent encapsulation; addURL is not public*/
			java.net.URLClassLoader loader = (java.net.URLClassLoader)ClassLoader.getSystemClassLoader();
			java.net.URL url = jar.toURI().toURL();
            /*Disallow if already loaded*/
			for (java.net.URL it : java.util.Arrays.asList(loader.getURLs())){
				if (it.equals(url)){
					return;
				}
			}
			java.lang.reflect.Method method = java.net.URLClassLoader.class.getDeclaredMethod("addURL", java.net.URL.class);
			method.setAccessible(true); /*promote the method to public access*/
			method.invoke(loader, url);
		} catch (final java.lang.NoSuchMethodException |
				java.lang.IllegalAccessException |
				java.net.MalformedURLException |
				java.lang.reflect.InvocationTargetException e){
			throw new Exception(e);
		}
	}
}
