package org.ezapi.html;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JpegImageContent implements PageComponent {

    private byte[] bytes = new byte[1024];

    public JpegImageContent(BufferedImage image) {
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final boolean isJpg() {
        return true;
    }

    public byte[] get() {
        return bytes;
    }

    @Override
    public String parseToString() {
        return "{\"jpg\":\"" + bytes + "\"}";
    }

}
