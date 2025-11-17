package org.ln.noortools.tag;

import java.io.IOException;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.ActionTag;
import org.ln.noortools.util.FileMetadataUtil;

public class WriteOwner extends AbstractFsTag implements ActionTag{
	
	private String target;

    public WriteOwner(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "WriteOwner";
    }

    @Override
    public void init() {
    	String arg = getStringArg(0, "").trim();
    	if (arg.isEmpty()) {
            // parse arg → targetDate (gestione errori a parte)
            target = "luke";
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
                FileMetadataUtil.setOwner(rf.getSource().toPath(), target);
                // e alla fine il tuo AudioFileIO.write(...) / log ecc. se serve
            } catch (IOException e) {
                System.err.println("[WriteCreationDate] Cannot update creation date: "
                        + rf.getSource() + " → " + e.getMessage());
            }
        }
    }

//    @Override
//    public void performAction() {
//        //String owner = getStringArg(0, "");
//        for (RenamableFile rf : filesCtx) {
//        	
//        	FileMetadataUtil.setOwner(rf.getSource().toPath(), target);
////            try {
////                Path p = rf.getSource().toPath();
////                UserPrincipalLookupService svc = p.getFileSystem().getUserPrincipalLookupService();
////                Files.setOwner(p, svc.lookupPrincipalByName(owner));
////            } catch (Exception ignored) {}
//        }
//    }

    @Override
    public String getDescription() {
        return "Changes the file owner";
    }

	@Override
	public String getActionDescription() {
		return "Changes the file owner";
	}
}
