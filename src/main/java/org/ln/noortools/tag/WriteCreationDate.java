package org.ln.noortools.tag;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public class WriteCreationDate extends AbstractFsTag implements ActionTag{

    public WriteCreationDate(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "WriteCreationDate";
    }

    @Override
    public void init() {
        newClear();
        String value = getStringArg(0, "").trim();
        for (int i = 0; i < filesCtx.size(); i++) newAdd(value);
    }

    @Override
    public void performAction() {
        String ts = getStringArg(0, "");
        try {
            FileTime t = FileTime.from(Instant.parse(ts));
            for (RenamableFile rf : filesCtx) {
                writeTime(rf, "basic:creationTime", t);
            }
        } catch (DateTimeParseException e) {
            System.err.println("[WriteCreationDate] Invalid timestamp: " + ts);
        }
    }

    @Override
    public String getDescription() {
        return "Writes creation timestamp (ISO-8601)";
    }
}
