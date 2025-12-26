package org.ln.noor.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.ln.noor.core.enums.RenameMode;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.service.TemplateApplier;
import org.ln.noor.tools.rename.tag.AbstractTag;
import org.springframework.context.support.StaticMessageSource;

class TemplateApplierTest {

    private I18n newI18n() {
        StaticMessageSource source = new StaticMessageSource();
        source.addMessage("tag.stub.description", Locale.getDefault(), "Stub");
        return new I18n(source);
    }

    @Test
    void apply_replacesNamePortion() {
        TemplateApplier applier = new TemplateApplier();
        AbstractTag upperCaseTag = new UpperCaseTag(newI18n());

        List<Object> components = List.of("NEW_", upperCaseTag);
        List<RenamableFile> files = List.of(
                new RenamableFile(new File("photo.jpg")),
                new RenamableFile(new File("clip.mp3"))
        );

        List<RenamableFile> result = applier.apply(components, files, RenameMode.NAME_ONLY);

        assertThat(result)
                .extracting(RenamableFile::getDestinationName)
                .containsExactly("NEW_PHOTO.jpg", "NEW_CLIP.mp3");
    }

    @Test
    void apply_replacesExtensionPortion() {
        TemplateApplier applier = new TemplateApplier();
        AbstractTag upperCaseTag = new UpperCaseTag(newI18n());

        List<Object> components = List.of(upperCaseTag);
        List<RenamableFile> files = List.of(new RenamableFile(new File("demo.txt")));

        List<RenamableFile> result = applier.apply(components, files, RenameMode.EXT_ONLY);

        assertThat(result.getFirst().getDestinationName()).isEqualTo("demo.TXT");
    }

    private static class UpperCaseTag extends AbstractTag {

        protected UpperCaseTag(I18n i18n) {
            super(i18n);
            this.tagName = "Stub";
            this.type = TagType.STRING;
        }

        @Override
        public void init() {
            newClear();
            for (String value : getOldNames()) {
                newAdd(value.toUpperCase(Locale.ROOT));
            }
        }

        @Override
        public String getDescription() {
            return "Upper case stub";
        }
    }
}
