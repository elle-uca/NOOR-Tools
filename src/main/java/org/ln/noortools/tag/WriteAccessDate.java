package org.ln.noortools.tag;

import java.io.IOException;
import java.time.LocalDateTime;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileMetadataUtil;

public class WriteAccessDate extends AbstractFsTag implements ActionTag{
	
	private LocalDateTime targetDate;

    public WriteAccessDate(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "WriteAccessDate";
    }

    @Override
    public void init() {
        String arg = getStringArg(0, "").trim();
        if (!arg.isEmpty()) {
            targetDate = parseDateTime(arg);
        } else {
            targetDate = LocalDateTime.now();
        }
        newClear();
        for (RenamableFile rf : filesCtx) {
        	newAdd(rf.getSource().getName());
        }
    }
    

    
    @Override
   public void performAction() {
        for (RenamableFile rf : filesCtx) {
            try {
                FileMetadataUtil.setAccessDate(rf.getSource().toPath(), targetDate);
                // e alla fine il tuo AudioFileIO.write(...) / log ecc. se serve
            } catch (IOException e) {
                System.err.println("[WriteCreationDate] Cannot update creation date: "
                        + rf.getSource() + " → " + e.getMessage());
            }
        }
    }
    
//    @Override
//    public void performAction() {
//        String ts = getStringArg(0, "");
//        try {
//            FileTime t = FileTime.from(Instant.parse(ts));
//            for (RenamableFile rf : filesCtx) {
//                writeTime(rf, "basic:lastModifiedTime", t);
//            }
//        } catch (Exception ignored) {}
//    }

    @Override
    public String getDescription() {
        return "Writes modified timestamp (ISO-8601)";
    }

	@Override
	public String getActionDescription() {
		return "Writes modified timestamp (ISO-8601)";
	}
}
