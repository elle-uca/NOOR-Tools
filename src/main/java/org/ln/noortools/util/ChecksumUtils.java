package org.ln.noortools.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.zip.CRC32;

import org.ln.noortools.enums.ChecksumAlg;

public class ChecksumUtils {

	
	
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
