package org.ezapi.html;

public class TitleContent implements HeadContent {

    private final String title;

    public TitleContent(String title) {
        this.title = title;
    }

    public String parseToString() {
        return "<title>" + title + "</title>";
    }

}
