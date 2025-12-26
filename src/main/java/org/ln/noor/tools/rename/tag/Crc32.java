package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.enums.ChecksumAlg;
import org.ln.noor.core.i18n.I18n;

/**
 * CRC32 checksum tag.
 *
 * Produces a lowercase hexadecimal CRC32 hash.
 */
public class Crc32 extends AbstractChecksumTag {

    public Crc32(I18n i18n, Object... arg) {
        super(i18n, ChecksumAlg.CRC32, arg);
        this.tagName = "Crc32";
        this.type = TagType.CHECKSUM;
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.crc32.description");
    }
}
