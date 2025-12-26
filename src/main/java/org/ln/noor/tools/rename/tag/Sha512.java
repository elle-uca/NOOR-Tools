package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.enums.ChecksumAlg;
import org.ln.noor.core.i18n.I18n;

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
