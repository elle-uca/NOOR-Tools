package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;

/**
 * Tag <Title>
 * 

 *   
 *    Author: Luca Noale
 */
public class Title extends AbstractTag {

    public Title(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "Title";
        this.type = TagType.AUDIO;
    }

    @Override
    public void init() {
//    	start = getIntArg(0, 1) > 0 ? getIntArg(0, 1) : 1;
//        newClear();              // reset result list
//
//        for (int i = 0; i < oldSize(); i++) {
//            int currentNumber = start + i;
//            String hex = Integer.toHexString(currentNumber).toUpperCase();
//            newAdd(hex);
//        }
    }

    @Override
    public String getDescription() {
        return i18n.get("tag.title.description");
    }
}

