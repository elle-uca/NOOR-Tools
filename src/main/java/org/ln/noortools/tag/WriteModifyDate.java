package org.ln.noortools.tag;

import java.nio.file.attribute.FileTime;
import java.time.Instant;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public class WriteModifyDate extends AbstractFsTag implements ActionTag{

    public WriteModifyDate(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "WriteModifyDate";
    }

    @Override
    public void init() {
        newClear();
        String value = getStringArg(0, "").trim();
        for (int i = 0; i < filesCtx.size(); i++) 
        	newAdd(value);
    }

    @Override
    public void performAction() {
        String ts = getStringArg(0, "");
        try {
            FileTime t = FileTime.from(Instant.parse(ts));
            for (RenamableFile rf : filesCtx) {
                writeTime(rf, "basic:lastModifiedTime", t);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public String getDescription() {
        return "Writes modified timestamp (ISO-8601)";
    }
}
