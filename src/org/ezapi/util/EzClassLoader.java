package org.ezapi.util;

import org.ezapi.EasyAPI;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class EzClassLoader {

    //private static final URLClassLoader CLASS_LOADER = (URLClassLoader) URLClassLoader.getSystemClassLoader();

    public static void load(File file) {
        /*
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(CLASS_LOADER, file.toURI().toURL());
        } catch (MalformedURLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
         */
        try {
            load0(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private static void load0(URL jar) {
        try(URLClassLoader classLoader = new URLClassLoader(new URL[] { jar }, EasyAPI.class.getClassLoader()); JarInputStream jis = new JarInputStream(jar.openStream())) {
            while (true) {
                JarEntry j = jis.getNextJarEntry();
                if (j == null) break;
                String name = j.getName();
                if (name.isEmpty()) continue;
                if (name.endsWith(".class")) {
                    name = name.replace("/", ".");
                    String cname = name.substring(0, name.length() - 6);
                    Class<?> clazz = classLoader.loadClass(cname);
                    System.out.println(clazz.getName());
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
