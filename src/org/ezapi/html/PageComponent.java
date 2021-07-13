package org.ezapi.html;

public interface PageComponent {

    byte[] get();

    default boolean isPlain() {
        return false;
    }

    default boolean isPng() {
        return false;
    }

    default boolean isJpg() {
        return false;
    }

    default boolean isFile() {
        return false;
    }

    String parseToString();

}
