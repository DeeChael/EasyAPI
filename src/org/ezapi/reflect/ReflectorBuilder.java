package org.ezapi.reflect;

import com.sun.istack.internal.NotNull;
import org.ezapi.returns.DoubleReturn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

public final class ReflectorBuilder {

    private final Class<?> clazz;

    private Constructor<?> constructor;

    private Object[] objects = new Object[0];

    private final List<Entry<String, DoubleReturn<Class<?>[],Object[]>>> invokedMethod = new ArrayList<>();

    public ReflectorBuilder(Class<?> clazz) {
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        this.clazz = clazz;
        try {
            this.constructor = clazz.getDeclaredConstructor();
            this.constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setConstructor(@NotNull Class<?>[] classes, @NotNull Object[] objects) {
        if (classes.length != objects.length) return;
        try {
            this.constructor = clazz.getDeclaredConstructor(classes);
            this.objects = objects;
            this.constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void invoke(String methodName, @NotNull Class<?>[] classes, @NotNull Object[] objects) {
        if (classes.length != objects.length) return;;
        boolean found = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                if (!Arrays.equals(method.getParameterTypes(), classes)) return;
                found = true;
            }
        }
        if (!found) return;
        this.invokedMethod.add(new AbstractMap.SimpleEntry<>(methodName, new DoubleReturn<>(classes, objects)));
    }

    public void setField(String fieldName, Object value) {
        boolean found = false;
        for (Field field : clazz.getFields()) {
            if (fieldName.equals(field.getName())) {
                if (!field.getType().equals(value.getClass())) return;
                found = true;
            }
        }
        if (!found) return;
        this.invokedMethod.add(new AbstractMap.SimpleEntry<>("setField$" + fieldName, new DoubleReturn<>(null, new Object[] { value })));
    }

    public Object build() {
        try {
            Object object = constructor.newInstance(objects);
            for (Entry<String,DoubleReturn<Class<?>[], Object[]>> entry : invokedMethod) {
                String methodName = entry.getKey();
                if (methodName.startsWith("setField$")) {
                    methodName = methodName.replace("setField$", "");
                    Field field = clazz.getDeclaredField(methodName);
                    field.setAccessible(true);
                    field.set(object, entry.getValue().second()[0]);
                    continue;
                }
                DoubleReturn<Class<?>[], Object[]> doubleReturn = entry.getValue();
                Method method = clazz.getDeclaredMethod(methodName, doubleReturn.first());
                method.setAccessible(true);
                method.invoke(object, doubleReturn.second());
                return object;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
