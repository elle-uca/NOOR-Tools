package org.ln.noortools.view.panel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.model.RenamableFile;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.service.StringParser;
import org.ln.noortools.tag.AbstractTag;
import org.ln.noortools.view.TagListCellRenderer;
import org.ln.noortools.view.TagListModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.miginfocom.swing.MigLayout;



@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class TagPanel extends AbstractPanelContent {

	private final TagListModel tagListModel; // 👈 injected by Spring
	private final RenamerService renamerService;
	private JList<AbstractTag> tagList;
	private JLabel tagLabel;
	private JTextField searchField;
	private JPanel categoryBar;
	//private FillOptionsPanel fill;
	
	  
	    
	public TagPanel(I18n i18n, RenamerService renamerService, TagListModel tagListModel) {
        super(i18n);
        this.renamerService = renamerService;
        this.tagListModel = tagListModel; // ✅ use injected model
        
	}
	    
	    // 2) Imposta il model DOPO che Spring ha iniettato tutto (e dopo il costruttore)
	    @PostConstruct
	    private void wireModel() {
	        tagList.setModel(tagListModel);
	        tagList.setCellRenderer(new TagListCellRenderer());  // 👈 AGGIUNGI QUESTO
	        
	        searchField.getDocument().addDocumentListener(new DocumentListener() {
	            private void go() {
	                tagListModel.setQuery(searchField.getText());
	            }
	            @Override public void insertUpdate(DocumentEvent e) { go(); }
	            @Override public void removeUpdate(DocumentEvent e) { go(); }
	            @Override public void changedUpdate(DocumentEvent e) { go(); }
	        });
	    
	        // opzionale: revalidate/repaint se serve
	        tagList.revalidate();
	        tagList.repaint();
	    }

	    
	@Override
	protected void initComponents(JPanel contentArea) {
    	contentArea.setLayout(new MigLayout(
                "fill, insets 15",      // padding interno
                "[grow]",               // una colonna che cresce
                "[][][grow]"            // etichetta, campo, lista
        ));

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 🔹 Etichetta
        tagLabel = new JLabel("Available Tags");
        tagLabel.setFont(tagLabel.getFont().deriveFont(13f));

        // 🔹 Campo di testo ereditato da AbstractPanelContent
        renameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        searchField = new JTextField();
        // 🔹 Lista tag
        // ⚠️ usare un modello temporaneo per evitare null
        tagList = new JList<>(new DefaultListModel<>());
       // tagList = new JList<>(tagListModel);
        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tagList.setVisibleRowCount(8);
        tagList.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        tagList.setBackground(new Color(250, 250, 250));

        // Scroll pane con bordo arrotondato
        JScrollPane scrollPane = new JScrollPane(tagList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Doppio click → aggiunge il tag al campo
        tagList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = tagList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        AbstractTag tag = tagList.getModel().getElementAt(index);
                        renameField.setText(renameField.getText() + tag.getTagString());
                    }
                }
            }
        });
        // ✅ 1. Crea la barra categorie PRIMA
        categoryBar = new JPanel(new MigLayout("insets 0, gap 6"));
        categoryBar.putClientProperty("JPanel.style", "rounded");
        buildCategoryButtons();
        JScrollPane filterScroll = new JScrollPane(categoryBar);
        filterScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filterScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        filterScroll.setBorder(BorderFactory.createEmptyBorder());
        filterScroll.getHorizontalScrollBar().setUnitIncrement(12); // scorrimento fluido
        filterScroll.setOpaque(false);
        filterScroll.getViewport().setOpaque(false);

        // 🔹 Aggiunta dei componenti con MigLayout
        contentArea.add(tagLabel, "wrap");
        contentArea.add(renameField, "growx, h 28!, wrap");
        contentArea.add(searchField, "growx, h 28!, wrap");
        contentArea.add(filterScroll,  "growx, h 40!, wrap");
        contentArea.add(scrollPane, "grow, push, h 300!");
	}



	@Override
	protected
	void updateView() {
		
	    // ✅ Non fare nulla se il testo non è ancora valido
	    if (!StringParser.isParsable(renameField.getText())) {
	        return;
	    }
		
		List<RenamableFile> updated =
			    StringParser.parse(renameField.getText(), renamerService.getFiles(), getRenameMode());
		renamerService.setFiles(updated);
		//renamerService.updateDestinationNames(updated);	
	}


	
	private void buildCategoryButtons() {
	    categoryBar.removeAll();

	    categoryBar.add(createCategoryButton(" All ", null, "/icons/infinite.png"));
	    categoryBar.add(createCategoryButton("Numeric", AbstractTag.TagType.NUMERIC, "/icons/numeri.png"));
	    categoryBar.add(createCategoryButton("String", AbstractTag.TagType.STRING, "/icons/string.png"));
	    categoryBar.add(createCategoryButton("Date/Time", AbstractTag.TagType.DATE_TIME, "/icons/date-time.png"));
	    categoryBar.add(createCategoryButton("Audio", AbstractTag.TagType.AUDIO, "/icons/audio.png"));
	    categoryBar.add(createCategoryButton("Checksum", AbstractTag.TagType.CHECKSUM, "/icons/hashtag.png"));
	    categoryBar.add(createCategoryButton("FileSystem", AbstractTag.TagType.FILE_SYSTEM, "/icons/os-info.png"));

	    categoryBar.revalidate();
	    categoryBar.repaint();
	}

	
	// Da eliminare
	private Icon getScaledIcon(String path) {
			ImageIcon originalIcon = new ImageIcon(getClass().getResource(path)); 
        
//        if (originalIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
//             System.err.println("Errore: Impossibile caricare l'immagine. Controlla il percorso.");
//             // Usa un'icona di fallback o termina
//             return; 
//        }
		// 2. SCALA L'IMMAGINE ORIGINALE
        // Otteniamo l'oggetto Image sottostante
        Image originalImage = originalIcon.getImage(); 
        
        // Usiamo getScaledInstance per ridimensionare l'immagine a 16x16.
        // Image.SCALE_SMOOTH è un algoritmo di scalatura di alta qualità.
        Image scaledImage = originalImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH);

        // 3. CREA LA NUOVA ICONA SCALATA
        // Creiamo una nuova ImageIcon dall'Image ridimensionata
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

	    return scaledIcon;
	}


	
	private JButton createCategoryButton(String text, AbstractTag.TagType type, String icon) {
	    JButton b = new JButton(text);
	    b.putClientProperty("JButton.buttonType", "roundRect");
	    b.setFocusPainted(false);
	    b.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));
	    b.setFont(b.getFont().deriveFont(12f));
	    b.setIcon(getScaledIcon(icon));

	    boolean selected = tagListModel.getTypeFilter().contains(type);

	    Color accent = resolveAccentColor();
	    Color normalBg = UIManager.getColor("Panel.background");
	    if (normalBg == null) normalBg = b.getBackground();

	    Color bg = selected ? tint(accent, 0.70f) : normalBg;
	    b.setBackground(bg);
	    b.setForeground(selected ? Color.BLACK : UIManager.getColor("Label.foreground"));

	    b.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override public void mouseEntered(java.awt.event.MouseEvent e) {
	            if (!selected) b.setBackground(tint(accent, 0.85f));
	        }
	        @Override public void mouseExited(java.awt.event.MouseEvent e) {
	            b.setBackground(bg);
	        }
	    });

	    b.addActionListener(e -> {
	        tagListModel.setSingleType(type);
	        refreshCategoryButtons();
	    });

	    return b;
	}

	
	private void refreshCategoryButtons() {
	    buildCategoryButtons();
	}
	
	
	private static Color resolveAccentColor() {
	    // Ordine di preferenza: chiavi disponibili nei vari temi FlatLaf
	    String[] keys = {
	        "Component.accentColor",          // FlatLaf >= 3.x (se disponibile)
	        "Actions.Blue",                    // palette azzurra standard FlatLaf
	        "Link.foreground",                 // spesso azzurrino tenue
	        "Table.selectionBackground",       // selezione tabella
	        "TextField.selectionBackground",   // selezione testo
	        "CheckBox.icon.selectedBackground" // accento checkbox
	    };

	    for (String k : keys) {
	        Color c = UIManager.getColor(k);
	        if (c != null) return c;
	    }
	    // fallback neutro (azzurrino tenue) se proprio non troviamo nulla
	    return new Color(0x4DA3FF);
	}

	/** versione "tint" che non crasha se base è null */
	private static Color tint(Color base, float amount) {
	    if (base == null) base = resolveAccentColor();
	    // niente ColorFunctions: facciamo un mix manuale verso il bianco
	    int r = base.getRed();
	    int g = base.getGreen();
	    int b = base.getBlue();
	    int nr = Math.round(r + (255 - r) * amount);
	    int ng = Math.round(g + (255 - g) * amount);
	    int nb = Math.round(b + (255 - b) * amount);
	    return new Color(nr, ng, nb);
	}

}
