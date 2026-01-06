package org.ln.noor.tools.rename.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.ln.noor.core.i18n.I18n;

/**
 * Utility class for reading and writing OS-level file metadata
 * (creation / modification / access dates) and formatting them.
 *
 * It also provides helper methods to format dates according to a
 * user-friendly pattern (e.g. "dd-mm-yy"), mapped to a valid
 * Java DateTimeFormatter pattern.
 *
 * Works on:
 *  - Windows: uses DosFileAttributeView when available
 *  - Linux / macOS: uses BasicFileAttributeView (creationTime may be unsupported)
 *
 * @author Luca Noale
 */
public final class FileMetadataUtil {

    private static final boolean IS_WINDOWS =
            System.getProperty("os.name").toLowerCase().contains("win");

    private FileMetadataUtil() {}

    // =====================================================================================
    //  📌 LETTURA ATTRIBUTI
    // =====================================================================================

    public static LocalDateTime getCreationDate(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            if (attrs.creationTime() == null) return null;
            return LocalDateTime.ofInstant(
                    attrs.creationTime().toInstant(),
                    ZoneId.systemDefault()
            );
        } catch (IOException e) {
            return null;
        }
    }

    public static LocalDateTime getModificationDate(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            if (attrs.lastModifiedTime() == null) return null;
            return LocalDateTime.ofInstant(
                    attrs.lastModifiedTime().toInstant(),
                    ZoneId.systemDefault()
            );
        } catch (IOException e) {
            return null;
        }
    }

    public static LocalDateTime getAccessDate(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            if (attrs.lastAccessTime() == null) return null;
            return LocalDateTime.ofInstant(
                    attrs.lastAccessTime().toInstant(),
                    ZoneId.systemDefault()
            );
        } catch (IOException e) {
            return null;
        }
    }

    // =====================================================================================
    //  🧩 FORMATTING FOR TAGS (read-only)
    // =====================================================================================

    /**
     * Formats a timestamp for display in rename preview tags by mapping user-friendly
     * patterns (for example, "dd-mm-yy") to Java {@link DateTimeFormatter} patterns.
     * This method only operates in memory and does not touch the filesystem.
     *
     * If the pattern is null or blank, a default is used. If the date is null, an empty string is returned.
     */
    public static String formatDate(LocalDateTime dateTime, String userPattern) {
        if (dateTime == null) {
            return "";
        }

        String pattern = (userPattern == null || userPattern.isBlank())
                ? "yyyy-mm-dd HH:nn:ss"   // human-friendly default pattern
                : userPattern;

        // Delegate to the existing pattern mapper used by the <Date> tag.
        String javaPattern = DateTimeFormatMapper.toJavaPattern(pattern);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(javaPattern);
        return dateTime.format(formatter);
    }

    /**
     * Variant preserved for future localization needs; currently delegates to {@link #formatDate(LocalDateTime, String)}.
     */
    public static String formatDate(LocalDateTime dateTime, String userPattern, I18n i18n) {
        return formatDate(dateTime, userPattern); // per ora delega
    }

    // =====================================================================================
    //  ✏️ WRITING ATTRIBUTES (for WriteTag operations)
    // =====================================================================================

    public static void setCreationDate(Path path, LocalDateTime dateTime) throws IOException {
        if (dateTime == null) return;

        FileTime ft = FileTime.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        if (IS_WINDOWS) {
            // On Windows prefer DosFileAttributeView when present to avoid unsupported attribute errors.
            DosFileAttributeView dosView = Files.getFileAttributeView(path, DosFileAttributeView.class);
            if (dosView != null) {
                BasicFileAttributes attrs = dosView.readAttributes();
                FileTime lastModified = attrs.lastModifiedTime();
                FileTime lastAccess   = attrs.lastAccessTime();
                // Apply creation time while preserving last modified and last access values.
                dosView.setTimes(lastModified, lastAccess, ft);
                return;
            }
        }

        // Linux / macOS / fallback
        BasicFileAttributeView basicView =
                Files.getFileAttributeView(path, BasicFileAttributeView.class);
        if (basicView != null) {
            try {
                BasicFileAttributes attrs = basicView.readAttributes();
                FileTime lastModified = attrs.lastModifiedTime();
                FileTime lastAccess   = attrs.lastAccessTime();
                basicView.setTimes(lastModified, lastAccess, ft);
            } catch (UnsupportedOperationException ex) {
                // Some filesystems ignore creation time; swallow and continue.
            }
        }
    }

    public static void setModificationDate(Path path, LocalDateTime dateTime) throws IOException {
        if (dateTime == null) return;

        FileTime ft = FileTime.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        if (IS_WINDOWS) {
            DosFileAttributeView dosView = Files.getFileAttributeView(path, DosFileAttributeView.class);
            if (dosView != null) {
                BasicFileAttributes attrs = dosView.readAttributes();
                FileTime creation   = attrs.creationTime();
                FileTime lastAccess = attrs.lastAccessTime();
                dosView.setTimes(ft, lastAccess, creation);
                return;
            }
        }

        BasicFileAttributeView basicView =
                Files.getFileAttributeView(path, BasicFileAttributeView.class);
        if (basicView != null) {
            BasicFileAttributes attrs = basicView.readAttributes();
            FileTime creation   = attrs.creationTime();
            FileTime lastAccess = attrs.lastAccessTime();
            basicView.setTimes(ft, lastAccess, creation);
        } else {
            // fallback minimale
            Files.setLastModifiedTime(path, ft);
        }
    }

    public static void setAccessDate(Path path, LocalDateTime dateTime) throws IOException {
        if (dateTime == null) return;

        FileTime ft = FileTime.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        if (IS_WINDOWS) {
            DosFileAttributeView dosView = Files.getFileAttributeView(path, DosFileAttributeView.class);
            if (dosView != null) {
                BasicFileAttributes attrs = dosView.readAttributes();
                FileTime creation      = attrs.creationTime();
                FileTime lastModified  = attrs.lastModifiedTime();
                dosView.setTimes(lastModified, ft, creation);
                return;
            }
        }

        BasicFileAttributeView basicView =
                Files.getFileAttributeView(path, BasicFileAttributeView.class);
        if (basicView != null) {
            BasicFileAttributes attrs = basicView.readAttributes();
            FileTime creation     = attrs.creationTime();
            FileTime lastModified = attrs.lastModifiedTime();
            basicView.setTimes(lastModified, ft, creation);
        }
    }
    
    public static void setOwner(Path path, String userName) throws IOException {
        if (userName == null || userName.isBlank()) return;

        UserPrincipalLookupService lookup =
                path.getFileSystem().getUserPrincipalLookupService();

        UserPrincipal newOwner;
        try {
            newOwner = lookup.lookupPrincipalByName(userName);
        } catch (IOException e) {
            // utente non trovato o FS non supporta → come gli altri metodi: ignora
            return;
        }

        // =========================================================================
        //   WINDOWS: tentativo con AclFileAttributeView per compatibilità NTFS
        // =========================================================================
        if (IS_WINDOWS) {
            AclFileAttributeView aclView =
                    Files.getFileAttributeView(path, AclFileAttributeView.class);
            if (aclView != null) {
                try {
                    aclView.setOwner(newOwner);
                    return;
                } catch (UnsupportedOperationException | IOException ignored) {
                    // come gli altri metodi: fallback
                }
            }
        }

        // =========================================================================
        //   Linux / macOS / fallback standard Java
        // =========================================================================
        try {
            Files.setOwner(path, newOwner);
        } catch (UnsupportedOperationException ignored) {
            // FS non supporta owner → stessa filosofia: ignora
        }
    }

}
