package org.ln.noor.core.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Static access holder for the Spring {@link ApplicationContext}.
 * <p>
 * {@code SpringContext} provides a globally accessible reference
 * to the Spring application context. It is primarily intended for
 * legacy or framework-integrated code where standard dependency
 * injection is not easily applicable (e.g. static contexts,
 * Swing components, or third-party callbacks).
 *
 * <p><strong>Usage note:</strong><br>
 * This class should be used sparingly. Whenever possible, prefer
 * constructor-based dependency injection instead of static access.
 *
 * <p>
 * The context is automatically injected by Spring at startup
 * through the {@link ApplicationContextAware} interface.
 *
 * @author Luca Noale
 */
@Component
public class SpringContext implements ApplicationContextAware {

    /** 
     * Holds the active Spring application context.
     * <p>
     * This reference is set once during application startup
     * and remains available for the entire application lifecycle.
     */
    private static ApplicationContext context;

    /**
     * Called by Spring to inject the current {@link ApplicationContext}.
     *
     * @param ctx the active application context
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

    /**
     * Returns a Spring-managed bean of the specified type.
     * <p>
     * This method provides static access to beans when
     * dependency injection cannot be used directly.
     *
     * @param <T>   the bean type
     * @param clazz the class object of the requested bean
     * @return the corresponding Spring bean instance
     *
     * @throws org.springframework.beans.BeansException
     *         if no such bean is defined or the context is not available
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
