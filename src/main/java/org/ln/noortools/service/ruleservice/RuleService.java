package org.ln.noortools.service.ruleservice;

import java.util.List;

import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;

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
