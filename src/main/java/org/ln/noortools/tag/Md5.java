package org.ln.noortools.tag;

import org.ln.noortools.enums.ChecksumAlg;
import org.ln.noortools.i18n.I18n;

/**
 * MD5 checksum tag.
 *
 * Produces a lowercase hexadecimal MD5 hash.
 */
public class Md5 extends AbstractChecksumTag {

    public Md5(I18n i18n, Object... arg) {
        super(i18n, ChecksumAlg.MD5, arg);
        this.tagName = "Md5";
        this.type = TagType.CHECKSUM;
    }

    // No override needed: base class handles everything.
    @Override
    public String getDescription() {
        return i18n.get("tag.md5.description");
    }

    
    @Override
    public String getActionDescription() {
        return i18n.get("tag.md5.description");
    }

}
