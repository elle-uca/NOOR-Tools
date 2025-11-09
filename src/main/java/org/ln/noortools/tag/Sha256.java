package org.ln.noortools.tag;

import java.nio.file.Path;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.HashUtils;

/**
 * Tag <Sha256>
 * 

 *   
 *    Author: Luca Noale
 */
public class Sha256 extends AbstractTag {

    public Sha256(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "Sha256";
        this.type = TagType.CHECKSUM;
    }

    @Override
    public void init() {
        int len = getIntArg(0, 8);
        newClear();
        for (String old : oldNames) {
            try {
                String full = HashUtils.digest(Path.of(old), "SHA-256");
                newAdd(full.substring(0, Math.min(len, full.length())));
            } catch (Exception e) {
                newAdd("ERR");
            }
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.sha256.description");
    }
}

