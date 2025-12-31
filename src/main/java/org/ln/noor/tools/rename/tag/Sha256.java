package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.enums.ChecksumAlg;
import org.ln.noor.core.i18n.I18n;

/**
 * SHA-256 checksum tag.
 *
 * Produces a lowercase hexadecimal SHA-256 hash.
 *
 * @author Luca Noale
 */
public class Sha256 extends AbstractChecksumTag  {

    public Sha256(I18n i18n, Object... arg) {
        super(i18n, ChecksumAlg.SHA256, arg);
        this.tagName = "Sha256";
        this.type = TagType.CHECKSUM;
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.sha256.description");
    }
}
