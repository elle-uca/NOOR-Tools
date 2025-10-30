package org.ln.noortools.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.util.FileNameUtil;

/**
 * Base class for all RuleServices.
 * 
 * Responsibilities:
 * - Provides null-safety (files, params, etc.)
 * - Copies RenamableFile to preserve immutability
 * - Handles file extensions consistently
 * - Supports RenameMode: FULL, NAME_ONLY, EXT_ONLY
 * - Delegates string transformation to subclasses via {@link #transformName}.
 */
public abstract class AbstractRuleService implements RuleService {
	
	
    @Override
    public final List<RenamableFile> applyRule(List<RenamableFile> files, RenameMode mode, Object... params) {
        if (files == null || files.isEmpty()) return List.of();

        List<RenamableFile> updated = new ArrayList<>();

        for (RenamableFile file : files) {
            if (file == null) continue;

            String base = FileNameUtil.getBaseName(file.getSource().getName());
            String ext  = FileNameUtil.getExtension(file.getSource().getName());

            String newBase = base;
            String newExt  = ext;

            switch (mode) {
                case NAME_ONLY -> {
                    newBase = transformName(base, params);
                }
                case EXT_ONLY -> {
                    newExt = transformExtension(ext, params);
                }
                case FULL -> {
                    String full = file.getSource().getName();
                    String newFull = transformFullName(full, params);
                    updated.add(copyWithName(file, newFull));
                    continue;
                }
            }

            String finalName = newBase + (newExt.isEmpty() ? "" : "." + newExt);
            updated.add(copyWithName(file, finalName));
        }

        return updated;
    }
    
    /**
     * Transforms a string (base name, extension, or full name depending on mode).
     *
     * @param input  the input string (never null)
     * @param params parameters passed by the service
     * @return transformed string
     */
    protected abstract String transformName(String input, Object... params);


    protected String transformExtension(String currentExt, Object... params) {
        return transformName(currentExt, params);
    }

    protected String transformFullName(String fullName, Object... params) {
        return transformName(fullName, params);
    }

    private RenamableFile copyWithName(RenamableFile original, String newName) {
        RenamableFile copy = new RenamableFile(new File(original.getSource().getPath()));
        copy.setDestinationName(newName);
        return copy;
    }
}
//    @Override
//    public final List<RenamableFile> applyRule(List<RenamableFile> files, Object... params) {
//        if (files == null || files.isEmpty()) return List.of();
//
//        List<RenamableFile> updated = new ArrayList<>();
//
//        for (RenamableFile file : files) {
//            if (file == null) continue;
//
//            String base = FileNameUtil.getBaseName(file.getSource().getName());
//            String ext  = FileNameUtil.getExtension(file.getSource().getName());
//
//            RenameMode mode = (params.length > 2 && params[2] instanceof RenameMode)
//                    ? (RenameMode) params[2]
//                    : RenameMode.NAME_ONLY;
//
//            String newBase = base;
//            String newExt  = ext;
//
//            switch (mode) {
//                case NAME_ONLY -> {
//                    newBase = transformName(base, params);
//                }
//                case EXT_ONLY -> {
//                    // 🔑 Usa un hook dedicato per l’estensione
//                    newExt = transformExtension(ext, params);
//                }
//                case FULL -> {
//                    String full = file.getSource().getName();
//                    String newFull = transformFullName(full, params); // hook separato (default: usa transformName)
//                    updated.add(copyWithName(file, newFull));
//                    continue;
//                }
//            }
//
//            String finalName = newBase + (newExt.isEmpty() ? "" : "." + newExt);
//            updated.add(copyWithName(file, finalName));
//        }
//
//        return updated;
//    }

