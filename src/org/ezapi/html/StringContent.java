package org.ezapi.html;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class StringContent implements BodyContent, PageComponent {

    private final String text;

    public StringContent(String text) {
        this.text = text;
    }

    @Override
    public String parseToString() {
        return text;
    }

    @Override
    public final boolean isPlain() {
        return true;
    }

    public byte[] get() {
        return parseToString().getBytes();
    }

}
