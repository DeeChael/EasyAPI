package org.ezapi.util;

import org.ezapi.EasyAPI;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public final class EzClassLoader {

    //private static final URLClassLoader CLASS_LOADER = (URLClassLoader) URLClassLoader.getSystemClassLoader();

    private static final Unsafe UNSAFE;
    private static final MethodHandles.Lookup LOOKUP;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
            UNSAFE.ensureClassInitialized(MethodHandles.Lookup.class);
            Field lookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            Object lookupBase = UNSAFE.staticFieldBase(lookupField);
            long lookupOffset = UNSAFE.staticFieldOffset(lookupField);
            LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(lookupBase, lookupOffset);
        } catch (Throwable t) {
            throw new IllegalStateException("Unsafe not found");
        }
    }

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
            load2(file);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private static void load2(File file) throws IOException, ClassNotFoundException {
        URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> enumeration = jar.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();
            if (entry.getName().endsWith(".class")) {
                classLoader.loadClass(entry.getName().replace("/", ".").replace(".class", ""));
                System.out.println(entry.getName().replace("/", ".").replace(".class", ""));
            }
        }

    }

    private static void load1(URL url) throws Throwable {
        ClassLoader loader = EasyAPI.getInstance().getClass().getClassLoader();
        if (loader instanceof URLClassLoader) {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(loader, url);
            return;
        }
        if ("LaunchClassLoader".equals(loader.getClass().getSimpleName())) {
            MethodHandle methodHandle = LOOKUP.findVirtual(loader.getClass(), "addURL", MethodType.methodType(void.class, java.net.URL.class));
            methodHandle.invoke(loader, url);
        } else {
            Field ucpField = loader.getClass().getDeclaredField("ucp");
            long ucpOffset = UNSAFE.objectFieldOffset(ucpField);
            Object ucp = UNSAFE.getObject(loader, ucpOffset);
            MethodHandle methodHandle = LOOKUP.findVirtual(ucp.getClass(), "addURL", MethodType.methodType(void.class, java.net.URL.class));
            methodHandle.invoke(ucp, url);
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
