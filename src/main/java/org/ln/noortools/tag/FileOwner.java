package org.ln.noortools.tag;

import java.nio.file.Files;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public class FileOwner extends AbstractFsTag {
    
	public FileOwner(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "FileOwner";
    }

    @Override
    public void init() {
        newClear();
        for (RenamableFile rf : filesCtx) {
            try {
                String owner = Files.getOwner(rf.getSource().toPath()).getName();
                newAdd(owner);
            } catch (Exception e) {
                newAdd("Unknown");
            }
        }
    }
    
	@Override
	public String getActionDescription() {
		return "Returns file owner (OS-level)";
	}

	@Override
	public String getDescription() {
		return "Returns file owner (OS-level)";
	}
}
