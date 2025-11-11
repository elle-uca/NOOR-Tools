package org.ln.noortools.util;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.ln.noortools.enums.AudioTagType;

public class AudioUtil {
	
	private static final Set<String> AUDIO_EXT = Set.of(
	        "mp3","flac","ogg","opus","wav","aiff","aif","m4a","aac","wma","ape","dsf","dff"
	);


	public static String getAudioTag(File audioFile, AudioTagType type) {
		
		 if (audioFile == null || !audioFile.isFile()) return "";
		 
		 String name = audioFile.getName().toLowerCase();
		    int dot = name.lastIndexOf('.');
		    if (dot == -1) return "";  // no estensione
		    String ext = name.substring(dot + 1);

		    // ✅ Se NON è un file audio → restituisci ""
		    if (!AUDIO_EXT.contains(ext)) return "";

		 
		AudioFile audio = null;
		try {
			audio = AudioFileIO.read(audioFile);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tag tag = audio.getTag();

		String result = null; 
		switch (type) {

		case ALBUM -> result = tag.getFirst(FieldKey.ALBUM);
		case ARTIST -> result = tag.getFirst(FieldKey.ARTIST);
		case TITLE -> result = tag.getFirst(FieldKey.TITLE);
		default -> result = "UNKNOW";
		}
		//String album   = tag.getFirst(FieldKey.ALBUM);
		return result;

	}


}
