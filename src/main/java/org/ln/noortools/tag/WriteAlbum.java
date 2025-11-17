package org.ln.noortools.tag;

import org.jaudiotagger.tag.FieldKey;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.ActionTag;
import org.ln.noortools.util.AudioUtil;

public class WriteAlbum extends AbstractAudioTag implements ActionTag {

    public WriteAlbum(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "WriteAlbum";
    }
    
    @Override
    public void init() {
        newClear();

        String newName = getStringArg(0, "").trim();
        if (newName.isEmpty()) newName = "Unknown";

        // Solo anteprima → genera la stringa di rename
        for (int i = 0; i < oldSize(); i++) {
        	newAdd(newName);
        }
    }

    /**
     * Fase di AZIONE REALE (scrive davvero sul file)
     * Chiamata dopo che l’utente ha premuto "Rinomina".
     */
    @Override
    public void performAction() {
    	String newName = getStringArg(0, "").trim();
    	if (newName.isEmpty()) newName = "Unknown";

    	for (RenamableFile rf : filesCtx) {
    		AudioUtil.writeTag(rf, FieldKey.ALBUM, newName);
    	}
    }

    
    


    @Override
    public String getDescription() {
        return "Writes a new Album tag into the audio file";
    }

	@Override
	public String getActionDescription() {
		return "Writes a new Album tag into the audio file";
	}
}
