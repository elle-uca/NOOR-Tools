package org.ln.noor.tools.rename.tag;

import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.List;

import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.service.FileAwareTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for file-system related tags (creation date, owner, etc.).
 * It exposes helpers to normalize the files context and to safely work
 * with metadata even on platforms that may lack certain attributes.
 *
 * @author Luca Noale
 */
public abstract class AbstractFsTag extends AbstractTag implements FileAwareTag {


        private static final Logger logger = LoggerFactory.getLogger(AbstractFsTag.class);

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
    
    protected LocalDateTime parseDateTime(String arg) {
        // TODO: parse formati supportati
        return LocalDateTime.parse(arg); // placeholder
    }
    
    /** Utility per scrivere FileTime */
    protected void writeTime(RenamableFile rf, String attribute, FileTime time) {
        try {
            Files.setAttribute(rf.getSource().toPath(), attribute, time);
        } catch (Exception e) {
            logger.error("[FS-WriteTag] Cannot write attribute: {}", attribute, e);
        }
    }
}
    
 
