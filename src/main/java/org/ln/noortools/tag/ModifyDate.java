package org.ln.noortools.tag;

import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public class ModifyDate extends AbstractFsTag {
    
	public ModifyDate(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "ModifyDate";
        this.type = TagType.DATE_TIME;
    }

    @Override
    public void init() {
        newClear();
        for (RenamableFile rf : filesCtx) {
            try {
                BasicFileAttributes attrs = Files.readAttributes(
                    rf.getSource().toPath(),
                    BasicFileAttributes.class
                );
                String value = attrs.lastModifiedTime().toString().replace("T", " ").replace("Z","");
                newAdd(value);
            } catch (Exception e) {
                newAdd("");
            }
        }
    }

	@Override
	public String getDescription() {
		return "Returns last modified timestamp";
	}
}
