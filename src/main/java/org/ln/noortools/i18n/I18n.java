package org.ln.noortools.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * I18n (Internationalization) is a utility component responsible for handling 
 * all text retrieval and translation within the application.
 * It acts as a wrapper around Spring's MessageSource to simplify access to 
 * localized messages (text strings).
 * 
 * Author: Luca Noale
 */
@Component
public class I18n {

    // Spring's core interface for resolving messages, with support for internationalization.
    // This dependency is typically configured by Spring Boot (e.g., loading messages from .properties files).
    private final MessageSource messageSource;

    /**
     * Constructor uses Dependency Injection (DI) to receive the MessageSource bean 
     * configured by the Spring framework.
     * * @param messageSource The automatically injected MessageSource instance.
     */
    public I18n(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Retrieves a localized message based on a given code and the user's current locale.
     * This is the main method for getting translated text.
     * * @param code The key (e.g., "error.file.notfound") used to look up the message in the bundles.
     * @param args Optional arguments to be substituted into the message template (e.g., placeholders like {0}).
     * @return The translated and formatted message string.
     */
    public String get(String code, Object... args) {
        // Retrieve the current locale associated with the execution context (e.g., the user's session or thread).
        Locale locale = LocaleContextHolder.getLocale();
        
        // Delegate the actual message lookup to the underlying MessageSource.
        // The last argument, 'locale', specifies which language bundle to use.
        return messageSource.getMessage(code, args, locale);
    }
}