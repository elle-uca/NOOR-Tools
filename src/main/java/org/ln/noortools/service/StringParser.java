package org.ln.noortools.service;

import java.util.List;
import java.util.Set;

import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;
import org.springframework.stereotype.Component;

/**
 * Parses a rename template containing tags (e.g., <IncN:1>)
 * and applies them to a list of files according to the selected RenameMode.
 */
@Component
public class StringParser {

    private final TemplateTokenizer tokenizer;
    private final TagBuilder tagBuilder;
    private final TemplateApplier templateApplier;

    public StringParser(TemplateTokenizer tokenizer, TagBuilder tagBuilder, TemplateApplier templateApplier) {
        this.tokenizer = tokenizer;
        this.tagBuilder = tagBuilder;
        this.templateApplier = templateApplier;
    }

    /**
     * Parses a rename template, evaluates every tag inside it and returns a new list
     * of {@link RenamableFile} with updated destination names. The original list is
     * never mutated.
     */
    public List<RenamableFile> parse(String template, List<RenamableFile> files, RenameMode mode) {
        if (template == null || files == null || files.isEmpty() || !isParsable(template)) {
            return files;
        }

        List<TemplateComponent> tokens = tokenizer.tokenize(template);
        List<Object> templateComponents = tagBuilder.buildComponents(tokens);

        return templateApplier.apply(templateComponents, files, mode);
    }

    // ========================================================================================
    // Helper methods

    public static boolean isParsable(String str) {
        return str != null && !str.isBlank() && count(str, '<') == count(str, '>');
    }

    private static int count(String s, char c) {
        int count = 0;
        for (char ch : s.toCharArray())
            if (ch == c) count++;
        return count;
    }

    /** Clears any cached constructors. Kept for compatibility with previous API. */
    public void clearCache() {
        // No constructor cache with the new builder; method retained to avoid breaking callers.
    }

    /** Returns cached tag names. Kept for compatibility with previous API. */
    public Set<String> getCachedTags() {
        return Set.of();
    }
}
