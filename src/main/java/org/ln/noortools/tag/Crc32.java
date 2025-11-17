package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;

/**
 * Tag <Crc32>
 * 

 *   
 *    Author: Luca Noale
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
    
    @Override
    public String getActionDescription() {
        return i18n.get("tag.crc32.description");
    }
}

