package org.ln.noortools.util;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.ln.noortools.enums.AudioTagType;
import org.ln.noortools.model.RenamableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioUtil {

    private static final Logger logger = LoggerFactory.getLogger(AudioUtil.class);
	
//	private static final Set<String> AUDIO_EXT = Set.of(
//    "mp3","flac","ogg","opus","wav","aiff","aif","m4a","aac","wma","ape","dsf","dff"
//);
	

	public static AudioFile read(File file) {
        try {
            return AudioFileIO.read(file);
        } catch (Exception e) {
            logger.error("[AudioUtil] Cannot read file: {}", file.getAbsolutePath(), e);
            return null;
        }
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
    


    public static void writeTag(RenamableFile file, FieldKey key, String newName) {
             try {
                AudioFile audio = AudioUtil.read(file.getSource());

                // Garantisce che ci sia un tag del tipo corretto
                Tag tag = AudioUtil.ensureTag(audio);
                if (tag == null) {
                	tag = audio.getTagOrCreateAndSetDefault();
                }
                
               // forza la sovrascrittura
                tag.deleteField(key);
                tag.setField(key, newName);
                tag.setField(FieldKey.COMMENT, "Tag scritto da NOOR Tools ");
                
             // Scrive fisicamente sul file
                AudioFileIO.write(audio);
                logger.info("Tags written and saved successfully for file: {}", file.getSource().getName());
            } catch (Exception e) {
                logger.error("[WriteTag] Cannot update tag: {}", file.getSource(), e);
            }
            
    }

}









//package org.ln.noortools.util;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Set;
//
//import org.jaudiotagger.audio.AudioFile;
//import org.jaudiotagger.audio.AudioFileIO;
//import org.jaudiotagger.audio.exceptions.CannotReadException;
//import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
//import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
//import org.jaudiotagger.tag.FieldKey;
//import org.jaudiotagger.tag.Tag;
//import org.jaudiotagger.tag.TagException;
//import org.ln.noortools.enums.AudioTagType;
//
//public class AudioUtil {
//	
//	private static final Set<String> AUDIO_EXT = Set.of(
//	        "mp3","flac","ogg","opus","wav","aiff","aif","m4a","aac","wma","ape","dsf","dff"
//	);
//
//	 /**
//     * Restituisce un AudioFile valido o null se non leggibile.
//     */
//    public static AudioFile read(File file) {
//        try {
//            return AudioFileIO.read(file);
//        } catch (Exception e) {
//            System.err.println("[AudioUtil] Cannot read file: " + file + " → " + e.getMessage());
//            return null;
//        }
//    }
//
//    /**
//     * Restituisce il Tag associato all'audio.
//     * Se non esiste, ne crea uno nuovo compatibile col formato.
//     */
//    public static Tag ensureTag(AudioFile audio) {
//        if (audio == null) return null;
//
//        Tag tag = audio.getTag();
//        if (tag == null) {
//            try {
//                tag = audio.createDefaultTag();
//                audio.setTag(tag);
//                System.out.println("[AudioUtil] Created new default tag for " + audio.getFile().getName());
//            } catch (Exception e) {
//                System.err.println("[AudioUtil] Cannot create default tag: " + e.getMessage());
//                return null;
//            }
//        }
//        return tag;
//    }
//
//    /**
//     * Ritorna il valore di un tag specifico (se presente).
//     */
//    public static String getAudioTag(File file, AudioTagType type) {
//        try {
//            AudioFile audio = AudioFileIO.read(file);
//            if (audio == null) return null;
//
//            Tag tag = audio.getTag();
//            if (tag == null) return null;
//
//            return switch (type) {
//                case TITLE -> tag.getFirst(org.jaudiotagger.tag.FieldKey.TITLE);
//                case ARTIST -> tag.getFirst(org.jaudiotagger.tag.FieldKey.ARTIST);
//                case ALBUM -> tag.getFirst(org.jaudiotagger.tag.FieldKey.ALBUM);
////                case GENRE -> tag.getFirst(org.jaudiotagger.tag.FieldKey.GENRE);
////                case YEAR -> tag.getFirst(org.jaudiotagger.tag.FieldKey.YEAR);
//                default -> null;
//            };
//        } catch (Exception e) {
//            logger.error("[AudioUtil] Cannot read audio tag from: {}", file, e);
//            return null;
//        }
//    }
//}
	
	
	

//	public static String getAudioTag(File audioFile, AudioTagType type) {
//		
//		 if (audioFile == null || !audioFile.isFile()) return "";
//		 
//		 String name = audioFile.getName().toLowerCase();
//		    int dot = name.lastIndexOf('.');
//		    if (dot == -1) return "";  // no estensione
//		    String ext = name.substring(dot + 1);
//
//		    // ✅ Se NON è un file audio → restituisci ""
//		    if (!AUDIO_EXT.contains(ext)) return "";
//
//		 
//		AudioFile audio = null;
//		try {
//
//			audio = AudioFileIO.read(audioFile);
//	        if (audio == null) {
//	            System.err.println("[AudioUtil] Cannot read audio file: " + audioFile.getAbsolutePath());
//	            return null;
//	        }
//			
//			
//		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
//				| InvalidAudioFrameException e) {
//			System.err.println("[AudioUtil] Cannot read audio tag from: " + audioFile + " – " + e.getMessage());
//	        return ""; //
//		}
//		Tag tag = audio.getTag();
//		
//       //Tag tag = audio.getTag();
//        if (tag == null) {
//            // niente tag, file "vergine"
//            System.err.println("[AudioUtil] No tag found in: " + audioFile.getName());
//            return null;
//        }
//		
//
//		String result = null;
//		
//		switch (type) {
//		case ALBUM -> result = tag.getFirst(FieldKey.ALBUM);
//		case ARTIST -> result = tag.getFirst(FieldKey.ARTIST);
//		case TITLE -> result = tag.getFirst(FieldKey.TITLE);
//		default -> result = "UNKNOW";
//		}
//		
//		return result;
//
//	}



