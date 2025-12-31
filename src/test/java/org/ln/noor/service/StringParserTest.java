package org.ln.noor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.ln.noor.core.enums.RenameMode;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.service.StringParser;
import org.ln.noor.tools.rename.service.TagBuilder;
import org.ln.noor.tools.rename.service.TemplateApplier;
import org.ln.noor.tools.rename.service.TemplateComponent;
import org.ln.noor.tools.rename.service.TemplateTokenizer;
import org.ln.noor.tools.rename.service.TextComponent;
/**
 * StringParserTest.
 *
 * @author Luca Noale
 */

class StringParserTest {

    @Test
    void isParsableValidatesAngleBracketsBalance() {
        assertThat(StringParser.isParsable("<IncN>"))
                .isTrue();
        assertThat(StringParser.isParsable("<IncN"))
                .isFalse();
        assertThat(StringParser.isParsable("  ")).isFalse();
    }

    @Test
    void parseDelegatesToCollaborators() {
        TemplateTokenizer tokenizer = mock(TemplateTokenizer.class);
        TagBuilder builder = mock(TagBuilder.class);
        TemplateApplier applier = mock(TemplateApplier.class);

        List<RenamableFile> files = List.of(new RenamableFile("fileA"));
        List<TemplateComponent> tokens = List.of(new TextComponent("abc"));
        List<Object> components = List.of("abc");

        when(tokenizer.tokenize("abc")).thenReturn(tokens);
        when(builder.buildComponents(tokens)).thenReturn(components);
        when(applier.apply(components, files, RenameMode.NAME_ONLY)).thenReturn(files);

        StringParser parser = new StringParser(tokenizer, builder, applier);
        List<RenamableFile> result = parser.parse("abc", files, RenameMode.NAME_ONLY);

        assertThat(result).isEqualTo(files);
        verify(tokenizer).tokenize("abc");
        verify(builder).buildComponents(tokens);
        verify(applier).apply(components, files, RenameMode.NAME_ONLY);
    }
}

