package org.ezapi.html;

import java.util.ArrayList;
import java.util.List;

public class BodyContainer implements HtmlContent {

    private final List<BodyContent> bodyContents = new ArrayList<>();

    public BodyContainer add(BodyContent bodyContent) {
        if (!bodyContents.contains(bodyContent)) {
            bodyContents.add(bodyContent);
        }
        return this;
    }

    public String parseToString() {
        StringBuilder in = new StringBuilder();
        for (BodyContent bodyContent : bodyContents) {
            in.append(bodyContent.parseToString()).append("\n");
        }
        return "<body>\n" + in + "</body>";
    }

}
