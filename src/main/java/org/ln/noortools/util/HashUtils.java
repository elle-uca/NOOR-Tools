package org.ln.noortools.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import org.ln.noortools.enums.ChecksumAlg;

public final class HashUtils {

    public static String crc32(Path path) throws IOException {
        CRC32 crc = new CRC32();
        try (InputStream in = Files.newInputStream(path)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                crc.update(buf, 0, n);
            }
        }
        return Long.toHexString(crc.getValue()).toUpperCase();
    }

    public static String digest(Path path, String algo) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algo);
        try (InputStream in = Files.newInputStream(path)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                md.update(buf, 0, n);
            }
        }
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString().toUpperCase();
    }
    
	
    private static String digestHex(Path path, String alg) throws Exception {
        MessageDigest md = MessageDigest.getInstance(alg);
        try (var in = Files.newInputStream(path)) {
            byte[] buffer = new byte[8096];
            int read;
            while ((read = in.read(buffer)) > 0) {
                md.update(buffer, 0, read);
            }
        }
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String compute(Path path, ChecksumAlg alg) throws Exception {
        return switch (alg) {
            case SHA256 -> digestHex(path, "SHA-256");
            case MD5 -> digestHex(path, "MD5");
            case CRC32 -> {
                CRC32 crc = new CRC32();
                try (var in = Files.newInputStream(path)) {
                    byte[] buffer = new byte[8096];
                    int read;
                    while ((read = in.read(buffer)) > 0)
                        crc.update(buffer, 0, read);
                }
                yield String.format("%08x", crc.getValue());
            }
        };
    }

}

