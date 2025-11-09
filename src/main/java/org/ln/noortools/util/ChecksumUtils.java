package org.ln.noortools.util;

import java.nio.file.Files;
import java.nio.file.Path;

//public class ChecksumUtils {
//
//    private static String digestHex(Path path, String alg) throws Exception {
//        var md = java.security.MessageDigest.getInstance(alg);
//        try (var in = Files.newInputStream(path)) {
//            byte[] buffer = new byte[8192];
//            int n;
//            while ((n = in.read(buffer)) > 0) md.update(buffer, 0, n);
//        }
//        byte[] bytes = md.digest();
//        var sb = new StringBuilder(bytes.length * 2);
//        for (byte b : bytes) sb.append(String.format("%02x", b));
//        return sb.toString();
//    }
//
//    public static String compute(Path path, ChecksumAlg alg) throws Exception {
//        return switch (alg) {
//            case SHA256 -> digestHex(path, "SHA-256");
//            case MD5 -> digestHex(path, "MD5");
//            case CRC32 -> {
//                var crc = new java.util.zip.CRC32();
//                try (var in = Files.newInputStream(path)) {
//                    byte[] buffer = new byte[8192];
//                    int n;
//                    while ((n = in.read(buffer)) > 0) crc.update(buffer, 0, n);
//                }
//                yield String.format("%08x", crc.getValue());
//            }
//        };
//    }
//}
