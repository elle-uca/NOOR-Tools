package org.ln.noortools.tag;

import org.ln.noortools.enums.ChecksumAlg;
import org.ln.noortools.i18n.I18n;

/**
 * SHA-256 checksum tag.
 *
 * Produces a lowercase hexadecimal SHA-256 hash.
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

    
    
    @Override
    public String getActionDescription() {
        return i18n.get("tag.sha256.description");
    }

}
