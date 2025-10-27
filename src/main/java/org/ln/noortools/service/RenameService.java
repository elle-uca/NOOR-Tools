package org.ln.noortools.service;

import org.ln.noortools.model.RenamableFile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RenameService {

    private final Map<String, RuleService> ruleRegistry = new HashMap<>();

    public RenameService() {
        // Registra i servizi
        ruleRegistry.put("add", new AddRuleService());
        ruleRegistry.put("replace", new ReplaceRuleService());
        ruleRegistry.put("remove", new RemoveRuleService());
        // qui potrai aggiungere altri RuleService (case, date, ecc.)
    }

    /**
     * Applica una regola identificata dal nome.
     */
    public List<RenamableFile> applyRule(String ruleName,
                                         List<RenamableFile> files,
                                         Object... params) {
        RuleService service = ruleRegistry.get(ruleName.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Unknown rule: " + ruleName);
        }
        return service.applyRule(files, params);
    }
}