//	 public final List<RenamableFile> applyRule(List<RenamableFile> files, Object... params) {
//	        if (files == null || files.isEmpty()) return List.of();
//
//	        // Estrai il mode dall’ULTIMO parametro (se presente)
//	        RenameMode mode = RenameMode.NAME_ONLY;
//	        if (params != null && params.length > 0) {
//	            Object last = params[params.length - 1];
//	            if (last instanceof RenameMode m) {
//	                mode = m;
//	                params = Arrays.copyOf(params, params.length - 1); // rimuovi il mode dai parametri di trasformazione
//	            }
//	        }
//
//	        List<RenamableFile> updated = new ArrayList<>();
//
//	        for (RenamableFile file : files) {
//	            if (file == null) continue;
//
//	            String base = FileNameUtil.getBaseName(file.getSource().getName());   // es: "report"
//	            String ext  = FileNameUtil.getExtension(file.getSource().getName());  // es: "txt"
//
//	            switch (mode) {
//	                case NAME_ONLY -> {
//	                    String newBase = transformName(base, params);
//	                    if (newBase == null) newBase = base;
//	                    updated.add(copyWithName(file, compose(newBase, ext)));
//	                }
//	                case EXT_ONLY -> {
//	                    String newExt = transformExtension(ext, params);
//	                    if (newExt == null) newExt = ext;
//	                    updated.add(copyWithName(file, compose(base, newExt)));
//	                }
//	                case FULL -> {
//	                    String full    = file.getSource().getName();           // "report.txt"
//	                    String newFull = transformFullName(full, params);
//	                    if (newFull == null) newFull = full;
//	                    updated.add(copyWithName(file, newFull));
//	                }
//	            }
//	        }
//	        return updated;
//	    }

	
	
//	@Override
//	public final List<RenamableFile> applyRule(List<RenamableFile> files, Object... params) {
//	    if (files == null || files.isEmpty()) return List.of();
//
//	    List<RenamableFile> updated = new ArrayList<>();
//
//	    for (RenamableFile file : files) {
//	        if (file == null) continue;
//
//	        String fileName = file.getSource().getName();
//	        String base = FileNameUtil.getBaseName(fileName);   // es: "report"
//	        String ext  = FileNameUtil.getExtension(fileName);  // es: "txt" (senza punto!)
//
//	        // default = NAME_ONLY
//	        RenameMode mode = (params.length > 2 && params[2] instanceof RenameMode)
//	                ? (RenameMode) params[2]
//	                : RenameMode.NAME_ONLY;
//
//	        String newBase = base;
//	        String newExt  = ext;
//
//	        switch (mode) {
//	            case NAME_ONLY -> {
//	                newBase = transformName(base, params);
//	            }
//	            case EXT_ONLY -> {
//	                // hook per gestire l’estensione
//	                newExt = transformExtension(ext, params);
//	            }
//	            case FULL -> {
//	                String newFull = transformFullName(fileName, params);
//	                updated.add(copyWithName(file, newFull));
//	                continue; // salta join base+ext
//	            }
//	        }
//
//	        // 🔑 ricostruisci il nome finale correttamente
//	        String finalName = newBase + (newExt.isEmpty() ? "" : "." + newExt);
//	        updated.add(copyWithName(file, finalName));
//	    }
//
//	    return updated;
//	}

//	 /** default: delega alla logica del nome */
//	    protected String transformExtension(String currentExt, Object... params) {
//	        return transformName(currentExt, params);
//	    }
//
//	    /** default: delega alla logica del nome */
//	    protected String transformFullName(String fullName, Object... params) {
//	        return transformName(fullName, params);
//	    }
//
//
//	    // Helpers
//	    private static String compose(String base, String ext) {
//	        return ext == null || ext.isEmpty() ? base : base + "." + ext;
//	    }
//
//	    private static RenamableFile copyWithName(RenamableFile original, String newName) {
//	        RenamableFile copy = new RenamableFile(new File(original.getSource().getPath()));
//	        copy.setDestinationName(newName);
//	        return copy;
//	    }
	
//    protected String transformExtension(String currentExt, Object... params) {
//        // default: delega alla stessa logica del nome
//        return transformName(currentExt, params);
//    }
//
//    protected String transformFullName(String fullName, Object... params) {
//        // default: delega alla stessa logica del nome
//        return transformName(fullName, params);
//    }
//
//    private RenamableFile copyWithName(RenamableFile original, String newName) {
//        RenamableFile copy = new RenamableFile(original.getSource());
//        copy.setDestinationName(newName);
//        return copy;
//    }
    

