package org.ln.noortools.tag;

import java.io.IOException;
import java.time.LocalDateTime;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.ActionTag;
import org.ln.noortools.util.FileMetadataUtil;

public class WriteModifyDate extends AbstractFsTag implements ActionTag{
	
	private LocalDateTime targetDate;

    public WriteModifyDate(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "WriteModifyDate";
    }

    @Override
    public void init() {
        // 1) calcoli la data da scrivere (param o default)
        String arg = getStringArg(0, "").trim();
        if (!arg.isEmpty()) {
            // parse arg → targetDate (gestione errori a parte)
            targetDate = parseDateTime(arg);
        } else {
            // ad es. default = data attuale, o nulla se vuoi obbligare il param
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
                FileMetadataUtil.setModificationDate(rf.getSource().toPath(), targetDate);
                // e alla fine il tuo AudioFileIO.write(...) / log ecc. se serve
            } catch (IOException e) {
                System.err.println("[WriteCreationDate] Cannot update creation date: "
                        + rf.getSource() + " → " + e.getMessage());
            }
        }
    }
    
    

    @Override
    public String getDescription() {
        return "Writes modified timestamp (ISO-8601)";
    }

	@Override
	public String getActionDescription() {
		return "Writes modified timestamp (ISO-8601)";
	}
}
