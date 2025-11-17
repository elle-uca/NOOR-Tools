package org.ln.noortools.tag;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.DateTimeFormatMapper;
import org.ln.noortools.util.FileMetadataUtil;

public class ModifyDate extends AbstractFsTag {
    
	public ModifyDate(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "ModifyDate";
    }

	
    @Override
    public void init() {
    	String pattern = DateTimeFormatMapper.toJavaPattern(
    			getStringArg(0, "yyyy-mm-dd hh:nn"));
        newClear();
        for (RenamableFile rf : filesCtx) {
            var dt = FileMetadataUtil.getModificationDate(rf.getSource().toPath());
            String formatted = DateTimeFormatMapper.format(dt, pattern);
            newAdd(formatted);
        }
    }
	

	@Override
	public String getDescription() {
		return "Returns last modified timestamp";
	}

	@Override
	public String getActionDescription() {
		return "Returns last modified timestamp";
	}
}
