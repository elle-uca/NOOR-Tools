package org.ln.noortools.service;

import java.util.ArrayList;
import java.util.List;

import org.ln.noortools.enums.RenameMode;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.tag.AbstractTag;
import org.ln.noortools.util.FileNameUtil;
import org.springframework.stereotype.Component;

/**
 * Applies a parsed rename template to a list of files, producing new
 * {@link RenamableFile} instances with computed destination names.
 */
@Component
public class TemplateApplier {

    public TemplateApplier() {
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
            String base = FileNameUtil.getBaseName(sourceName);
            String ext  = FileNameUtil.getExtension(sourceName);

            StringBuilder destinationBuilder = new StringBuilder();
            for (Object component : components) {
                if (component instanceof AbstractTag tag) {
                    destinationBuilder.append(tag.getNewName(i));
                } else {
                    destinationBuilder.append(component.toString());
                }
            }

            String destinationName = switch (mode) {
                case NAME_ONLY -> destinationBuilder + (ext.isEmpty() ? "" : "." + ext);
                case EXT_ONLY -> FileNameUtil.combine(base, destinationBuilder.toString());
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

                tag.init();
            }
        }
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
            case NAME_ONLY -> FileNameUtil.getBaseName(filename);
            case EXT_ONLY -> FileNameUtil.getExtension(filename);
        };
    }
}
