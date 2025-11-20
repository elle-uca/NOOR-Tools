package org.ln.noortools.service.ruleservice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileNameUtil;

/**
 * Base class for all RuleServices.
 * 
 * Responsibilities:
 * - Provides null-safety (files, params, etc.)
 * - Copies RenamableFile to preserve immutability
 * - Handles file extensions consistently
 * - Supports RenameMode: FULL, NAME_ONLY, EXT_ONLY
 * - Delegates string transformation to subclasses via {@link #transformName}.
 * 
 * Author: Luca Noale
 */
public abstract class AbstractRuleService implements RuleService {
	
	
    @Override
    public final List<RenamableFile> applyRule(List<RenamableFile> files, RenameMode mode, Object... params) {
        if (files == null || files.isEmpty()) return List.of();

        List<RenamableFile> updated = new ArrayList<>();

        for (RenamableFile file : files) {
            if (file == null) continue;

            String base = FileNameUtil.getBaseName(file.getSource().getName());
            String ext  = FileNameUtil.getExtension(file.getSource().getName());

            String newBase = base;
            String newExt  = ext;

            switch (mode) {
                case NAME_ONLY -> {
                    newBase = transformName(base, params);
                }
                case EXT_ONLY -> {
                    newExt = transformExtension(ext, params);
                }
//                case FULL -> {
//                    String full = file.getSource().getName();
//                    String newFull = transformFullName(full, params);
//                    updated.add(copyWithName(file, newFull));
//                    continue;
//                }
            }

            String finalName = newBase + (newExt.isEmpty() ? "" : "." + newExt);
            updated.add(copyWithName(file, finalName));
        }

        return updated;
    }
    
    /**
     * Transforms a string (base name, extension, or full name depending on mode).
     *
     * @param input  the input string (never null)
     * @param params parameters passed by the service
     * @return transformed string
     */
    protected abstract String transformName(String input, Object... params);


    protected String transformExtension(String currentExt, Object... params) {
        return transformName(currentExt, params);
    }

    protected String transformFullName(String fullName, Object... params) {
        return transformName(fullName, params);
    }

    private RenamableFile copyWithName(RenamableFile original, String newName) {
        RenamableFile copy = new RenamableFile(new File(original.getSource().getPath()));
        copy.setDestinationName(newName);
        copy.setSelected(original.isSelected());           // ✅ preserva selezione
        copy.setFileStatus(original.getFileStatus()); 
        return copy;
    }
}

    

