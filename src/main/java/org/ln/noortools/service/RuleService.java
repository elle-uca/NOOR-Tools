package org.ln.noortools.service;

import java.util.List;

import org.ln.noortools.model.RenamableFile;

public interface RuleService {
    
	/**
     * Apply the rule to a single input string.
     */
	List<RenamableFile> applyRule(List<RenamableFile> names, Object... args);

    /**
     * A human-readable description of the rule.
     */
//    String getDescription();
}
