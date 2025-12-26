package org.ln.noor.core.factory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.ln.noor.core.factory.TagFactory;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.tag.AbstractTag;
import org.springframework.context.support.StaticMessageSource;

class TagFactoryTest {

    private TagFactory newFactory() {
        StaticMessageSource source = new StaticMessageSource();
        source.addMessage("tag.word.description", Locale.getDefault(), "word");
        return new TagFactory(new I18n(source));
    }

    @Test
    void create_returnsRegisteredTag() {
        TagFactory factory = newFactory();

        AbstractTag tag = factory.create("Word", 1);

        assertThat(tag).isNotNull();
        assertThat(tag.getTagName()).isEqualTo("Word");
    }

    @Test
    void create_returnsNullForUnknownTag() {
        TagFactory factory = newFactory();

        AbstractTag tag = factory.create("Unknown");

        assertThat(tag).isNull();
    }
}
