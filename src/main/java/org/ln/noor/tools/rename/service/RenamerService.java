package org.ln.noor.tools.rename.service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ln.noor.core.enums.FileStatus;
import org.ln.noor.core.enums.RenameMode;
import org.ln.noor.tools.rename.model.RenamableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Central service responsible for rename preview management.
 * <p>
 * {@code RenamerService} maintains the current list of loaded files,
 * applies rename rules, detects conflicts and notifies UI listeners
 * when the preview changes.
 *
 * <p>
 * This service does <strong>not</strong> perform filesystem operations.
 * It only computes rename previews and validates potential conflicts.
 *
 * <p>
 * The actual rename execution is delegated to {@link RenameController}.
 *
 * @author Luca Noale
 */
@Service
public class RenamerService {

    /** Logger instance */
    private static final Logger logger =
            LoggerFactory.getLogger(RenamerService.class);

    /**
     * Current list of files participating in the rename preview.
     * <p>
     * This list represents the single source of truth for the UI.
     */
    private final List<RenamableFile> files = new ArrayList<>();

    /**
     * Registered listeners notified when the file list changes.
     */
    private final List<RenamerServiceListener> listeners = new ArrayList<>();

    /**
     * Registry of available rename rules.
     * <p>
     * Key: lowercase rule identifier<br>
     * Value: corresponding {@link RuleService} implementation
     */
    private final Map<String, RuleService> ruleRegistry = new HashMap<>();

    /**
     * Creates the service and registers all available {@link RuleService}
     * implementations detected by Spring.
     *
     * @param ruleServices all rule services annotated as {@code @Service}
     */
    public RenamerService(List<RuleService> ruleServices) {
        for (RuleService service : ruleServices) {

            // Derive rule key from class name
            String key = service.getClass()
                    .getSimpleName()
                    .replace("RuleService", "")
                    .toLowerCase();

            ruleRegistry.put(key, service);

            logger.info(
                    "Registered rule: {} -> {}",
                    key,
                    service.getClass().getName());
        }
    }

    /**
     * Applies a rename rule to the current file list.
     * <p>
     * The rule is executed on a copy of the current files and
     * the result is merged back while preserving the original order.
     *
     * @param ruleName rule identifier (e.g. {@code "add"}, {@code "remove"})
     * @param mode     rename mode (FULL, NAME_ONLY, EXT_ONLY)
     * @param params   rule-specific parameters
     *
     * @throws IllegalArgumentException if the rule is not registered
     */
    public void applyRule(
            String ruleName,
            RenameMode mode,
            Object... params) {

        RuleService service =
                ruleRegistry.get(ruleName.toLowerCase());

        if (service == null) {
            throw new IllegalArgumentException(
                    "Unknown rule: " + ruleName);
        }

        // Work on a copy of the current file list
        List<RenamableFile> selectedFiles =
                new ArrayList<>(files);

        if (selectedFiles.isEmpty()) {
            setFiles(new ArrayList<>(files));
            return;
        }

        // Apply rule and obtain updated preview
        List<RenamableFile> updatedFiles =
                service.applyRule(selectedFiles, mode, params);

        // Index updated files by source path
        Map<Path, RenamableFile> updatedByPath =
                updatedFiles.stream()
                        .collect(Collectors.toMap(
                                f -> f.getSource().toPath(),
                                Function.identity(),
                                (existing, replacement) -> replacement));

        // Merge updates while preserving original order
        List<RenamableFile> merged =
                new ArrayList<>(files.size());

        for (RenamableFile current : files) {
            RenamableFile replacement =
                    updatedByPath.get(
                            current.getSource().toPath());

            merged.add(
                    replacement != null
                            ? replacement
                            : current);
        }

        // Update internal state and notify listeners
        setFiles(merged);
    }

    /**
     * Reapplies the current rename preview.
     * <p>
     * This method is typically used after file loading
     * or preference changes.
     */
    public void reapplyRules() {
        if (files.isEmpty()) {
            return;
        }
        setFiles(new ArrayList<>(files));
    }

