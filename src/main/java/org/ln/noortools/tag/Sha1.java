package org.ln.noortools.tag;

import org.ln.noortools.enums.ChecksumAlg;
import org.ln.noortools.i18n.I18n;

public class Sha1 extends AbstractChecksumTag {

    public Sha1(I18n i18n, Object... arg) {
        super(i18n, ChecksumAlg.SHA1, arg);
        this.tagName = "Sha1";
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.sha1.description");
    }
}
