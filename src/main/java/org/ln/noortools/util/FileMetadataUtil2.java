package org.ln.noortools.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class FileMetadataUtil2 {

    /** Utility: converte LocalDateTime → FileTime */
    private static FileTime toFileTime(LocalDateTime dt) {
        return FileTime.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }

    // =====================================================================================
    //  📌 CREATION DATE
    // =====================================================================================

    public static boolean setCreationDate(Path path, LocalDateTime dateTime) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(dateTime);

        try {
            FileTime ft = toFileTime(dateTime);

            // Windows / Linux (con supporto)
            BasicFileAttributeView view =
                    Files.getFileAttributeView(path, BasicFileAttributeView.class);

            if (view != null) {
                view.setTimes(null, null, ft);   // modify = null, access = null, creation = ft
                return true;
            }

            return false;

        } catch (IOException e) {
            System.err.println("[FileMetadataUtil] Cannot set creation date: " + e.getMessage());
            return false;
        }
    }

    // =====================================================================================
    //  📌 MODIFICATION DATE
    // =====================================================================================

    public static boolean setModificationDate(Path path, LocalDateTime dateTime) {
        try {
            FileTime ft = toFileTime(dateTime);
            Files.setLastModifiedTime(path, ft);
            return true;
        } catch (IOException e) {
            System.err.println("[FileMetadataUtil] Cannot set modify date: " + e.getMessage());
            return false;
        }
    }

    // =====================================================================================
    //  📌 ACCESS DATE
    // =====================================================================================

    public static boolean setAccessDate(Path path, LocalDateTime dateTime) {
        try {
            FileTime ft = toFileTime(dateTime);

            BasicFileAttributeView view =
                    Files.getFileAttributeView(path, BasicFileAttributeView.class);

            if (view != null) {
                view.setTimes(null, ft, null);  // modify=null, access=ft, creation=null
                return true;
            }

            return false;

        } catch (IOException e) {
            System.err.println("[FileMetadataUtil] Cannot set access date: " + e.getMessage());
            return false;
        }
    }

    // =====================================================================================
    //  📌 OWNER
    // =====================================================================================

    public static boolean setOwner(Path path, String ownerName) {
        try {
            UserPrincipalLookupService lookup =
                    path.getFileSystem().getUserPrincipalLookupService();

            UserPrincipal owner = lookup.lookupPrincipalByName(ownerName);

            Files.setOwner(path, owner);
            return true;

        } catch (IOException e) {
            System.err.println("[FileMetadataUtil] Cannot set owner: " + e.getMessage());
            return false;
        }
    }

    // =====================================================================================
    //  📌 LETTURA ATTRIBUTI (per i tag normali)
    // =====================================================================================

    public static LocalDateTime getCreationDate(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            return LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            return null;
        }
    }

    public static LocalDateTime getModificationDate(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            return LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(),
                    ZoneId.systemDefault());
        } catch (IOException e) {
            return null;
        }
    }

    public static LocalDateTime getAccessDate(Path path) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            return LocalDateTime.ofInstant(attrs.lastAccessTime().toInstant(),
                    ZoneId.systemDefault());
        } catch (IOException e) {
            return null;
        }
    }

    public static String getOwner(Path path) {
        try {
            return Files.getOwner(path).getName();
        } catch (IOException e) {
            return null;
        }
    }
}
