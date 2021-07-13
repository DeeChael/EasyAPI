package org.ezapi.html;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PngImageContent implements PageComponent {

    private byte[] bytes = new byte[1024];

    public PngImageContent(BufferedImage image) {
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final boolean isPng() {
        return true;
    }

    public byte[] get() {
        return bytes;
    }

    @Override
    public String parseToString() {
        return "{\"png\":\"" + bytes + "\"}";
    }

}
