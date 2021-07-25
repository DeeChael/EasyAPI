package org.ezapi.util;

import org.bukkit.Bukkit;

import java.lang.reflect.*;

public final class ReflectionUtils {

    public static boolean isExtended(Class<?> child, Class<?> parent) {
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

    public static Class<?> getClassInClass(Class<?> clazz, String className) {
        return getClass(clazz.getName() + "$" + className);
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

    public static Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... arguments) {
        try {
            Method method = clazz.getDeclaredMethod(name, arguments);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    public static Class<?> getArrayClassFromClass(Class<?> classToTransferToArrayClass) {
        return Array.newInstance(classToTransferToArrayClass, 1).getClass();
    }

    public static Class<?> getClassFromArrayClass(Class<?> arrayClassToTransferToClass) {
        return arrayClassToTransferToClass.getComponentType();
    }

    public static Class<?> getClass(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException ignored) {
        }
        return Object.class;
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