    /**
     * Replaces the current file list and triggers conflict checks
     * and listener notifications.
     *
     * @param newFiles the new list of files
     */
    public void setFiles(List<RenamableFile> newFiles) {
        files.clear();
        files.addAll(newFiles);
        checkConflicts();
        notifyListeners();
    }

    /**
     * Detects rename conflicts and updates file status flags.
     * <p>
     * Conflicts are detected when:
     * <ul>
     *   <li>A destination filename already exists on disk</li>
     *   <li>Multiple files resolve to the same destination name</li>
     * </ul>
     *
     * @return {@code true} if any conflict is detected
     */
    public boolean checkConflicts() {

        if (files.isEmpty()) {
            return false;
        }

        File directory =
                files.getFirst()
                        .getSource()
                        .getParentFile();

        // Collect existing filenames in the directory
        Set<String> existingNames = new HashSet<>();
        for (File f : directory.listFiles()) {
            existingNames.add(f.getName());
        }

        Set<String> usedNames = new HashSet<>();
        boolean conflictDetected = false;

        for (RenamableFile file : files) {

            // Skip unselected files
            if (!file.isSelected()) {
                file.setFileStatus(FileStatus.OK);
                continue;
            }

            String oldName = file.getSource().getName();
            String newName = file.getDestinationName();

            // Ensure a valid destination name
            if (newName == null || newName.isBlank()) {
                newName = oldName;
                file.setDestinationName(newName);
            }

            file.setFileStatus(FileStatus.OK);

            // Conflict with existing file on disk
            if (existingNames.contains(newName)
                    && !newName.equals(oldName)) {

                file.setFileStatus(FileStatus.KO);
                conflictDetected = true;
                continue;
            }

            // Conflict with another destination name
            if (!usedNames.add(newName)) {
                file.setFileStatus(FileStatus.KO);
                conflictDetected = true;
            }
        }

        return conflictDetected;
    }

    /**
     * Reloads all files from a directory and rebuilds the preview.
     *
     * @param directory directory to reload
     */
    public void reloadDirectory(Path directory) {

        if (directory == null) {
            return;
        }

        File dirFile = directory.toFile();
        if (!dirFile.isDirectory()) {
            return;
        }

        List<RenamableFile> reloaded = new ArrayList<>();

        File[] list = dirFile.listFiles();
        if (list != null) {
            for (File f : list) {

                RenamableFile newFile =
                        new RenamableFile(f);

                // Initialize destination with source name
                newFile.setDestinationName(f.getName());
                reloaded.add(newFile);
            }
        }

        files.clear();
        files.addAll(reloaded);

        checkConflicts();
        notifyListeners();
    }

    /**
     * Updates only destination names without replacing the file list.
     *
     * @param updated updated preview list
     */
    public void updateDestinationNames(
            List<RenamableFile> updated) {

        for (int i = 0; i < files.size(); i++) {
            files.get(i).setDestinationName(
                    updated.get(i).getDestinationName());
        }
    }

    /**
     * Returns an unmodifiable view of the current file list.
     *
     * @return list of {@link RenamableFile}
     */
    public List<RenamableFile> getFiles() {
        return Collections.unmodifiableList(files);
    }

    /**
     * Registers a listener for preview updates.
     *
     * @param listener the listener to add
     */
    public void addListener(
            RenamerServiceListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a previously registered listener.
     *
     * @param listener the listener to remove
     */
    public void removeListener(
            RenamerServiceListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the file list
     * or preview has changed.
     */
    public void notifyListeners() {
        for (RenamerServiceListener listener : listeners) {
            listener.onFilesUpdated(getFiles());
        }
    }

    /**
     * Clears the current file list.
     * <p>
     * This operation affects only the application state
     * and does not modify the filesystem.
     */
    public void clear() {
        files.clear();
        notifyListeners();
    }
}
