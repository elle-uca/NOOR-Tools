package org.ln.noortools.tag;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public class WriteAlbum extends AbstractAudioTag {

    public WriteAlbum(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "WriteAlbum";
       // this.type = TagType.AUDIO;
    }

    @Override
    public void init() {
        // Parametro obbligatorio: <WriteAlbum:NuovoAlbum>
        String newAlbum = getStringArg(0, "").trim();
        if (newAlbum.isEmpty()) return;

        for (RenamableFile rf : filesCtx) {
            try {
                AudioFile audio = AudioFileIO.read(rf.getSource());
                Tag tag = audio.getTag();

                if (tag == null) {
                    tag = audio.createDefaultTag();
                    audio.setTag(tag);
                }

                tag.setField(FieldKey.ALBUM, newAlbum);
                audio.commit();

            } catch (Exception e) {
                // Log senza rompere il flusso
                System.err.println("[WriteAlbum] Cannot update tag: " + rf.getSource() + " -> " + e.getMessage());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Writes a new Album tag into the audio file";
    }
}
