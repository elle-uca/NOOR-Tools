package org.ln.noor.tools.rename.tag;

import java.util.List;

import org.ln.noor.core.i18n.I18n;
import org.ln.noor.core.model.RenamableFile;
import org.ln.noor.service.FileAwareTag;

/**
 * Base class for tags that need to read metadata from audio files.
 * It stores the current file context and offers a filename fallback
 * so subclasses only have to focus on extracting specific fields.
 */
public abstract class AbstractAudioTag extends AbstractTag implements FileAwareTag {

   

    
	protected List<RenamableFile> filesCtx = List.of();

    protected AbstractAudioTag(I18n i18n, Object... args) {
        super(i18n, args);
        this.type = TagType.AUDIO;
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
}
    
 