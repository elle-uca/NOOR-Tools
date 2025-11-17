package org.ln.noortools.tag;

import org.jaudiotagger.tag.FieldKey;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.ActionTag;
import org.ln.noortools.util.AudioUtil;

public class WriteArtist extends AbstractAudioTag implements ActionTag {

    public WriteArtist(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "WriteArtist";
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
    		AudioUtil.writeTag(rf, FieldKey.ARTIST, newName);
    	}
    }
//    @Override
//    public void performAction() {
//        String newArtist = getStringArg(0, "").trim();
//        if (newArtist.isEmpty()) return;
//
//        for (RenamableFile rf : filesCtx) {
//            try {
//                AudioFile audio = AudioFileIO.read(rf.getSource());
//                Tag tag = audio.getTag();
//
//                if (tag == null) {
//                    tag = audio.createDefaultTag();
//                    audio.setTag(tag);
//                }
//
//                tag.setField(FieldKey.ARTIST, newArtist);
//                audio.commit();
//
//                System.out.println("[WriteArtist] ALBUM set to '" + newArtist + "' for " + rf.getSource().getName());
//
//            } catch (Exception e) {
//                System.err.println("[WriteArtist] Cannot write tag for "
//                        + rf.getSource() + " → " + e.getMessage());
//            }
//        }
//    }
    

    @Override
    public String getDescription() {
        return "Writes a new Artist tag into the audio file";
    }

	@Override
	public String getActionDescription() {
		return "Writes a new Artist tag into the audio file";
	}
}
