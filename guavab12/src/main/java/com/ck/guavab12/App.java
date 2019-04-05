package com.ck.guavab12;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        // Load a framework factory
        FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();

        // Create a framework
        Map<String, String> config = new HashMap<String, String>();
        config.put("org.osgi.framework.storage.clean", "onFirstInit");

        Framework concierge = frameworkFactory.newFramework(config);

        // Start the framework
        concierge.start();

        BundleContext context = concierge.getBundleContext();

        System.out.println("user.dir=" + System.getProperty("user.dir", null));

        Bundle b1 = context.installBundle("file:../guavab1/target/guavab1-0.0.1-SNAPSHOT.jar");
        Bundle b2 = context.installBundle("file:../guavab2/target/guavab2-0.0.1-SNAPSHOT.jar");

        invokeTest(b1, "com.ck.guavam1.App1");
        invokeTest(b2, "com.ck.guavam2.App2");
    }

    private static void invokeTest(Bundle b1, String className)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> c1 = b1.loadClass(className);
        Method test = c1.getMethod("test");
        System.out.println(b1 + " says: " + test.invoke(null));
    }
}
