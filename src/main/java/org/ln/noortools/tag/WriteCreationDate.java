package org.ln.noortools.tag;

import java.io.IOException;
import java.time.LocalDateTime;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.ActionTag;
import org.ln.noortools.util.FileMetadataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteCreationDate extends AbstractFsTag implements ActionTag{

        private static final Logger logger = LoggerFactory.getLogger(WriteCreationDate.class);

        private LocalDateTime targetDate;

    public WriteCreationDate(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "WriteCreationDate";
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
                FileMetadataUtil.setCreationDate(rf.getSource().toPath(), targetDate);
                // e alla fine il tuo AudioFileIO.write(...) / log ecc. se serve
            } catch (IOException e) {
                logger.error("[WriteCreationDate] Cannot update creation date: {}", rf.getSource(), e);
            }
        }
    }
    
    
//    @Override
//    public void performAction() {
//        if (targetDate == null) {
//            return;
//        }
//        String ts = getStringArg(0, "");
//        try {
//            FileTime t = FileTime.from(Instant.parse(ts));
//            for (RenamableFile rf : filesCtx) {
//                writeTime(rf, "basic:creationTime", t);
//            }
//        } catch (DateTimeParseException e) {
//            System.err.println("[WriteCreationDate] Invalid timestamp: " + ts);
//        }
//    }


    
    @Override
    public String getDescription() {
        return "Writes creation timestamp (ISO-8601)";
    }

	@Override
	public String getActionDescription() {
		return "Writes creation timestamp (ISO-8601)";
	}


}
