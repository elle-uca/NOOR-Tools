package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;

public class TagHeader extends AbstractTag {
    
	
	public TagHeader(I18n i18n, TagType type, Object... args ) {
    	super(i18n, args);
    	this.type = type;
    }

    @Override
    public String getTagString() { return ""; } // non usato

	@Override
	public void init() {}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Header  "+type;
	}
	
	@Override
	public String getActionDescription() {
		// TODO Auto-generated method stub
		return "Header  "+type;
	}
}
