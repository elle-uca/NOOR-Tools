package org.ln.noortools.tag;

import java.nio.file.Path;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.util.HashUtils;

/**
 * Tag <Crc32>
 * 

 *   
 *    Author: Luca Noale
 */
public class Crc32 extends AbstractTag {

    public Crc32(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "Crc32";
        this.type = TagType.CHECKSUM;
    }

    @Override
    public void init() {
        newClear();
        for (String old : oldNames) {
            try {
                newAdd(HashUtils.crc32(Path.of(old)));
            } catch (Exception e) {
                newAdd("ERR");
            }
        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.crc32.description");
    }
}

