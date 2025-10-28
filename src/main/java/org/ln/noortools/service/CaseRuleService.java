package org.ln.noortools.service;

import org.ln.noortools.enums.ModeCase;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.StringCaseUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule service <Case>
 *
 * Applies case transformations to filenames (without extension),
 * according to the selected {@link ModeCase}.
 *
 * Supported modes:
 * - UPPER: convert entire string to uppercase
 * - LOWER: convert entire string to lowercase
 * - TITLE_CASE: capitalize first letter of each word
 * - CAPITALIZE_FIRST: capitalize only the first letter of the string
 * - TOGGLE_CASE: invert the case of each letter
 *
 * Example:
 *   Input file: "myDocument.txt"
 *   Mode: UPPER
 *   Result: "MYDOCUMENT"
 *
 * Author: Luca Noale
 */
@Service("caseruleservice")
public class CaseRuleService implements RuleService {

    /**
     * Generic dispatcher called by {@link org.ln.noortools.service.RenamerService}.
     *
     * @param files  list of files to process
     * @param params parameters (expects a single {@link ModeCase})
     * @return list of updated files with transformed names
     */
    @Override
    public List<RenamableFile> applyRule(List<RenamableFile> files, Object... params) {
        ModeCase mode = (ModeCase) params[0];
        return applyRule(files, mode);
    }

    /**
     * Type-safe overload: applies case transformation with a specific mode.
     *
     * @param files list of files to process
     * @param mode  case transformation to apply
     * @return list of updated files with transformed names
     */
    public List<RenamableFile> applyRule(List<RenamableFile> files, ModeCase mode) {
        if (files == null) return List.of();

        List<RenamableFile> updated = new ArrayList<>();
        for (RenamableFile file : files) {
            // Extract filename without extension
            String base = file.getSource().getName().replaceFirst("\\.[^.]+$", "");

            // Apply transformation (delegated to StringCaseUtil)
            String newName = StringCaseUtil.transformCase(base, mode);

            // Create a new instance with updated destination name
            RenamableFile copy = new RenamableFile(file.getSource());
            copy.setDestinationName(newName);
            updated.add(copy);
        }
        return updated;
    }
}
