package org.ln.noor.tools.rename.tag;

import org.ln.noor.core.i18n.I18n;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.ln.noor.tools.rename.util.DateTimeFormatMapper;
import org.ln.noor.tools.rename.util.FileMetadataUtil;

public class AccessDate extends AbstractFsTag {
    
	public AccessDate(I18n i18n, Object... arg) {
        super(i18n, arg);
        this.tagName = "AccessDate";
    }

    @Override
    public void init() {
    	String pattern = DateTimeFormatMapper.toJavaPattern(
    			getStringArg(0, "yyyy-mm-dd hh:nn"));
        newClear();
        for (RenamableFile rf : filesCtx) {
            var dt = FileMetadataUtil.getAccessDate(rf.getSource().toPath());
            String formatted = DateTimeFormatMapper.format(dt, pattern);
            newAdd(formatted);
        }
    }

	@Override
	public String getDescription() {
		return "Returns file last access timestamp";
	}


}
