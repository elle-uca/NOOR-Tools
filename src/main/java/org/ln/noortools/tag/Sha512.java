package org.ln.noortools.tag;

import org.ln.noortools.enums.ChecksumAlg;
import org.ln.noortools.i18n.I18n;

public class Sha512 extends AbstractChecksumTag {

    public Sha512(I18n i18n, Object... arg) {
        super(i18n, ChecksumAlg.SHA512, arg);
        this.tagName = "Sha512";
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.sha512.description");
    }
}
