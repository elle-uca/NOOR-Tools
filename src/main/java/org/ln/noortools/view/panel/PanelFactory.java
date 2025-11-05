package org.ln.noortools.view.panel;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.view.TagListModel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating new instances of rule panels
 * (e.g. AddPanel, RemovePanel, ReplacePanel, CasePanel).
 *
 * <p>This class centralizes the creation of panel components, injecting
 * shared dependencies such as {@link I18n} and {@link RenamerService}.
 * 
 * <p>Each panel still receives its parent {@link AccordionPanel}, which
 * cannot be injected by Spring directly since it is dynamically created.
 * 
 * <p>Marked as a Spring {@code @Component} with {@code singleton} scope,
 * since the factory itself is stateless and can be reused across the UI.
 *
 * Author: Luca Noale
 */
@Component
@Scope("singleton")
public class PanelFactory {

    private final I18n i18n;
    private final RenamerService renamerService;
    @SuppressWarnings("unused")
	private final TagListModel tagListModel;
    private final ObjectProvider<TagPanel> tagPanelProvider;

    /**
     * Constructor-based dependency injection.
     *
     * @param i18n the internationalization service
     * @param renamerService the core renaming service
     */
    public PanelFactory(I18n i18n, 
    		RenamerService renamerService, 
    		TagListModel tagListModel, 
    		ObjectProvider<TagPanel> tagPanelProvider) {
        this.i18n = i18n;
        this.renamerService = renamerService;
        this.tagListModel = tagListModel;
        this.tagPanelProvider = tagPanelProvider;
    }

    /**
     * Creates a new {@link AddPanel} instance bound to the specified accordion.
     */
    public AddPanel createAddPanel(AccordionPanel accordion) {
        return new AddPanel(i18n, renamerService);
    }

    /**
     * Creates a new {@link RemovePanel} instance bound to the specified accordion.
     */
    public RemovePanel createRemovePanel(AccordionPanel accordion) {
        return new RemovePanel(i18n, renamerService);
    }

    /**
     * Creates a new {@link ReplacePanel} instance bound to the specified accordion.
     */
    public ReplacePanel createReplacePanel(AccordionPanel accordion) {
        return new ReplacePanel(i18n, renamerService);
    }

    /**
     * Creates a new {@link CasePanel} instance bound to the specified accordion.
     */
    public CasePanel createCasePanel(AccordionPanel accordion) {
        return new CasePanel(i18n, renamerService);
    }
    
    /**
     * Creates a new {@link TagPanel} instance bound to the specified accordion.
     */
    public TagPanel createTagPanel(AccordionPanel accordion) {
        TagPanel panel = tagPanelProvider.getObject();
        panel.setAccordion(accordion);
        return panel;
    }
}
