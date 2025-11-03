package org.ln.noortools.view.panel;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * Lightweight factory for creating new {@link AccordionPanel} instances.
 * 
 * This factory uses Spring's {@link ObjectProvider} to request a new
 * {@code AccordionPanel} each time {@link #createAccordion()} is called.
 * 
 * Because {@code AccordionPanel} is declared with {@code @Scope("prototype")},
 * every call produces a fresh instance — ensuring UI components are never shared
 * across different windows or contexts.
 * 
 * Author: Luca Noale
 */
@Component
public class AccordionFactory {

    private final ObjectProvider<AccordionPanel> accordionProvider;

    /**
     * Constructor injection.
     * Spring automatically provides the {@link ObjectProvider} for the
     * {@link AccordionPanel} bean.
     */
    public AccordionFactory(ObjectProvider<AccordionPanel> accordionProvider) {
        this.accordionProvider = accordionProvider;
    }

    /**
     * Creates a new {@link AccordionPanel} instance managed by Spring.
     * 
     * @return a fresh prototype-scoped {@code AccordionPanel}
     */
    public AccordionPanel createAccordion() {
        return accordionProvider.getObject();
    }
}

