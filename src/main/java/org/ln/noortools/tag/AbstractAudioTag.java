// org.ln.noortools.tag.AbstractChecksumTag
package org.ln.noortools.tag;

import java.util.List;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public abstract class AbstractAudioTag extends AbstractTag implements FileAwareTag {

   

    
	protected List<RenamableFile> filesCtx = List.of();
    //private static final Map<String, String> checksumCache = new ConcurrentHashMap<>();

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
    
 