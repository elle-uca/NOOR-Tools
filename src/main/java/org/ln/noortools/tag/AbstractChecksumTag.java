package org.ln.noortools.tag;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;

import org.ln.noortools.enums.ChecksumAlg;
import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.FileAwareTag;

/**
 * Base class for all checksum-based tags (CRC32, MD5, SHA256).
 *
 * This class:
 *  - Computes a checksum for each file in the context.
 *  - Uses a static ConcurrentHashMap as a cache, so each
 *    file+algorithm pair is computed only once.
 *  - Supports optional prefix length (e.g. <Md5:8>).
 *
 * Subclasses only set the algorithm and description.
 */
public abstract class AbstractChecksumTag extends AbstractTag implements FileAwareTag {

   
    /** Selected algorithm for this tag instance. */
    private final ChecksumAlg algorithm;

    /** Files provided by the renamer context. */
    private List<RenamableFile> filesCtx = List.of();

    /**
     * Global cache for checksums:
     * key = absolutePath|length|lastModified|algorithm
     * value = hex checksum
     */
    private static final Map<String, String> checksumCache = new ConcurrentHashMap<>();

    protected AbstractChecksumTag(I18n i18n, ChecksumAlg alg, Object... args) {
        super(i18n, args);
        this.algorithm = alg;
        this.type = TagType.CHECKSUM;
    }

    @Override
    public void setFilesContext(List<RenamableFile> files) {
        this.filesCtx = (files == null) ? List.of() : files;
    }

    /**
     * Main checksum computation logic.
     * Uses cache when possible, falling back to real hashing otherwise.
     */
    @Override
    public void init() {
        this.newNames = new ArrayList<>(filesCtx.size());

        int cut = getIntArg(0, 0); // e.g. <Md5:8> → truncate to first 8 chars

        for (RenamableFile rf : filesCtx) {

            String key = cacheKey(rf);
            String result = checksumCache.get(key);

            if (result == null) {
                // Compute checksum and store in cache
                try (InputStream in = Files.newInputStream(rf.getSource().toPath())) {

                    result = switch (algorithm) {
                        case CRC32 -> computeCRC32(in);
                        case MD5 -> computeDigest("MD5", in);
                        case SHA256 -> computeDigest("SHA-256", in);
					default -> throw new IllegalArgumentException("Unexpected value: " + algorithm);
                    };

                    checksumCache.put(key, result);

                } catch (Exception e) {
                    // On failure, add empty string
                    newNames.add("");
                    continue;
                }
            }

            // Apply cutoff if needed
            if (cut > 0 && cut < result.length()) {
                result = result.substring(0, cut);
            }

            newNames.add(result);
        }
    }

    @Override
    public String getNewName(int index) {
        if (newNames == null || index >= newNames.size())
            return "";
        return newNames.get(index);
    }

    /**
     * Computes a CRC32 checksum in hexadecimal.
     */
    private String computeCRC32(InputStream in) throws Exception {
        CRC32 crc = new CRC32();
        byte[] buffer = new byte[8192];
        int n;
        while ((n = in.read(buffer)) > 0) {
            crc.update(buffer, 0, n);
        }
        return Long.toHexString(crc.getValue());
    }

    /**
     * Computes a digest using the given algorithm (MD5, SHA-256).
     */
    private String computeDigest(String alg, InputStream in) throws Exception {
        MessageDigest md = MessageDigest.getInstance(alg);
        in.transferTo(new java.security.DigestOutputStream(OutputStream.nullOutputStream(), md));
        return HexFormat.of().formatHex(md.digest());
    }

    /**
     * Cache key for the given file.
     * Ensures recomputation only when file content changes.
     */
    private String cacheKey(RenamableFile rf) {
        var f = rf.getSource();
        return f.getAbsolutePath() + "|" + f.length() + "|" + f.lastModified() + "|" + algorithm;
    }
}
