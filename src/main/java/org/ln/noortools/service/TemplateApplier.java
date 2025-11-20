package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.tag.AbstractTag;
import org.springframework.stereotype.Component;

@Component
public class TemplateApplier {

    private final ActionManager actionManager;
    private final PerformManager performManager;

    public TemplateApplier(ActionManager actionManager, PerformManager performManager) {
        this.actionManager = actionManager;
        this.performManager = performManager;
    }

    public List<RenamableFile> apply(
            List<Object> components,
            List<RenamableFile> files,
            RenameMode mode
    ) {
        List<String> oldNames = getOldStrings(files, mode);
        List<String> newNames = getNewStrings(files, mode);

        initializeTags(components, files, oldNames, newNames);

        List<RenamableFile> updated = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            RenamableFile file = files.get(i);
            RenamableFile copy = new RenamableFile(file.getSource());

            String sourceName = file.getSource().getName();
            String base = baseNameOf(sourceName);
            String ext  = extensionOf(sourceName);

            StringBuilder destinationBuilder = new StringBuilder();
            for (Object component : components) {
                if (component instanceof AbstractTag tag) {
                    destinationBuilder.append(tag.getNewName(i));
                } else {
                    destinationBuilder.append(component.toString());
                }
            }

            String destinationName = switch (mode) {
                case FULL -> destinationBuilder.toString();
                case NAME_ONLY -> destinationBuilder + (ext.isEmpty() ? "" : "." + ext);
                case EXT_ONLY -> base + "." + destinationBuilder;
            };

            copy.setDestinationName(destinationName);
            updated.add(copy);
        }
        return updated;
    }

    private void initializeTags(
            List<Object> components,
            List<RenamableFile> files,
            List<String> oldNames,
            List<String> newNames
    ) {
        for (Object component : components) {
            if (component instanceof AbstractTag tag) {
                tag.setOldNames(new ArrayList<>(oldNames));
                tag.setNewNames(new ArrayList<>(newNames));

                if (tag instanceof FileAwareTag fileAwareTag) {
                    fileAwareTag.setFilesContext(files);
                }

                if (tag instanceof ActionTag actionTag) {
                    actionManager.registerActionTag(actionTag);
                }

                if (tag instanceof PerformTag performTag) {
                    performManager.registerActionTag(performTag);
                }

                tag.init();
            }
        }
    }

    private String baseNameOf(String name) {
        int dot = name.lastIndexOf('.');
        return (dot > 0) ? name.substring(0, dot) : name;
    }

    private String extensionOf(String name) {
        int dot = name.lastIndexOf('.');
        return (dot > 0 && dot < name.length() - 1) ? name.substring(dot + 1) : "";
    }

    private List<String> getOldStrings(List<RenamableFile> files, RenameMode mode) {
        List<String> result = new ArrayList<>();
        for (RenamableFile f : files) {
            String src = f.getSource().getName();
            result.add(selectPart(src, mode));
        }
        return result;
    }

    private List<String> getNewStrings(List<RenamableFile> files, RenameMode mode) {
        List<String> result = new ArrayList<>();
        for (RenamableFile f : files) {
            String dest = f.getDestinationName();
            if (dest == null || dest.isBlank()) dest = f.getSource().getName();
            result.add(selectPart(dest, mode));
        }
        return result;
    }

    private String selectPart(String filename, RenameMode mode) {
        return switch (mode) {
            case FULL -> filename;
            case NAME_ONLY -> baseNameOf(filename);
            case EXT_ONLY -> extensionOf(filename);
        };
    }
}
