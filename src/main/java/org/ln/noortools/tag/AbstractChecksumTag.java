// org.ln.noortools.tag.AbstractChecksumTag
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

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public abstract class AbstractChecksumTag extends AbstractTag implements FileAwareTag {

    protected enum ChecksumAlg { CRC32, MD5, SHA256 }

    private final ChecksumAlg algorithm;
    private List<RenamableFile> filesCtx = List.of();
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
        newNames = new ArrayList<>(filesCtx.size());
        int cut = getIntArg(0, 0); // <Md5:8> → primi 8 caratteri

        for (RenamableFile rf : filesCtx) {
        	String result;
        	String key = cacheKey(rf);
        	
        	result = checksumCache.get(key);
        	if (result == null) {
        	
            try (InputStream in = Files.newInputStream(rf.getSource().toPath())) {

                result = switch (algorithm) {
                    case CRC32 -> computeCRC32(in);
                    case MD5 -> computeDigest("MD5", in);
                    case SHA256 -> computeDigest("SHA-256", in);
                };
                checksumCache.put(key, result);
                if (cut > 0 && cut < result.length())
                    result = result.substring(0, cut);

                newNames.add(result);

            } catch (Exception e) {
                newNames.add("");
            }
        }}
    }

    private String computeCRC32(InputStream in) throws Exception {
        CRC32 crc = new CRC32();
        byte[] buffer = new byte[8192];
        int n;
        while ((n = in.read(buffer)) > 0) {
            crc.update(buffer, 0, n);
        }
        return Long.toHexString(crc.getValue());
    }

    private String computeDigest(String alg, InputStream in) throws Exception {
        MessageDigest md = MessageDigest.getInstance(alg);
        in.transferTo(new java.security.DigestOutputStream(OutputStream.nullOutputStream(), md));
        return HexFormat.of().formatHex(md.digest());
    }

    private String cacheKey(RenamableFile rf) {
        var f = rf.getSource();
        return f.getAbsolutePath() + "|" + f.length() + "|" + f.lastModified() + "|" + algorithm;
    }

}




//package org.ln.noortools.tag;
//
//import java.util.List;
//
//import org.ln.noortools.enums.ChecksumAlg;
//import org.ln.noortools.i18n.I18n;
//import org.ln.noortools.model.RenamableFile;
//import org.ln.noortools.util.ChecksumUtils;
//
//public abstract class AbstractChecksumTag extends AbstractTag implements FileAwareTag {
//
//    protected List<RenamableFile> files;
//    protected final org.ln.noortools.enums.ChecksumAlg alg;
//
//    protected AbstractChecksumTag(I18n i18n, ChecksumAlg alg, Object... args) {
//        super(i18n, args);
//        this.alg = alg;
//        this.type = TagType.CHECKSUM;
//    }
//
//    @Override
//    public void setFilesContext(List<RenamableFile> files) {
//        this.files = files;
//    }
//
//    @Override
//    public void init() {
//        newNames.clear();
//
//        int max = getIntArg(0, 0); // opzionale: lunghezza (0 = completa)
//        boolean upper = getStringArg(1, "lower").equalsIgnoreCase("upper");
//
//        for (RenamableFile f : files) {
//            try {
//                String result = ChecksumUtils.compute(f.getSource().toPath(), alg);
//                if (max > 0 && max < result.length())
//                    result = result.substring(0, max);
//                if (upper)
//                    result = result.toUpperCase();
//                newNames.add(result);
//            } catch (Exception e) {
//                newNames.add("");
//            }
//        }
//    }
//
//    @Override
//    public String getDescription() {
//        return "Checksum " + alg + " of file contents";
//    }
//}
