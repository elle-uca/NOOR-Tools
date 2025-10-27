
package org.ln.noortools.factory;

import org.ln.noortools.tag.AbstractTag;

public class TagFactory {

    private TagFactory() {
        // utility class, no instances
    }

    /**
     * Crea dinamicamente un tag a partire dalla classe.
     * Supporta sia costruttori con Object... args che costruttori vuoti.
     */
    public static AbstractTag create(Class<? extends AbstractTag> clazz, Object... args) {
        try {
            // ✅ preferisce il costruttore con varargs
            return clazz.getConstructor(Object[].class).newInstance((Object) args);
        } catch (NoSuchMethodException e) {
            try {
                // fallback: costruttore vuoto + setArgs()
                AbstractTag tag = clazz.getDeclaredConstructor().newInstance();
                tag.setArgs(args);
                return tag;
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create tag via no-arg constructor: " + clazz.getName(), ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tag: " + clazz.getName(), e);
        }
    }
}
