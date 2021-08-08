package org.ezapi.reflect;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class JClassManager {

    private JClassManager() {}

    private static final JClassLoader classLoader = new JClassLoader();

    public static void addClass(JClass jClass) {

    }

    public static Class<?> getClass(String className) {
        try {
            return classLoader.findClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static class JFileManager extends ForwardingJavaFileManager {

        private JFileObject javaFileObject;

        protected JFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public JFileObject getJavaFileObject() {
            return javaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return javaFileObject = new JFileObject(className, kind);
        }

    }

    private static class JFileObject extends SimpleJavaFileObject {

        private ByteArrayOutputStream byteArrayOutputStream;

        protected JFileObject(String className, Kind kind) {
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

    private static class JClassLoader extends ClassLoader {

        private final Map<String, byte[]> classes = new HashMap<>();

        private JClassLoader() {
        }

        public void addNewClass(String className, byte[] bytes) {
            if (!classes.containsKey(className)) {
                classes.put(className, bytes);
            }
        }

        @Override
        protected Class<?> findClass(String className) throws ClassNotFoundException {
            if (classes.containsKey(className)) {
                byte[] bytes = classes.get(className);
                return defineClass(className, bytes, 0, bytes.length);
            } else {
                return super.findClass(className);
            }
        }
    }

}
