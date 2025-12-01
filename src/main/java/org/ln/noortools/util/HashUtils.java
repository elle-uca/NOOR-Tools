package org.ln.noortools.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.zip.CRC32;

import org.ln.noortools.enums.ChecksumAlg;

/**
 * Utility class that provides file-based checksum operations.
 *
 * This calculator supports:
 *   - CRC32
 *   - MD5
 *   - SHA-256
 *
 * All returned digests are lowercase hexadecimal strings.
 */
public final class HashUtils {

    private static final int BUFFER = 8192;

    private HashUtils() {
        // Utility class, no instances allowed
    }

    /**
     * Computes CRC32 checksum for the given file.
     */
    public static String crc32(Path path) throws Exception {
        CRC32 crc = new CRC32();
        try (InputStream in = Files.newInputStream(path)) {
            byte[] buf = new byte[BUFFER];
            int n;
            while ((n = in.read(buf)) > 0) {
                crc.update(buf, 0, n);
            }
        }
        return Long.toHexString(crc.getValue());
    }

    /**
     * Computes a message digest using the specified algorithm (MD5, SHA-256).
     */
    public static String digest(Path path, String algo) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algo);
        try (InputStream in = Files.newInputStream(path)) {
            // Discard output, only feed bytes to MessageDigest
            in.transferTo(new DigestOutputStream(OutputStream.nullOutputStream(), md));
        }
        return HexFormat.of().formatHex(md.digest());
    }

    /**
     * Generic compute function based on ChecksumAlg enum.
     */
    public static String compute(Path path, ChecksumAlg alg) throws Exception {
        return switch (alg) {
            case CRC32 -> crc32(path);
            case MD5   -> digest(path, "MD5");
            case SHA256 -> digest(path, "SHA-256");
		default -> throw new IllegalArgumentException("Unexpected value: " + alg);
        };
    }
}
