package org.ezapi.util;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarFile;

public final class FileUtils {

    public static void create(File file, boolean isFile) {
        if (file.exists() && file.isFile() && isFile) return;
        if (isFile) {
            try {
                if (file.getParentFile() != null) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                }
                file.createNewFile();
            } catch (IOException ignored) {
            }
        } else {
            file.mkdirs();
        }
    }

    public static String readText(File file) {
        try {
            Scanner scanner = new Scanner(file);
            StringBuilder text = new StringBuilder();
            while (scanner.hasNext()) {
                text.append(scanner.nextLine());
                if (scanner.hasNext()) text.append("\n");
            }
            scanner.close();
            return text.toString();
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    public static void writeText(File file, String text) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    public static List<File> listFiles(File directory) {
        List<File> files = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] listed = directory.listFiles();
            if (listed != null) {
                if (listed.length > 0) {
                    files.addAll(Arrays.asList(listed));
                }
            }
        }
        return files;
    }

    public static List<Class<?>> getClasses(Plugin plugin, String[] ignore) {
        List<Class<?>> classes = new CopyOnWriteArrayList<>();
        URL url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            File src;
            try {
                src = new File(url.toURI());
            } catch (URISyntaxException e) {
                src = new File(url.getPath());
            }
            new JarFile(src).stream().filter(entry -> entry.getName().endsWith(".class")).forEach(entry -> {
                String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                try {
                    if (Arrays.stream(ignore).noneMatch(className::startsWith)) {
                        classes.add(Class.forName(className, false, plugin.getClass().getClassLoader()));
                    }
                } catch (Throwable ignored) {
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return classes;
    }

}
