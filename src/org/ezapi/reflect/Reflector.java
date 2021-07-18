package org.ezapi.reflect;

import com.sun.istack.internal.NotNull;
import org.ezapi.returns.DoubleReturn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class Reflector {

    private final Class<?> clazz;

    private final List<Map.Entry<String, DoubleReturn<Class<?>[],Object[]>>> invokedMethod = new ArrayList<>();

    private final Object object;

    public Reflector(Object object) {
        this.object = object;
        this.clazz = object.getClass();
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
            for (Map.Entry<String,DoubleReturn<Class<?>[], Object[]>> entry : invokedMethod) {
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
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
