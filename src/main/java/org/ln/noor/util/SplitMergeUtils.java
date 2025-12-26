package org.ln.noor.util;

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

public class SplitMergeUtils {

    public static class MergeResult {
        public List<String[]> mapping;               // lista [origine, destinazione]
        public int conflicts;                        // quanti file hanno richiesto suffisso
        public Map<String, Integer> filesPerFolder;  // riepilogo per sottocartella
        public List<File> sourceDirs;                // elenco sottocartelle
    }

    /**
     * Simulazione suddivisione per numero file.
     */
    public static Map<String, List<File>> simulateSplitByCount(String sourceDir, int maxFiles, String folderPrefix) {
        Map<String, List<File>> simulation = new LinkedHashMap<>();

        File folder = new File(sourceDir);
        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) return simulation;

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
     * Simulazione suddivisione per dimensione (MB).
     */
    public static Map<String, List<File>> simulateSplitBySize(String sourceDir, long maxSizeMB, String folderPrefix) {
        Map<String, List<File>> simulation = new LinkedHashMap<>();
        long maxBytes = maxSizeMB * 1024 * 1024;

        File folder = new File(sourceDir);
        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) return simulation;

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
     * Applica realmente lo split in base a una simulazione.
     */
    public static void applySplit(String sourceDir, Map<String, List<File>> simulation) throws IOException {
        for (Map.Entry<String, List<File>> entry : simulation.entrySet()) {
            Path targetDir = Paths.get(sourceDir, entry.getKey());
            Files.createDirectories(targetDir);

            for (File file : entry.getValue()) {
                Files.move(file.toPath(), targetDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * Mostra la simulazione in una JTable. Path path = Paths.get("dir1", "dir2");
     * @param path 
     */
    public static void showSimulationTable(String path, Map<String, List<File>> simulation) {
        String[] columns = {"Cartella", "Nome File", "Dimensione (KB)"};
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
                "Simulazione suddivisione",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int confirm = JOptionPane.showConfirmDialog(null, "Vuoi applicare davvero lo split?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    applySplit(path, simulation); 
                    JOptionPane.showMessageDialog(null, "Split completato!");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Errore: " + e.getMessage());
                }
            }
        }
    }
    
    
    /**
     * Simula il merge: ritorna mappatura, conflitti e riepilogo per sottocartella.
     */
    public static MergeResult simulateMerge(String parentDir, String targetDir) {
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
            if (files == null) continue;

            int countForFolder = 0;

            for (File file : files) {
                Path targetFile = targetPath.resolve(file.getName());
                boolean conflict = false;

                // gestisce conflitti con suffisso _n
                int counter = 1;
                while (Files.exists(targetFile) || containsTarget(result.mapping, targetFile)) {
                    conflict = true;
                    String name = file.getName();
                    int dot = name.lastIndexOf(".");
                    String base = (dot == -1) ? name : name.substring(0, dot);
                    String ext = (dot == -1) ? "" : name.substring(dot);
                    targetFile = targetPath.resolve(base + "_" + counter + ext);
                    counter++;
                }

                if (conflict) result.conflicts++;
                result.mapping.add(new String[]{
                        file.getAbsolutePath(),
                        targetFile.toString()
                });
                countForFolder++;
            }

            result.filesPerFolder.put(subDir.getName(), countForFolder);
        }
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Errore durante la simulazione:\n" + e.getMessage(),
                  "Errore", JOptionPane.ERROR_MESSAGE);
		}
        return result;
    }

    private static boolean containsTarget(List<String[]> mapping, Path targetFile) {
        for (String[] entry : mapping) {
            if (entry[1].equals(targetFile.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applica il merge vero e proprio, con opzione elimina cartelle vuote.
     */
    public static void applyMerge(MergeResult simulation, boolean move, boolean deleteEmptyDirs) throws IOException {
        for (String[] entry : simulation.mapping) {
            Path source = Paths.get(entry[0]);
            Path target = Paths.get(entry[1]);
            if (move) {
                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // Cancella le cartelle vuote se richiesto e se era una move
        if (move && deleteEmptyDirs) {
            for (File dir : simulation.sourceDirs) {
                if (dir.isDirectory() && Objects.requireNonNull(dir.list()).length == 0) {
                    dir.delete();
                }
            }
        }
    }

    /**
     * Mostra simulazione con riepilogo, report per sottocartella e tabella.
     */
    public static void showSimulation(MergeResult simulation, boolean move) {
        String[] columns = {"File originale", "File destinazione"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (String[] entry : simulation.mapping) {
            model.addRow(new Object[]{entry[0], entry[1]});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        // Riepilogo numerico
        int total = simulation.mapping.size();
        int conflicts = simulation.conflicts;

        StringBuilder summaryBuilder = new StringBuilder();
        summaryBuilder.append("Totale file: ").append(total)
                .append(" | Conflitti risolti: ").append(conflicts)
                .append("\n\nFile per sottocartella:\n");
        for (Map.Entry<String, Integer> entry : simulation.filesPerFolder.entrySet()) {
            summaryBuilder.append(" - ").append(entry.getKey())
                    .append(": ").append(entry.getValue()).append(" file\n");
        }

        JTextArea summaryArea = new JTextArea(summaryBuilder.toString());
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setBackground(UIManager.getColor("Label.background"));
        summaryArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JCheckBox deleteDirsCheckbox = new JCheckBox("Cancella cartelle vuote dopo merge");
        deleteDirsCheckbox.setEnabled(move); // ha senso solo se sposto i file

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(summaryArea, BorderLayout.CENTER);
        northPanel.add(deleteDirsCheckbox, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Anteprima merge (" + (move ? "SPOSTA" : "COPIA") + ")",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int confirm = JOptionPane.showConfirmDialog(null, "Vuoi applicare davvero il merge?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    applyMerge(simulation, move, deleteDirsCheckbox.isSelected());
                    JOptionPane.showMessageDialog(null, "Merge completato!");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Errore: " + e.getMessage());
                }
            }
        }
    }

}
