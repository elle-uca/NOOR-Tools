package org.ln.noor.tools.splitmerge.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * Utility class providing split and merge operations for files.
 * <p>
 * {@code SplitMergeUtils} supports:
 * <ul>
 *   <li>Simulating file splitting into multiple folders</li>
 *   <li>Applying split operations on the filesystem</li>
 *   <li>Simulating merge operations from subfolders</li>
 *   <li>Applying merge operations with conflict resolution</li>
 *   <li>Displaying detailed previews using Swing dialogs</li>
 * </ul>
 *
 * <p>
 * All operations are designed to be previewed before execution
 * to prevent accidental filesystem changes.
 *
 * @author Luca Noale
 */
public class SplitMergeUtils {

    /**
     * Result object returned by merge simulations.
     * <p>
     * It contains the full source-to-destination mapping,
     * conflict statistics and per-folder summaries.
     */
    public static class MergeResult {

        /** List of source → destination mappings */
        public List<String[]> mapping;

        /** Number of filename conflicts resolved via suffixes */
        public int conflicts;

        /** Number of processed files per source folder */
        public Map<String, Integer> filesPerFolder;

        /** List of source directories involved in the merge */
        public List<File> sourceDirs;
    }

    /**
     * Simulates a split operation based on a maximum number of files per folder.
     *
     * @param sourceDir     source directory containing files
     * @param maxFiles      maximum number of files per target folder
     * @param folderPrefix  prefix for generated folder names
     * @return a map where keys are target folder names and values are file lists
     */
    public static Map<String, List<File>> simulateSplitByCount(
            String sourceDir,
            int maxFiles,
            String folderPrefix) {

        Map<String, List<File>> simulation = new LinkedHashMap<>();

        File folder = new File(sourceDir);
        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            return simulation;
        }

        int folderIndex = 1;
        int fileCounter = 0;

        String currentFolder = folderPrefix + folderIndex;
        simulation.put(currentFolder, new ArrayList<>());

        for (File file : files) {

            if (fileCounter >= maxFiles) {
                folderIndex++;
                currentFolder = folderPrefix + folderIndex;
                simulation.put(currentFolder, new ArrayList<>());
                fileCounter = 0;
            }

            simulation.get(currentFolder).add(file);
            fileCounter++;
        }

