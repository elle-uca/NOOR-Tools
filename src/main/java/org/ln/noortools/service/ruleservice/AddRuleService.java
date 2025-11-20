package org.ln.noortools.service.ruleservice;

import org.springframework.stereotype.Service;

/**
 * Rule service <Add>
 *
 * Adds a substring at the beginning, end, or after a given position
 * in each filename (without extension).
 *
 * Examples:
 *   <Add:Hello:START>  -> HelloFile
 *   <Add:World:END>    -> FileWorld
 *   <Add:123:3>        -> Fil123eName   (after 3rd character)
 *
 * Author: Luca Noale
 */
@Service("addruleservice")
public class AddRuleService extends AbstractRuleService {
	
    @Override
    protected String transformName(String base, Object... params) {
        String text = (params.length > 0 && params[0] instanceof String) ? (String) params[0] : "";
        int position = (params.length > 1 && params[1] instanceof Integer) ? (int) params[1] : 1;
        if (base == null) return text;
        if (text == null) text = "";

        int index = position - 1;

        if (index <= 0) return text + base;                  
        if (position == Integer.MAX_VALUE) return base + text; 
        if (index >= base.length()) return base + text;      
        return base.substring(0, index) + text + base.substring(index);
    }



 //   @Override
//    protected String transformName(String base, Object... params) {
//        String text = (params.length > 0 && params[0] instanceof String) ? (String) params[0] : "";
//        int position = (params.length > 1 && params[1] instanceof Integer) ? (int) params[1] : 1;
//        if (base == null) return text;
//        if (text == null) text = "";
//
//        int index = position - 1;
//
//        if (index <= 0) return text + base;                  // start
//        if (position == Integer.MAX_VALUE) return base + text;    // end
//        if (index >= base.length()) return base + text;      // beyond length → append
//        return base.substring(0, index) + text + base.substring(index);
//
//    }
    
//    protected String transformName(String base, Object... params) {
//        String text = (params.length > 0 && params[0] instanceof String) ? (String) params[0] : "";
//        int position = (params.length > 1 && params[1] instanceof Integer) ? (int) params[1] : 1;
//        if (base == null) return text;
//        if (text == null) text = "";
//
//        int index = position - 1;
//
//        if (index <= 0) return text + base;                       // all'inizio
//        if (position == Integer.MAX_VALUE) return base + text;    // alla fine
//        if (index >= base.length()) return base + text;           // oltre → append
//        return base.substring(0, index) + text + base.substring(index);
//    }
//    
//    @Override
//    protected String transformExtension(String currentExt, Object... params) {
//        // 🔧 Per EXT_ONLY in Add vogliamo SOSTITUIRE l’estensione, non inserirci testo.
//        String text = (params.length > 0 && params[0] instanceof String) ? (String) params[0] : "";
//        if (text == null) text = "";
//        // normalizza: senza il punto iniziale
//        return text.startsWith(".") ? text.substring(1) : text;
//    }

}
