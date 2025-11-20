package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class TemplateTokenizer {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("<[^>]+>|[^<]+");
    private static final Pattern NAME_PATTERN = Pattern.compile("(?<=<)[A-Za-z][A-Za-z0-9_]*(?=[:>])");
    private static final Pattern ARG_PATTERN = Pattern.compile("(?<=:)\\s*([^>:]+)\\s*(?=[:>])");

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
