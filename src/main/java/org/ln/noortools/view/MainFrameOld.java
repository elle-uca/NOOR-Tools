package org.ln.noortools.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
//@Component
public class MainFrameOld extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea tagArea;
    private JComboBox<String> fillCombo;
    private JButton addFileBtn, addDirBtn, renameBtn;
    private JToggleButton themeToggle;

    public MainFrameOld() {
        super("NOOR Tools (Not Only an Ordinary Renamer)");

        // ✅ Avvio in Light mode
        FlatLightLaf.setup();

        initComponents();
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 🔘 Toggle Light/Dark
        themeToggle = new JToggleButton("🌞");
        themeToggle.addActionListener(e -> switchTheme());

        // Toolbar moderna
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // no drag
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addFileBtn = new JButton("File", UIManager.getIcon("FileView.fileIcon"));
        addFileBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        addFileBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        addDirBtn = new JButton("Directory", UIManager.getIcon("FileView.directoryIcon"));
        addDirBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        addDirBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        renameBtn = new JButton("Rinomina", UIManager.getIcon("OptionPane.informationIcon"));
        renameBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        renameBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        toolBar.add(addFileBtn);
        toolBar.add(addDirBtn);
        toolBar.add(renameBtn);
        toolBar.addSeparator(new Dimension(20, 0));
        toolBar.add(themeToggle);

        // Pannello sinistro (regole/tag)
        JPanel leftPanel = new JPanel(new MigLayout("fillx, insets 10", "[grow]", "[][][grow][]"));
        leftPanel.setPreferredSize(new Dimension(280, 0));

        JLabel lblRules = new JLabel("Nuovo nome");
        lblRules.setFont(lblRules.getFont().deriveFont(Font.BOLD, 14f));

        tagArea = new JTextArea(10, 20);
        tagArea.setBorder(BorderFactory.createTitledBorder("Regole"));
        tagArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        fillCombo = new JComboBox<>(new String[]{"Nessun riempimento", "0", "X"});
        JButton addRuleBtn = new JButton("Aggiungi");

        leftPanel.add(lblRules, "wrap");
        leftPanel.add(new JScrollPane(tagArea), "grow, wrap");
        leftPanel.add(fillCombo, "split 2, growx");
        leftPanel.add(addRuleBtn, "wrap");

        // Pannello destro (tabella file)
        String[] cols = {"Name", "New Name", "Path", "Error", "Selected"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class; // checkbox
                return super.getColumnClass(columnIndex);
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);

        JScrollPane tableScroll = new JScrollPane(table);

        add(toolBar, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void switchTheme() {
        try {
            if (themeToggle.isSelected()) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                themeToggle.setText("🌙");
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
                themeToggle.setText("🌞");
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per testare la GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrameOld().setVisible(true);
        });
    }
}
