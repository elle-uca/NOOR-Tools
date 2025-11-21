package org.ln.noortools.view.panel;

import org.ln.noortools.SpringContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Factory responsible for creating new instances of the UI rule panels
 * used inside the renaming accordion (AddPanel, RemovePanel, ReplacePanel, CasePanel, TagPanel).
 *
 * <p>The factory itself is a Spring-managed singleton. 
 * It acts as a construction hub that hides the details of how each panel is
 * instantiated and how dependencies are injected.
 *
 * <h3>Important design notes:</h3>
 *
 * <ul>
 *   <li><b>Most panels</b> (AddPanel, RemovePanel, ReplacePanel, CasePanel)
 *       are created using {@link SpringContext#getBean(Class)} because they
 *       do not require dynamic constructor parameters and are declared as
 *       prototype beans.</li>
 *
 *   <li><b>TagPanel</b> is the only panel created via an
 *       {@link ObjectProvider}. This allows TagPanel to be a prototype bean
 *       with additional initialization (calling {@code setAccordion()})
 *       before being returned.</li>
 *
 *   <li>The {@link AccordionPanel} reference cannot be injected by Spring,
 *       because each panel must be associated with a specific accordion
 *       instance created dynamically in the UI.</li>
 *
 *   <li>The factory is stateless and thread-safe; hence it is a {@code @Singleton}.</li>
 * </ul>
 *
 * <p>This approach keeps panel creation consistent and allows future panels
 * to be easily added without modifying UI code.</p>
 *
 * Author: Luca Noale
 */
@Component
@Scope("singleton")
public class PanelFactory {

    /** Provider for dynamically obtaining new TagPanel instances (prototype-scoped). */
    private final ObjectProvider<TagPanel> tagPanelProvider;

    /**
     * Constructor injection.
     *
     * @param tagPanelProvider a provider that supplies fresh TagPanel instances
     */
    public PanelFactory(ObjectProvider<TagPanel> tagPanelProvider) {
        this.tagPanelProvider = tagPanelProvider;
    }

    // -------------------------------------------------------------------------
    // Panel builder methods
    // -------------------------------------------------------------------------

    /**
     * Creates a new {@link AddPanel}.
     * <p>Panels are obtained from Spring so that all their dependencies are injected.</p>
     */
    public AddPanel createAddPanel() {
        return SpringContext.getBean(AddPanel.class);
    }

    /**
     * Creates a new {@link RemovePanel}.
     */
    public RemovePanel createRemovePanel() {
        return SpringContext.getBean(RemovePanel.class);
    }

    /**
     * Creates a new {@link ReplacePanel}.
     */
    public ReplacePanel createReplacePanel() {
        return SpringContext.getBean(ReplacePanel.class);
    }

    /**
     * Creates a new {@link CasePanel}.
     */
    public CasePanel createCasePanel() {
        return SpringContext.getBean(CasePanel.class);
    }

    /**
     * Creates a new {@link TagPanel}.
     *
     * <p>Unlike the other panels, TagPanel is obtained from an ObjectProvider
     * so that:
     * <ul>
     *   <li>it is always a fresh prototype instance</li>
     *   <li>we can dynamically assign its parent AccordionPanel</li>
     * </ul>
     *
     * @param accordion the accordion to which this panel will belong
     */
    public TagPanel createTagPanel(AccordionPanel accordion) {
        TagPanel panel = tagPanelProvider.getObject(); // fresh instance
        panel.setAccordion(accordion);                 // dynamic link
        return panel;
    }
}
