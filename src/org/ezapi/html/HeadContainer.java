package org.ezapi.html;

import java.util.ArrayList;
import java.util.List;

public class HeadContainer implements HtmlContent {

    private final List<HeadContent> headerContents = new ArrayList<>();

    public HeadContainer add(HeadContent headerContent) {
        if (!headerContents.contains(headerContent)) {
            headerContents.add(headerContent);
        }
        return this;
    }

    public String parseToString() {
        StringBuilder in = new StringBuilder();
        for (HeadContent headerContent : headerContents) {
            in.append(headerContent.parseToString()).append("\n");
        }
        return "<head>\n" + in + "</head>";
    }

}
