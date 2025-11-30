package org.ln.noortools.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class TemplateTokenizerTest {

    private final TemplateTokenizer tokenizer = new TemplateTokenizer();

    @Test
    void tokenize_splitsIntoTextAndTags() {
        List<TemplateComponent> result = tokenizer.tokenize("Report_<IncrNum:1:2>.txt");

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isInstanceOf(TextComponent.class);
        assertThat(((TextComponent) result.get(0)).text()).isEqualTo("Report_");

        assertThat(result.get(1)).isInstanceOf(TagToken.class);
        TagToken token = (TagToken) result.get(1);
        assertThat(token.name()).isEqualTo("IncrNum");
        assertThat(token.arguments()).containsExactly(1, 2);

        assertThat(result.get(2)).isInstanceOf(TextComponent.class);
        assertThat(((TextComponent) result.get(2)).text()).isEqualTo(".txt");
    }

    @Test
    void tokenize_ignoresMalformedTags() {
        List<TemplateComponent> result = tokenizer.tokenize("file <1:2> name");

        assertThat(result).hasSize(3);
        assertThat(result.get(1)).isInstanceOf(TextComponent.class);
        assertThat(((TextComponent) result.get(1)).text()).isEqualTo("<1:2>");
    }
}
