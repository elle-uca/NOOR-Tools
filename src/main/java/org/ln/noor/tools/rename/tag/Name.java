package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.i18n.I18n;

/**
 * Tag <Name>
 *
 * 
 *

 *
 * Author: Luca Noale
 */
public class Name extends AbstractTag {



    public Name(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "Name";
        this.type = TagType.STRING;
    }

    @Override
    public void init() {
        newClear();
        for (String s : getOldNames()) {
            newAdd(s);
        }
    }



    
    
    @Override
    public String getDescription() {
        return i18n.get("tag.name.description") ;
    }
    

}
