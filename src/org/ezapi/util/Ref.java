package org.ezapi.util;

import org.bukkit.Bukkit;
import sun.misc.Unsafe;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public final class Ref {

    private Ref() {}

    private static Unsafe UNSAFE;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    public static boolean isExtendedFrom(Class<?> child, Class<?> parent) {
        return parent.isAssignableFrom(child);
    }

    public static boolean isProtected(Class<?> clazz) {
        return Modifier.isProtected(clazz.getModifiers());
    }

    public static boolean isPrivate(Class<?> clazz) {
        return Modifier.isPrivate(clazz.getModifiers());
    }

    public static boolean isPublic(Class<?> clazz) {
        return Modifier.isPublic(clazz.getModifiers());
    }

    public static boolean isAbstract(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isFinal(Class<?> clazz) {
        return Modifier.isFinal(clazz.getModifiers());
    }

    public static boolean isAnnotation(Class<?> clazz) {
        return clazz.isAnnotation();
    }

    public static boolean isInterface(Class<?> clazz) {
        return clazz.isInterface();
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

    public static boolean isEnum(Class<?> clazz) {
        return clazz.isEnum();
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    public static Class<?> getArrayClass(Class<?> contentClass) {
        return Array.newInstance(contentClass, 0).getClass();
    }
    
    public static Class<?> getContentClass(Class<?> arrayClass) {
        return arrayClass.isArray() ? arrayClass.getComponentType() : null;
    }
    
    public static <T extends Enum<T>> T getEnumObject(Class<T> enumClass, String name) {
        return Enum.valueOf(enumClass, name);
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... arguments) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(arguments);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }
    
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... classes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, classes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    public static long getFieldOffset(Field field) {
        return Modifier.isStatic(field.getModifiers()) ? UNSAFE.staticFieldOffset(field) : UNSAFE.objectFieldOffset(field);
    }

    public static Class<?> getInnerClass(Class<?> owner, String name) {
        return getClass(owner.getName() + "$" + name);
    }

    public static Class<?> getOwnerClass(Class<?> innerClass) {
        String className = innerClass.getName();
        if (!className.contains("$")) {
            return null;
        }
        String[] split = className.split("\\$");
        return getClass(className.substring(0, split[split.length - 1].length() - 1));
    }

    public static Class<?> getOutestClass(Class<?> innerClass) {
        String className = innerClass.getName();
        if (!className.contains("$")) {
            return null;
        }
        return getClass(className.split("\\$")[0]);
    }

    public static boolean implemented(Class<?> clazz) {
        return clazz.getInterfaces().length > 0;
    }

    public static boolean extended(Class<?> clazz) {
        return clazz.getSuperclass() != null;
    }

    public static <T> T newInstance(Class<T> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                try {
                    return clazz.cast(constructor.newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
                    try {
                        return clazz.cast(UNSAFE.allocateInstance(clazz));
                    } catch (InstantiationException e) {
                        return null;
                    }
                }
            }
        }
        try {
            return clazz.cast(UNSAFE.allocateInstance(clazz));
        } catch (InstantiationException e) {
            return null;
        }
    }

    public static Class<?> createClass(String className, String input) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager javaFileManager = compiler.getStandardFileManager(null, null, null);
        UnsafeJavaFileManager unsafeJavaFileManager = new UnsafeJavaFileManager(javaFileManager);
        try {
            JavaCompiler.CompilationTask task = compiler.getTask(null, unsafeJavaFileManager, null, null, null, Collections.singletonList(new StringObject(new URI(className + ".java"), JavaFileObject.Kind.SOURCE, input)));
            if (task.call()) {
                UnsafeJavaFileObject javaFileObject = unsafeJavaFileManager.getJavaFileObject();
                ClassLoader classLoader = new UnsafeClassLoader(className, javaFileObject.getBytes());
                return classLoader.loadClass(className);
            }
        } catch (URISyntaxException | ClassNotFoundException ignored) {
        }
        return null;
    }

    private static class UnsafeJavaFileManager extends ForwardingJavaFileManager {

        private UnsafeJavaFileObject javaFileObject;

        protected UnsafeJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public UnsafeJavaFileObject getJavaFileObject() {
            return javaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return javaFileObject = new UnsafeJavaFileObject(className, kind);
        }

    }

    private static class UnsafeJavaFileObject extends SimpleJavaFileObject {

        private ByteArrayOutputStream byteArrayOutputStream;

        protected UnsafeJavaFileObject(String className, Kind kind) {
            super(URI.create(className + kind.extension), kind);
            this.byteArrayOutputStream = new ByteArrayOutputStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return byteArrayOutputStream;
        }

        public byte[] getBytes() {
            return this.byteArrayOutputStream.toByteArray();
        }

    }

    private static class StringObject extends SimpleJavaFileObject {

        private final String content;

        protected StringObject(URI uri, Kind kind, String content) {
            super(uri, kind);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }

    }

    private static class UnsafeClassLoader extends ClassLoader {

        private final String className;

        private final byte[] bytes;

        private UnsafeClassLoader(String className, byte[] bytes) {
            this.className = className;
            this.bytes = bytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            return defineClass(className, bytes, 0, bytes.length);
        }
    }

    public static Field getFieldOrOld(Class<?> clazz, String newName, String oldName) {
        if (Ref.getVersion() >= 16) return getField(clazz, newName);
        return getField(clazz, oldName);
    }

    /**
     * Attention: This method is not supported by 1.17+ because spigot 1.17+ removed nms package version
     * @param name Class name, if the class is in a sub package, e.p. "net.minecraft.server.{version}.command.CommandExample", you can access by "command.CommandExample"
     * @return Nms Class, if not exist return null
     */
    public static Class<?> getNmsClass(String name) {
        if (getVersion() >= 16) return null;
        return getClass("net.minecraft.server." + getServerVersion() + "." + name);
    }

    public static Class<?> getNmsOrOld(String newName, String oldName) {
        if (getVersion() >= 16) return getClass("net.minecraft." + newName);
        return getNmsClass(oldName);
    }

    /**
     * CraftBukkit still has obc version in 1.17+
     * @param name Class name, if the class is in a sub package, e.p. "org.bukkit.craftbukki.{version}.command.CommandExample", you can access by "command.CommandExample"
     * @return Obc Class, if not exist return null
     */
    public static Class<?> getObcClass(String name) {
        return getClass("org.bukkit.craftbukkit." + getServerVersion() + "." + name);
    }

    public static String getServerVersion() {
        String version = null;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (Exception e) {
            try {
                version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[1];
            } catch (Exception ignored) {
            }
        }
        return version;
    }

    public static int getProtocolVersion(String version) {
        switch (version) {
            case "1.13":
            case "1.13.0":
                return 393;
            case "1.13.1":
                return 401;
            case "1.13.2":
                return 404;
            case "1.14":
            case "1.14.0":
                return 477;
            case "1.14.1":
                return 480;
            case "1.14.2":
                return 485;
            case "1.14.3":
                return 490;
            case "1.14.4":
                return 498;
            case "1.15":
            case "1.15.0":
                return 573;
            case "1.15.1":
                return 575;
            case "1.15.2":
                return 578;
            case "1.16":
            case "1.16.0":
                return 735;
            case "1.16.1":
                return 736;
            case "1.16.2":
                return 751;
            case "1.16.3":
                return 753;
            case "1.16.4":
            case "1.16.5":
                return 754;
            case "1.17":
                return 755;
            case "1.17.1":
                return 756;
            default:
                return -1;
        }
    }

    public static int getVersion() {
        switch (getServerVersion()) {
            case "v1_8_R1": return 1;
            case "v1_8_R2": return 2;
            case "v1_8_R3": return 3;
            case "v1_9_R1": return 4;
            case "v1_9_R2": return 5;
            case "v1_10_R1": return 6;
            case "v1_11_R1": return 7;
            case "v1_12_R1": return 8;
            case "v1_13_R1": return 9;
            case "v1_13_R2": return 10;
            case "v1_14_R1": return 11;
            case "v1_15_R1": return 12;
            case "v1_16_R1": return 13;
            case "v1_16_R2": return 14;
            case "v1_16_R3": return 15;
            case "v1_17_R1": return 16;
            default: return -1;
        }
    }

    public static int getLargeVersion() {
        switch (getServerVersion()) {
            case "v1_8_R1":
            case "v1_8_R2":
            case "v1_8_R3":
                return 1;
            case "v1_9_R1":
            case "v1_9_R2":
                return 2;
            case "v1_10_R1":
                return 3;
            case "v1_11_R1":
                return 4;
            case "v1_12_R1":
                return 5;
            case "v1_13_R1":
            case "v1_13_R2":
                return 6;
            case "v1_14_R1":
                return 7;
            case "v1_15_R1":
                return 8;
            case "v1_16_R1":
            case "v1_16_R2":
            case "v1_16_R3":
                return 9;
            case "v1_17_R1":
                return 10;
            case "v1_18_R1":
                return 11;
            default: return -1;
        }
    }

    public static double getCoreVersion() {
        switch (getLargeVersion()) {
            case 1:
                return 1.08;
            case 2:
                return 1.09;
            case 3:
                return 1.10;
            case 4:
                return 1.11;
            case 5:
                return 1.12;
            case 6:
                return 1.13;
            case 7:
                return 1.14;
            case 8:
                return 1.15;
            case 9:
                return 1.16;
            case 10:
                return 1.17;
            case 11:
                return 1.18;
            default:
                return -1;
        }
    }

    public static int getLongVersion() {
        switch (getServerVersion()) {
            case "v1_8_R1": return 10801;
            case "v1_8_R2": return 10802;
            case "v1_8_R3": return 10803;
            case "v1_9_R1": return 10901;
            case "v1_9_R2": return 10902;
            case "v1_10_R1": return 11001;
            case "v1_11_R1": return 11101;
            case "v1_12_R1": return 11201;
            case "v1_13_R1": return 11301;
            case "v1_13_R2": return 11302;
            case "v1_14_R1": return 11401;
            case "v1_15_R1": return 11501;
            case "v1_16_R1": return 11601;
            case "v1_16_R2": return 11602;
            case "v1_16_R3": return 11603;
            case "v1_17_R1": return 11701;
            default: return -1;
        }
    }

}
