package org.ln.noortools.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

@SuppressWarnings("serial")
//@Component
public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea tagArea;
    private JComboBox<String> fillCombo;
    private JButton addFileBtn, addDirBtn, renameBtn;
    private JToggleButton themeToggle;

    public MainFrame() {
        super("NOOR Tools (Not Only an Ordinary Renamer) by Luke");

        // ✅ Avvio in Light mode
        FlatLightLaf.setup();

        initComponents();
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }

    private void initComponents() {
        JSplitPane splitPane = new JSplitPane(
        		JSplitPane.HORIZONTAL_SPLIT, 
				createMethodPanel(), 
				createTablePanel());
		splitPane.setDividerLocation(400);
		getContentPane().add(splitPane);
    }

    private JPanel createMethodPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		return panel;
	}

    private JPanel createTablePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		return panel;
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
            new MainFrame().setVisible(true);
        });
    }
}
