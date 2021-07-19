package org.ezapi.util;

import org.apache.commons.lang.IllegalClassException;

import java.io.*;

public final class ObjectUtils {

    public static byte[] toBytes(Object object) {
        byte[] bytes = new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException ignored) {
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) {
        Object object = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException ignored) {
        }
        return object;
    }

    public static <T> T toObject(byte[] bytes, Class<T> clazz) {
        Object object = toObject(bytes);
        if (!clazz.isInstance(object)) throw new IllegalClassException("Object doesn't extended class " + clazz.getName());
        return clazz.cast(object);
    }

}
