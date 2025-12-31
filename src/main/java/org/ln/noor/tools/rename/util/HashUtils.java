package org.ln.noor.tools.rename.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.zip.CRC32;

import org.ln.noor.core.enums.ChecksumAlg;

/**
 * Utility class that provides file-based checksum operations.
 *
 * Supported algorithms:
 *   - CRC32
 *   - MD5
 *   - SHA-1
 *   - SHA-256
 *   - SHA-512
 *   - SHA3-256
 *   - SHA3-512
 *   - BLAKE2B-256 (Java 21 native)
 *
 * @author Luca Noale
 */
public final class HashUtils {

    private static final int BUFFER = 8192;

    private HashUtils() {}

    /** CRC32 checksum */
    public static String crc32(Path path) throws Exception {
        CRC32 crc = new CRC32();
        try (InputStream in = Files.newInputStream(path)) {
            byte[] buf = new byte[BUFFER];
            int n;
            while ((n = in.read(buf)) > 0)
                crc.update(buf, 0, n);
        }
        return Long.toHexString(crc.getValue());
    }

    /** Generic digest calculator */
    public static String digest(Path path, String algo) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algo);
        try (InputStream in = Files.newInputStream(path)) {
            in.transferTo(new DigestOutputStream(OutputStream.nullOutputStream(), md));
        }
        return HexFormat.of().formatHex(md.digest());
    }

    /** Main dispatcher */
    public static String compute(Path path, ChecksumAlg alg) throws Exception {
        return switch (alg) {
            case CRC32       -> crc32(path);
            case MD5         -> digest(path, "MD5");
            case SHA1        -> digest(path, "SHA-1");
            case SHA256      -> digest(path, "SHA-256");
            case SHA512      -> digest(path, "SHA-512");
            case SHA3_256    -> digest(path, "SHA3-256");
            case SHA3_512    -> digest(path, "SHA3-512");
            case BLAKE2B_256 -> digest(path, "BLAKE2B-256");
        };
    }
}
