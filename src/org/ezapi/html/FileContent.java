package org.ezapi.html;

import java.io.*;

public class FileContent /* implements PageComponent */ {

    private byte[] bytes = new byte[1024];

    public FileContent(File file) {
        try {
            int i;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((i = new FileInputStream(file).read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, i);
            }
        } catch (IOException ignored) {
        }
    }

    public byte[] get() {
        return bytes;
    }

    public boolean isFile() {
        return true;
    }
}
