package org.ln.noor.tools.rename.service;

import org.springframework.stereotype.Service;

/**
 * Rule service for the Add rule in the rename tool.
 * <p>
 * Produces destination names by inserting text at the start, end, or
 * a specific position within each source file name (without extension),
 * contributing to the rename preview. This rule service does not touch
 * the filesystem or apply changes.
 *
 * <p>Examples:
 * <ul>
 *   <li>{@code <Add:Hello:START>} → {@code HelloFile}</li>
 *   <li>{@code <Add:World:END>} → {@code FileWorld}</li>
 *   <li>{@code <Add:123:3>} → {@code Fil123eName} (after the 3rd character)</li>
 * </ul>
 *
 * @author Luca Noale
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
//        if (index <= 0) return text + base;                       // at the start
//        if (position == Integer.MAX_VALUE) return base + text;    // at the end
//        if (index >= base.length()) return base + text;           // beyond length → append
//        return base.substring(0, index) + text + base.substring(index);
//    }
//    
//    @Override
//    protected String transformExtension(String currentExt, Object... params) {
//        // EXT_ONLY should replace the extension rather than append text.
//        String text = (params.length > 0 && params[0] instanceof String) ? (String) params[0] : "";
//        if (text == null) text = "";
//        // normalize without the leading dot
//        return text.startsWith(".") ? text.substring(1) : text;
//    }

}
