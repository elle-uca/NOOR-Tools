package org.ln.noor.tools.rename.tag;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ln.noor.core.enums.ChecksumAlg;
import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.service.FileAwareTag;
import org.ln.noor.tools.rename.util.HashUtils;

/**
 * Base class for all checksum-based tags.
 *
 * Features:
 *  - Centralized caching to avoid recomputing hashes.
 *  - Uses HashUtils for unified hashing logic.
 *  - Supports substring truncation (e.g., <Md5:8>).
 */
public abstract class AbstractChecksumTag extends AbstractTag implements FileAwareTag {


    private final ChecksumAlg algorithm;
    private List<RenamableFile> filesCtx = List.of();

    /**
     * Cache key → computed checksum
     *
     * The key includes:
     *   - absolute path
     *   - file length
     *   - last modified timestamp
     *   - algorithm
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

    @Override
    public void init() {
        newClear();

        int cut = getIntArg(0, 0); // e.g. <Md5:8> → first 8 chars

        for (RenamableFile rf : filesCtx) {
            String key = cacheKey(rf);

            // Check cache first
            String result = checksumCache.get(key);

            if (result == null) {
                try {
                    result = HashUtils.compute(rf.getSource().toPath(), algorithm);
                    checksumCache.put(key, result);
                } catch (Exception e) {
                    newAdd("");
                    continue;
                }
            }

            // Apply substring trim
            if (cut > 0 && cut < result.length())
                result = result.substring(0, cut);

            newAdd(result);
        }
    }

    @Override
    public String getNewName(int index) {
        if (newNames == null || index >= newNames.size())
            return "";
        return newNames.get(index);
    }

    /**
     * Builds a cache key based on file identity and selected algorithm.
     */
    private String cacheKey(RenamableFile rf) {
        var f = rf.getSource();
        return f.getAbsolutePath() + "|" + f.length() + "|" + f.lastModified() + "|" + algorithm;
    }
}
