package org.ln.noortools.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ln.noortools.factory.TagFactory;
import org.ln.noortools.tag.AbstractTag;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TagProcessorService {

    private final Map<String, Class<? extends AbstractTag>> tagRegistry = new HashMap<>();
    private final Map<String, RuleService> ruleRegistry = new HashMap<>();


	private final ApplicationContext ctx;

    public TagProcessorService(ApplicationContext context) {
        this.ctx = context;

        // 🔍 auto-discovery di tutti i Tag
        Reflections reflections = new Reflections("org.ln.noortools.tag");
        Set<Class<? extends AbstractTag>> tagClasses = reflections.getSubTypesOf(AbstractTag.class);

        for (Class<? extends AbstractTag> clazz : tagClasses) {
            String simpleName = clazz.getSimpleName().toLowerCase();
            tagRegistry.put(simpleName, clazz);
            System.out.println("Registered tag: " + simpleName + " -> " + clazz.getName());
        }

        // 🔍 auto-discovery di tutti i RuleService
        Map<String, RuleService> beans = ctx.getBeansOfType(RuleService.class);
        beans.forEach((name, bean) -> {
            ruleRegistry.put(bean.getClass().getSimpleName().toLowerCase(), bean);
            System.out.println("Registered rule: " + name + " -> " + bean.getClass().getName());
        });
    }

    // -------------------------------
    // TAG HANDLING
    // -------------------------------
    public AbstractTag createTag(String tagName, Object... args) {
        Class<? extends AbstractTag> clazz = tagRegistry.get(tagName.toLowerCase());
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown tag: " + tagName);
        }
        return TagFactory.create(clazz, args);
    }

    public List<String> processTag(String tagName, List<String> names, Object... args) {
        AbstractTag tag = createTag(tagName, args);
        tag.setOldNames(names);
        tag.init();
        return tag.getNewNames();
    }

    public Set<String> getAvailableTags() {
        return tagRegistry.keySet();
    }

    // -------------------------------
    // RULE HANDLING
    // -------------------------------
//    public List<String> processRule(String ruleName, List<String> names, Object... args) {
//        RuleService rule = ruleRegistry.get(ruleName.toLowerCase());
//        if (rule == null) {
//            throw new IllegalArgumentException("Unknown rule: " + ruleName);
//        }
//        return rule.applyRule(names, args);
//    }

    public Set<String> getAvailableRules() {
        return ruleRegistry.keySet();
    }
}
