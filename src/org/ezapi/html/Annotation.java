package org.ezapi.html;

public class Annotation implements HtmlContent, HeadContent, BodyContent {

    private String input = "";

    public Annotation(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String parseToString() {
        return "<!-- " + input + " -->";
    }

}
