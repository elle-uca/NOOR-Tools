package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;

/**
 * Tag <Md5>
 * 

 *   
 *    Author: Luca Noale
 */
public class Md5 extends AbstractChecksumTag {

    public Md5(I18n i18n, Object... arg) {
        super(i18n, ChecksumAlg.MD5,arg);
        this.tagName = "Md5";
        this.type = TagType.CHECKSUM;
    }
    
    
//    @Override
//    public void init() {
////        newClear();
////        for (String old : oldNames) {
////            try {
////                newAdd(HashUtils.digest(Path.of(old), "MD5"));
////            } catch (Exception e) {
////                newAdd("ERR");
////            }
////        }
//    }

    @Override
    public String getDescription() {
        return i18n.get("tag.md5.description");
    }
    
    @Override
    public String getActionDescription() {
        return i18n.get("tag.md5.description");
    }
}

