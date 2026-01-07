package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.i18n.I18n;
/**
 * Non-rendered header tag used to label tag groups in the rename UI.
 * <p>
 * {@code TagHeader} contributes descriptive text for tag lists but does
 * not generate a destination name and does not touch the filesystem.
 *
 * @author Luca Noale
 */

public class TagHeader extends AbstractTag {
    
	
    public TagHeader(I18n i18n, TagType type, Object... args ) {
    	super(i18n, args);
    	this.type = type;
    }

    /**
     * Returns an empty tag string because headers are UI-only.
     * This method does not touch the filesystem.
     *
     * @return empty string
     */
    @Override
    public String getTagString() { return ""; } // UI-only tag; not part of the rename preview.

    /**
     * No-op initialization for header tags.
     * This method does not touch the filesystem.
     */
	@Override
	public void init() {}

    /**
     * Provides the header label shown in tag lists.
     * This method does not touch the filesystem.
     *
     * @return header description
     */
	@Override
	public String getDescription() {
		return "Header  "+type;
	}
	

}
