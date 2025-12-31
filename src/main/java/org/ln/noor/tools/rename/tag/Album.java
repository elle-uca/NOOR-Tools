package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.enums.AudioTagType;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.util.AudioUtil;

/**
 * Tag <Album>
 * 

 *   
 *    Author: Luca Noale
 *
 * @author Luca Noale
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

