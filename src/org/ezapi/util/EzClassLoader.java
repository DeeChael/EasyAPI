package org.ezapi.util;

import java.io.File;

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
    }

}
