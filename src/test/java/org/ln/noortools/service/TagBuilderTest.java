package org.ln.noortools.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.ln.noortools.factory.TagFactory;
import org.ln.noortools.i18n.I18n;
import org.springframework.context.support.StaticMessageSource;

class TagBuilderTest {

    private TagFactory factory() {
        StaticMessageSource source = new StaticMessageSource();
        return new TagFactory(new I18n(source));
    }

    @Test
    void buildComponentsCreatesTagsAndKeepsText() {
        TagBuilder builder = new TagBuilder(factory());

        TemplateTokenizer tokenizer = mock(TemplateTokenizer.class);
        when(tokenizer.tokenize("Hello <Word:1>"))
                .thenReturn(List.of(new TextComponent("Hello "), new TagToken("Word", List.of(1))));

        List<Object> components = builder.buildComponents(tokenizer.tokenize("Hello <Word:1>"));

        assertThat(components).hasSize(2);
        assertThat(components.getFirst()).isEqualTo("Hello ");
        assertThat(components.get(1)).isInstanceOf(org.ln.noortools.tag.Word.class);
    }
}

