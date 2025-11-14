package org.ln.noortools;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.ln.noortools.util.AudioUtil;

public class TagWriter {

    public static void writeTags(String filePath, String newArtist, String newAlbum, String newTitle) {
        File audioFile = new File(filePath);

        try {
            // 1. Carica il file audio usando Jaudiotagger
            AudioFile f = AudioFileIO.read(audioFile);
            
            // 2. Ottieni l'oggetto Tag per la modifica
            //Tag tag = f.getTag();
            Tag tag = AudioUtil.ensureTag(f);
            
            if (tag == null) {
                // Se non ci sono tag, ne crea uno nuovo (dipende dal formato del file)
                System.out.println("Nessun tag esistente trovato. Verrà creato un nuovo tag.");
                tag = f.createDefaultTag();
            }
//
//            System.out.println("Album    "+tag.getFirst(org.jaudiotagger.tag.FieldKey.ALBUM));
//            System.out.println("Artist    "+tag.getFirst(org.jaudiotagger.tag.FieldKey.ARTIST));
//            System.out.println("Title    "+tag.getFirst(org.jaudiotagger.tag.FieldKey.TITLE));

            
            // 3. Sovrascrivi i campi desiderati
            // FieldKey è un'enum che definisce tutti i tag standard (Artista, Album, ecc.)
            
            tag.deleteField(FieldKey.ARTIST);
            tag.deleteField(FieldKey.ALBUM);
            tag.deleteField(FieldKey.TITLE);
            
            tag.setField(FieldKey.ARTIST, newArtist);
            tag.setField(FieldKey.ALBUM, newAlbum);
            tag.setField(FieldKey.TITLE, newTitle);
            
            // Esempio per il commento
            tag.setField(FieldKey.COMMENT, "Tag scritto da Jaudiotagger in data odierna.");
            
            // 4. Salva le modifiche sul file
            AudioFileIO.write(f);

            System.out.println("Tag scritti e salvati con successo per il file: " + audioFile.getName());
            
        } catch (Exception e) {
            System.err.println("Errore durante la scrittura dei tag: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
    public static void readTags(String filePath) {
        File audioFile = new File(filePath);

        try {
            // 1. Carica il file audio usando Jaudiotagger
            AudioFile f = AudioFileIO.read(audioFile);
            
            // 2. Ottieni l'oggetto Tag per la modifica
            Tag tag = f.getTag();
            
            if (tag == null) {
                // Se non ci sono tag, ne crea uno nuovo (dipende dal formato del file)
                System.out.println("Nessun tag esistente trovato. Verrà creato un nuovo tag.");
                tag = f.createDefaultTag();
            }

            System.out.println("Album    "+tag.getFirst(org.jaudiotagger.tag.FieldKey.ALBUM));
            System.out.println("Artist    "+tag.getFirst(org.jaudiotagger.tag.FieldKey.ARTIST));
            System.out.println("Title    "+tag.getFirst(org.jaudiotagger.tag.FieldKey.TITLE));

            
            
        } catch (Exception e) {
            System.err.println("Errore durante la lettura dei tag: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
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
                System.out.println("[AudioUtil] Created ID3 tag for MP3");
            }
            else if (ext.endsWith(".flac")) {
            	newTag = audio.getTagOrCreateAndSetDefault();
                audio.setTag(newTag);
                System.out.println("[AudioUtil] Created FLAC tag");
            }
            else if (ext.endsWith(".mp4") || ext.endsWith(".m4a") || ext.endsWith(".m4b")) {
                newTag = new Mp4Tag();
                audio.setTag(newTag);
                System.out.println("[AudioUtil] Created MP4/M4A tag");
            }
            else if (ext.endsWith(".wav")) {
                newTag = new WavTag();
                audio.setTag(newTag);
                System.out.println("[AudioUtil] Created WAV tag");
            }
            else {
                // Formato non gestito: niente crash, solo avviso
                System.err.println("[AudioUtil] Unsupported format for tag creation: " + ext);
                return null;
            }

            return newTag;

        } catch (Exception e) {
            System.err.println("[AudioUtil] Failed to create tag for " + audio.getFile().getName() + ": " + e.getMessage());
            return null;
        }
    }
    
    public static void main(String[] args) {
        //File root = new File("/home/luke/Musica/test");
        File dir = new  File("/home/luke/Musica/test");

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
            	readTags(file.getAbsolutePath());
            	writeTags(
            			file.getAbsolutePath(), 
                        "Nuovo Artista Test", 
                        "Album Aggiornato", 
                        "Titolo Fresco"
                    );
            	readTags(file.getAbsolutePath());
            }
		}
    	
//    	// PERCORSO ESEMPIO: SOSTITUISCI CON IL TUO FILE REALE
//        String myAudioFilePath = "/home/luke/Musica/test/Lou Dalfin-Lou Pal-Pippo.mp3"; 
//        readTags(myAudioFilePath);
//        writeTags(
//            myAudioFilePath, 
//            "Nuovo Artista Test", 
//            "Album Aggiornato", 
//            "Titolo Fresco"
//        );
//        readTags(myAudioFilePath);
    }
}