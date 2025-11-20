package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.factory.TagFactory;
import org.ln.noortools.tag.AbstractTag;
import org.springframework.stereotype.Component;

@Component
public class TagBuilder {

    private final TagFactory tagFactory;

    public TagBuilder(TagFactory tagFactory) {
        this.tagFactory = tagFactory;
    }

    public List<Object> buildComponents(List<TemplateComponent> tokens) {
        List<Object> components = new ArrayList<>();
        for (TemplateComponent token : tokens) {
            if (token instanceof TextComponent text) {
                components.add(text.text());
            } else if (token instanceof TagToken tagToken) {
                AbstractTag tag = tagFactory.create(tagToken.name(), tagToken.arguments().toArray());
                if (tag != null) {
                    components.add(tag);
                }
            }
        }
        return components;
    }
}
