package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Splits a template string into literal text components and tag tokens.
 * This class contains no Spring dependencies beyond the stereotype,
 * which makes it straightforward to unit test.
 */
@Component
public class TemplateTokenizer {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("<[^>]+>|[^<]+");
    private static final Pattern NAME_PATTERN = Pattern.compile("(?<=<)[A-Za-z][A-Za-z0-9_]*(?=[:>])");
    private static final Pattern ARG_PATTERN = Pattern.compile("(?<=:)\\s*([^>:]+)\\s*(?=[:>])");

    /**
     * Tokenizes an input template such as "File <IncrNum:1>" into a
     * list of {@link TemplateComponent}s while preserving the order
     * of literals and tags.
     */
    public List<TemplateComponent> tokenize(String template) {
        List<TemplateComponent> parts = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(template);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.startsWith("<")) {
                TagToken tagToken = parseTag(token);
                if (tagToken != null) {
                    parts.add(tagToken);
                }
            } else {
                parts.add(new TextComponent(token));
            }
        }

        return parts;
    }

    /**
     * Parses a single token like "<IncrNum:1:2>" into a {@link TagToken}
     * or returns {@code null} when the token is malformed.
     */
    private TagToken parseTag(String token) {
        Matcher nameMatcher = NAME_PATTERN.matcher(token);
        if (!nameMatcher.find()) {
            return null;
        }
        String className = nameMatcher.group();

        Matcher argsMatcher = ARG_PATTERN.matcher(token);
        List<Object> arguments = new ArrayList<>();

        while (argsMatcher.find()) {
            String rawArgument = argsMatcher.group(1).trim();
            try {
                arguments.add(Integer.parseInt(rawArgument));
            } catch (NumberFormatException e) {
                arguments.add(rawArgument);
            }
        }

        return new TagToken(className, arguments);
    }
}
