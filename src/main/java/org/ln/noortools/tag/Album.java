package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;

/**
 * Tag <Album>
 * 

 *   
 *    Author: Luca Noale
 */
public class Album extends AbstractTag {

    public Album(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "Album";
        this.type = TagType.AUDIO;
    }

    @Override
    public void init() {

    }

    @Override
    public String getDescription() {
        return i18n.get("tag.album.description");
    }
}

