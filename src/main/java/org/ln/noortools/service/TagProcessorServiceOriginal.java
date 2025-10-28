package org.ln.noortools.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ln.noortools.tag.AbstractTag;
import org.ln.noortools.tag.IncN;
import org.springframework.stereotype.Service;

@Service
public class TagProcessorServiceOriginal {

    // Registry dei tag disponibili
    private final Map<String, Class<? extends AbstractTag>> tagRegistry = new HashMap<>();

    // Regex per catturare i tag
    private static final Pattern TAG_PATTERN = Pattern.compile("<([A-Za-z]+)(?::([^>]+))?>");

    public TagProcessorServiceOriginal() {
        registerTag("IncN", IncN.class);
        // registerTag("Date", DateTag.class);
        // registerTag("UpperCase", UpperCaseTag.class);
    }

    public void registerTag(String name, Class<? extends AbstractTag> clazz) {
        tagRegistry.put(name.toLowerCase(), clazz);
    }

    /**
     * Applica i tag del pattern alla lista di nomi.
     */
    public List<String> processPattern(String pattern, List<String> originalNames) {
        List<String> result = new ArrayList<>(originalNames);

        Matcher matcher = TAG_PATTERN.matcher(pattern);
        while (matcher.find()) {
            String tagName = matcher.group(1);
            String argsStr = matcher.group(2);

            Object[] args = parseArgs(argsStr);
            AbstractTag tag = createTag(tagName, args);

            // Inietta i nomi originali e inizializza
            tag.setOldNames(result);
            tag.init();

            // Aggiorna la lista con i nuovi nomi
            result = tag.getNewNames();
        }

        return result;
    }

    /**
     * Crea dinamicamente il tag.
     */
    private AbstractTag createTag(String tagName, Object... args) {
        Class<? extends AbstractTag> clazz = tagRegistry.get(tagName.toLowerCase());
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown tag: " + tagName);
        }
        try {
            return clazz.getConstructor(Integer[].class).newInstance((Object) args);
        } catch (NoSuchMethodException e) {
            // fallback se il tag accetta argomenti generici
            try {
                return clazz.getConstructor(Object[].class).newInstance((Object) args);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create tag: " + tagName, ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tag: " + tagName, e);
        }
    }

    /**
     * Converte gli argomenti stringa in Integer[] (se numerici).
     */
    private Object[] parseArgs(String argsStr) {
        if (argsStr == null || argsStr.isEmpty()) {
            return new Object[0];
        }
        String[] parts = argsStr.split(":");
        List<Object> parsed = new ArrayList<>();
        for (String p : parts) {
            try {
                parsed.add(Integer.parseInt(p));
            } catch (NumberFormatException e) {
                parsed.add(p);
            }
        }
        return parsed.toArray();
    }
}