        return simulation;
    }

    /**
     * Simulates a split operation based on a maximum folder size in megabytes.
     *
     * @param sourceDir     source directory containing files
     * @param maxSizeMB     maximum size per folder (in MB)
     * @param folderPrefix prefix for generated folder names
     * @return a map where keys are target folder names and values are file lists
     */
    public static Map<String, List<File>> simulateSplitBySize(
            String sourceDir,
            long maxSizeMB,
            String folderPrefix) {

        Map<String, List<File>> simulation = new LinkedHashMap<>();
        long maxBytes = maxSizeMB * 1024 * 1024;

        File folder = new File(sourceDir);
        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            return simulation;
        }

        int folderIndex = 1;
        long currentSize = 0;

        String currentFolder = folderPrefix + folderIndex;
        simulation.put(currentFolder, new ArrayList<>());

        for (File file : files) {

            long fileSize = file.length();

            if (currentSize + fileSize > maxBytes) {
                folderIndex++;
                currentFolder = folderPrefix + folderIndex;
                simulation.put(currentFolder, new ArrayList<>());
                currentSize = 0;
            }

            simulation.get(currentFolder).add(file);
            currentSize += fileSize;
        }

        return simulation;
    }

    /**
     * Applies a previously simulated split operation.
     *
     * @param sourceDir  source directory
     * @param simulation split simulation data
     * @throws IOException if a filesystem error occurs
     */
    public static void applySplit(
            String sourceDir,
            Map<String, List<File>> simulation) throws IOException {

        for (Map.Entry<String, List<File>> entry : simulation.entrySet()) {

            Path targetDir = Paths.get(sourceDir, entry.getKey());
            Files.createDirectories(targetDir);

            for (File file : entry.getValue()) {
                Files.move(
                        file.toPath(),
                        targetDir.resolve(file.getName()),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * Displays a split simulation in a table and optionally applies it.
     *
     * @param path        base directory path
     * @param simulation split simulation data
     */
    public static void showSimulationTable(
            String path,
            Map<String, List<File>> simulation) {

        String[] columns = {
                "Target Folder",
                "File Name",
                "Size (KB)"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Map.Entry<String, List<File>> entry : simulation.entrySet()) {
            String folder = entry.getKey();
            for (File file : entry.getValue()) {
                model.addRow(new Object[]{
                        Paths.get(path, folder),
                        file.getName(),
                        file.length() / 1024
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        int result = JOptionPane.showConfirmDialog(
                null,
                scrollPane,
                "Split simulation preview",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to apply the split operation?");

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    applySplit(path, simulation);
                    JOptionPane.showMessageDialog(
                            null,
                            "Split completed successfully.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Simulates a merge operation by collecting files from subfolders.
     *
     * @param parentDir parent directory containing subfolders
     * @param targetDir destination directory
     * @return a {@link MergeResult} containing preview data
     */
    public static MergeResult simulateMerge(
            String parentDir,
            String targetDir) {

        MergeResult result = new MergeResult();
        result.mapping = new ArrayList<>();
        result.conflicts = 0;
        result.filesPerFolder = new LinkedHashMap<>();
        result.sourceDirs = new ArrayList<>();

        File parent = new File(parentDir);
        File[] subDirs = parent.listFiles(File::isDirectory);

        if (subDirs == null || subDirs.length == 0) {
            return result;
        }

        Path targetPath = Paths.get(targetDir);

        try {
            Files.createDirectories(targetPath);

            for (File subDir : subDirs) {

                result.sourceDirs.add(subDir);

                File[] files = subDir.listFiles(File::isFile);
                if (files == null) {
                    continue;
                }

                int countForFolder = 0;

                for (File file : files) {

                    Path targetFile = targetPath.resolve(file.getName());
                    boolean conflict = false;
                    int counter = 1;

                    // Resolve conflicts using incremental suffixes
                    while (Files.exists(targetFile)
                            || containsTarget(result.mapping, targetFile)) {

                        conflict = true;
                        String name = file.getName();
                        int dot = name.lastIndexOf('.');
                        String base = (dot == -1)
                                ? name
                                : name.substring(0, dot);
                        String ext = (dot == -1)
                                ? ""
                                : name.substring(dot);

                        targetFile = targetPath.resolve(
                                base + "_" + counter + ext);
                        counter++;
                    }

                    if (conflict) {
                        result.conflicts++;
                    }

                    result.mapping.add(new String[]{
                            file.getAbsolutePath(),
                            targetFile.toString()
                    });

                    countForFolder++;
                }

                result.filesPerFolder.put(
                        subDir.getName(),
                        countForFolder);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error during merge simulation:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return result;
    }

    /**
     * Checks whether a target path already exists in the mapping.
     *
     * @param mapping    current mapping list
     * @param targetFile target path
     * @return {@code true} if the target already exists
     */
    private static boolean containsTarget(
            List<String[]> mapping,
            Path targetFile) {

        for (String[] entry : mapping) {
            if (entry[1].equals(targetFile.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies a merge operation using the provided simulation.
     *
     * @param simulation       merge simulation data
     * @param move             {@code true} to move files, {@code false} to copy
     * @param deleteEmptyDirs  whether to delete empty source directories
     * @throws IOException if a filesystem error occurs
     */
    public static void applyMerge(
            MergeResult simulation,
            boolean move,
            boolean deleteEmptyDirs) throws IOException {

        for (String[] entry : simulation.mapping) {

            Path source = Paths.get(entry[0]);
            Path target = Paths.get(entry[1]);

            if (move) {
                Files.move(
                        source,
                        target,
                        StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(
                        source,
                        target,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Optionally remove empty source directories
        if (move && deleteEmptyDirs) {
            for (File dir : simulation.sourceDirs) {
                if (dir.isDirectory()
                        && Objects.requireNonNull(dir.list()).length == 0) {
                    dir.delete();
                }
            }
        }
    }

    /**
     * Displays a merge simulation preview and optionally applies it.
     *
     * @param simulation merge simulation data
     * @param move       {@code true} for move, {@code false} for copy
     */
    public static void showSimulation(
            MergeResult simulation,
            boolean move) {

        String[] columns = {
                "Source File",
                "Target File"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (String[] entry : simulation.mapping) {
            model.addRow(new Object[]{entry[0], entry[1]});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        // Build summary text
        StringBuilder summaryBuilder = new StringBuilder();
        summaryBuilder.append("Total files: ")
                .append(simulation.mapping.size())
                .append(" | Conflicts resolved: ")
                .append(simulation.conflicts)
                .append("\n\nFiles per source folder:\n");

        for (Map.Entry<String, Integer> entry
                : simulation.filesPerFolder.entrySet()) {

            summaryBuilder.append(" - ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(" files\n");
        }

        JTextArea summaryArea = new JTextArea(summaryBuilder.toString());
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setBackground(
                UIManager.getColor("Label.background"));
        summaryArea.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JCheckBox deleteDirsCheckbox =
                new JCheckBox("Delete empty source folders after merge");
        deleteDirsCheckbox.setEnabled(move);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(summaryArea, BorderLayout.CENTER);
        northPanel.add(deleteDirsCheckbox, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Merge preview (" + (move ? "MOVE" : "COPY") + ")",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to apply the merge operation?");

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    applyMerge(
                            simulation,
                            move,
                            deleteDirsCheckbox.isSelected());
                    JOptionPane.showMessageDialog(
                            null,
                            "Merge completed successfully.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Error: " + e.getMessage());
                }
            }
        }
    }
}
