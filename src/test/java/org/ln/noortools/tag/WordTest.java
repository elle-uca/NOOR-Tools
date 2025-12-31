package org.ln.noortools.tag;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.tag.Word;
import org.springframework.context.support.StaticMessageSource;
/**
 * WordTest.
 *
 * @author Luca Noale
 */

class WordTest {

    private I18n newI18n() {
        StaticMessageSource source = new StaticMessageSource();
        source.addMessage("tag.word.description", Locale.getDefault(), "Word tag");
        return new I18n(source);
    }

    @Test
    void extractSubstringsFromChars_handlesRegexSpecialCharacters() {
        List<String> parts = Word.extractSubstringsFromChars("a-b[c]d", "-[]");

        assertThat(parts).containsExactly("a", "b", "c", "d");
    }

    @Test
    void init_buildsIndexedSegmentFromOldNames() {
        Word word = new Word(newI18n(), 2);
        word.setOldNames(List.of("file-name_test"));

        word.init();

        assertThat(word.getNewNames()).containsExactly("name");
    }
}
