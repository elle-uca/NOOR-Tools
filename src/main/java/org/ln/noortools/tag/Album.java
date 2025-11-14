package org.ln.noortools.tag;

import org.ln.noortools.enums.AudioTagType;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.AudioUtil;

/**
 * Tag <Album>
 * 

 *   
 *    Author: Luca Noale
 */
public class Album extends AbstractAudioTag {

    public Album(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "Album";
        
    }

    @Override
    public void init() {
        newClear();
        for (RenamableFile rf : filesCtx) {
            String value = AudioUtil.getAudioTag(rf.getSource(), AudioTagType.ALBUM);
            if (value == null || value.isBlank()) {
            	value = fallbackToFileName(rf);
            }
           newAdd(value.trim());
        }
      }

    @Override
    public String getDescription() {
    	return i18n.get("tag.album.description");
    }
}

