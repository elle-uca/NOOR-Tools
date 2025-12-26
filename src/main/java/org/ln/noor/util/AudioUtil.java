package org.ln.noor.util;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.ln.noor.core.enums.AudioTagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioUtil {

	private static final Logger logger = LoggerFactory.getLogger(AudioUtil.class);

	private static final AudioFileFilter AUDIO_FILTER = new AudioFileFilter();


	public static boolean isAudioFile(File file) {
	    return AUDIO_FILTER.accept(file);
	}



    /**
     * Garantisce che l'audio abbia un tag valido e compatibile col formato.
     * Se non esiste, ne crea uno nuovo del tipo corretto (ID3, FLAC, MP4, WAV, ecc.).
     */
    public static Tag ensureTag(AudioFile audio) {
        if (audio == null) return null;

        Tag tag = audio.getTag();
        if (tag != null) return tag;

        try {
            String ext = audio.getFile().getName().toLowerCase();
            Tag newTag = null;

            if (audio instanceof MP3File mp3) {
                newTag = mp3.createDefaultTag();
                mp3.setTag(newTag);
                logger.debug("[AudioUtil] Created ID3 tag for MP3");
            }
            else if (ext.endsWith(".flac")) {
            	newTag = audio.getTagOrCreateAndSetDefault();
                audio.setTag(newTag);
                logger.debug("[AudioUtil] Created FLAC tag");
            }
            else if (ext.endsWith(".mp4") || ext.endsWith(".m4a") || ext.endsWith(".m4b")) {
                newTag = new Mp4Tag();
                audio.setTag(newTag);
                logger.debug("[AudioUtil] Created MP4/M4A tag");
            }
            else if (ext.endsWith(".wav")) {
                newTag = new WavTag();
                audio.setTag(newTag);
                logger.debug("[AudioUtil] Created WAV tag");
            }
            else {
                // Formato non gestito: niente crash, solo avviso
                logger.error("[AudioUtil] Unsupported format for tag creation: {}", ext);
                return null;
            }

            return newTag;

        } catch (Exception e) {
            logger.error("[AudioUtil] Failed to create tag for {}", audio.getFile().getName(), e);
            return null;
        }
    }

    public static String getAudioTag(File file, AudioTagType type) {
    	if(!isAudioFile(file))return null;
        try {
            AudioFile audio = AudioFileIO.read(file);
            if (audio == null) return null;

            Tag tag = audio.getTag();
            if (tag == null) return null;

            return switch (type) {
                case TITLE -> tag.getFirst(org.jaudiotagger.tag.FieldKey.TITLE);
                case ARTIST -> tag.getFirst(org.jaudiotagger.tag.FieldKey.ARTIST);
                case ALBUM -> tag.getFirst(org.jaudiotagger.tag.FieldKey.ALBUM);
                case GENRE -> tag.getFirst(org.jaudiotagger.tag.FieldKey.GENRE);
                case YEAR -> tag.getFirst(org.jaudiotagger.tag.FieldKey.YEAR);
                default -> null;
            };
        } catch (Exception e) {
            logger.error("[AudioUtil] Cannot read audio tag from: {}", file, e);
            return null;
        }
    }
    

}




