//package org.ln.noortools.service;
//
//import org.ln.noortools.tag.AbstractTag;
//import java.util.Collections;
//
//public class TagRuleAdapter implements RuleService {
//
//    private final AbstractTag tag;
//
//    public TagRuleAdapter(AbstractTag tag) {
//        this.tag = tag;
//    }
//
//    @Override
//    public String apply(String original) {
//        tag.setOldNames(Collections.singletonList(original));
//        tag.init();
//        return tag.getNewNames().get(0);
//    }
//
//    @Override
//    public String getDescription() {
//        return tag.getDescription();
//    }
//}
