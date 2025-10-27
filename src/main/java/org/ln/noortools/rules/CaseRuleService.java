//package org.ln.noortools.rules;
//
//import org.ln.noortools.enums.ModeCase;
//import org.ln.noortools.service.RuleService;
//import org.ln.noortools.util.CaseTransformer;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class CaseRuleService implements RuleService {
//
//    @Override
//    public List<String> apply(List<String> names, Object... args) {
//        if (args.length == 0 || !(args[0] instanceof ModeCase mode)) {
//            throw new IllegalArgumentException("CaseRuleService requires ModeCase argument");
//        }
//
//        return names.stream()
//                .map(name -> CaseTransformer.transformCase(name, mode))
//                .collect(Collectors.toList());
//    }
//
//
//}
