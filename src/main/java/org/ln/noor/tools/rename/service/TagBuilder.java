package org.ln.noor.tools.rename.service;

import java.util.ArrayList;
import java.util.List;

import org.ln.noor.tools.rename.tag.AbstractTag;
import org.ln.noor.tools.rename.tag.TagFactory;
import org.springframework.stereotype.Component;

/**
 * Translates template tokens into concrete components that can be used by
 * the renaming pipeline. Plain text is kept as strings while tag tokens are
 * resolved through {@link org.ln.noor.tools.rename.tag.TagFactory}.
 *
 * @author Luca Noale
 */
@Component
public class TagBuilder {

    private final TagFactory tagFactory;

    public TagBuilder(TagFactory tagFactory) {
        this.tagFactory = tagFactory;
    }

    /**
     * Converts the parsed template tokens into runtime components.
     *
     * @param tokens ordered list of template parts
     * @return a mixed list of strings and {@link AbstractTag} instances
     */
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
