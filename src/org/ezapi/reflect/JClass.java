package org.ezapi.reflect;

import java.util.ArrayList;
import java.util.List;

public final class JClass {

    private final String packageName;

    private final String className;

    private final List<String> imports = new ArrayList<>();

    public JClass(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public void importClass(String className) {
        if (!classExists(className)) return;
        if (imports.contains(className)) return;
        imports.add(className);
    }

    public void importClass(Class<?> clazz) {
        importClass(clazz.getName());
    }

    public String getPackage() {
        return packageName;
    }

    public String getSimpleName() {
        return className;
    }

    public String getName() {
        return packageName.endsWith(".") ? packageName + className : packageName + "." + className;
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
