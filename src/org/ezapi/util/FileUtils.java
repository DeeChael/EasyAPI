package org.ezapi.util;

import org.ezapi.storage.JsonStorage;
import org.ezapi.storage.PropertiesStorage;
import org.ezapi.storage.Storage;
import org.ezapi.storage.YamlStorage;

import java.io.*;
import java.util.Scanner;

public final class FileUtils {

    public static void main(String[] args) {
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

    public static Storage getStorage(File file) {
        if (!file.exists()) return null;
        if (file.getName().endsWith(".json")) {
            return new JsonStorage(file);
        } else if (file.getName().endsWith(".yml") || file.getName().endsWith(".yaml")) {
            return new YamlStorage(file);
        } else if (file.getName().endsWith("properties")) {
            return new PropertiesStorage(file);
        }
        return null;
    }

}
