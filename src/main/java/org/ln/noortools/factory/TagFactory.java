package org.ln.noortools.factory;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.tag.Date;
import org.ln.noortools.tag.DecH;
import org.ln.noortools.tag.DecN;
import org.ln.noortools.tag.DecR;
import org.ln.noortools.tag.IncH;
import org.ln.noortools.tag.IncL;
import org.ln.noortools.tag.IncN;
import org.ln.noortools.tag.IncR;
import org.ln.noortools.tag.RandL;
import org.ln.noortools.tag.RandN;
import org.ln.noortools.tag.Subs;
import org.ln.noortools.tag.Time;
import org.ln.noortools.tag.Word;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Factory for creating tag instances used by NOOR Tools.
 * 
 * This class provides Spring-managed creation of Tag objects
 * ensuring that each tag receives the {@link I18n} dependency
 * and any required initialization parameters.
 *
 * Author: Luca Noale
 */
@Component
@Scope("singleton")
public class TagFactory {

    private final I18n i18n;

    public TagFactory(I18n i18n) {
        this.i18n = i18n;
    }

    // --- Example tags ---

    /**
     * Incremental numeric tag <IncN>.
     * @param args optional parameters for tag logic
     */
    public IncN createIncNTag(Object... args) {
        return new IncN(i18n, args);
    }
    
    /**
     * Decremental numeric tag <DecN>.
     * @param args optional parameters for tag logic
     */
    public DecN createDecNTag(Object... args) {
        return new DecN(i18n, args);
    }

    /**
     * Incremental hexadecimal tag <IncH>.
     * @param args optional parameters for tag logic
     */
    public IncH createIncHTag(Object... args) {
        return new IncH(i18n, args);
    }
 
    /**
     * Decremental hexadecimal tag <DecH>.
     * @param args optional parameters for tag logic
     */
    public DecH createDecHTag(Object... args) {
        return new DecH(i18n, args);
    }
    

     /**
     * Incremental Roman numeric tag <IncR>.
     * @param args optional parameters for tag logic
     */
    public IncR createIncRTag(Object... args) {
        return new IncR(i18n, args);
    }
    
    /**
     * Decremental Roman tag <DecR>.
     * @param args optional parameters for tag logic
     */
    public DecR createDecRTag(Object... args) {
        return new DecR(i18n, args);
    }

    /**
     * Incremental numeric tag <IncN>.
     * @param args optional parameters for tag logic
     */
    public IncL createIncLTag(Object... args) {
        return new IncL(i18n, args);
    }
    
    /**
     * Random number generator tag <Rnd>.
     * @param args optional parameters for random length, charset, etc.
     */
    public RandN createRandNTag(Object... args) {
        return new RandN(i18n, args);
    }

    /**
     * Random string generator tag <Rnd>.
     * @param args optional parameters for random length, charset, etc.
     */
    public RandL createRandLTag(Object... args) {
        return new RandL(i18n, args);
    }
    
    /**
     * Time-based tag <Time>.
     * @param args optional format or parameters
     */
    public Time createTimeTag(Object... args) {
        return new Time(i18n, args);
    }
    
    /**
     * Date-based tag <Date>.
     * @param args optional format or parameters
     */
    public Date createDateTag(Object... args) {
        return new Date(i18n, args);
    }
    
    /**
     * Date-based tag <Date>.
     * @param args optional format or parameters
     */
    public Word createWordTag(Object... args) {
        return new Word(i18n, args);
    }
    
    /**
     * Date-based tag <Date>.
     * @param args optional format or parameters
     */
    public Subs createSubsTag(Object... args) {
        return new Subs(i18n, args);
    }
}
























//
//package org.ln.noortools.factory;
//
//import org.ln.noortools.tag.AbstractTag;
//
//public class TagFactory {
//
//    private TagFactory() {
//        // utility class, no instances
//    }
//
//    /**
//     * Crea dinamicamente un tag a partire dalla classe.
//     * Supporta sia costruttori con Object... args che costruttori vuoti.
//     */
//    public static AbstractTag create(Class<? extends AbstractTag> clazz, Object... args) {
//        try {
//            // ✅ preferisce il costruttore con varargs
//            return clazz.getConstructor(Object[].class).newInstance((Object) args);
//        } catch (NoSuchMethodException e) {
//            try {
//                // fallback: costruttore vuoto + setArgs()
//                AbstractTag tag = clazz.getDeclaredConstructor().newInstance();
//                tag.setArgs(args);
//                return tag;
//            } catch (Exception ex) {
//                throw new RuntimeException("Failed to create tag via no-arg constructor: " + clazz.getName(), ex);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create tag: " + clazz.getName(), e);
//        }
//    }
//}
