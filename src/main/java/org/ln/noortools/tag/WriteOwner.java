package org.ln.noortools.tag;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserPrincipalLookupService;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;

public class WriteOwner extends AbstractFsTag implements ActionTag{

    public WriteOwner(I18n i18n, Object... args) {
        super(i18n, args);
        this.tagName = "WriteOwner";
    }

    @Override
    public void init() {
        newClear();
        String owner = getStringArg(0, "").trim();
        for (int i = 0; i < filesCtx.size(); i++) 
        	newAdd(owner);
    }

    @Override
    public void performAction() {
        String owner = getStringArg(0, "");
        for (RenamableFile rf : filesCtx) {
            try {
                Path p = rf.getSource().toPath();
                UserPrincipalLookupService svc = p.getFileSystem().getUserPrincipalLookupService();
                Files.setOwner(p, svc.lookupPrincipalByName(owner));
            } catch (Exception ignored) {}
        }
    }

    @Override
    public String getDescription() {
        return "Changes the file owner";
    }
}
