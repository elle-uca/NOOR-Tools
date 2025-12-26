package org.ln.noor.service.ruleservice;

import java.util.List;

import org.ln.noor.core.enums.RenameMode;
import org.ln.noor.core.model.RenamableFile;

public interface RuleService {
    


    /**
     * Applies a rename rule to the given files.
     *
     * @param files   list of files (original state)
     * @param mode    how to apply the rename (FULL, NAME_ONLY, EXT_ONLY)
     * @param params  additional rule parameters (text, numbers, etc.)
     * @return updated list with new destination names
     */
    List<RenamableFile> applyRule(List<RenamableFile> files, RenameMode mode, Object... params);


}
