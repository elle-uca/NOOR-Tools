package org.ln.noortools.tag;

import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.List;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public abstract class AbstractFsTag extends AbstractTag implements FileAwareTag {

   

    
	protected List<RenamableFile> filesCtx = List.of();

    protected AbstractFsTag(I18n i18n, Object... args) {
        super(i18n, args);
        this.type = TagType.FILE_SYSTEM;
    }

    @Override
    public void setFilesContext(List<RenamableFile> files) {
        this.filesCtx = (files == null) ? List.of() : files;
    }

    protected String fallbackToFileName(RenamableFile rf) {
        // rf.getSource() è un File
        String name = rf.getSource().getName();
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            name = name.substring(0, dot);
        }
        return name.trim();
    }
    
    /** Utility per scrivere FileTime */
    protected void writeTime(RenamableFile rf, String attribute, FileTime time) {
        try {
            Files.setAttribute(rf.getSource().toPath(), attribute, time);
        } catch (Exception e) {
            System.err.println("[FS-WriteTag] Cannot write attribute: " + attribute + " → " + e.getMessage());
        }
    }
}
    
 